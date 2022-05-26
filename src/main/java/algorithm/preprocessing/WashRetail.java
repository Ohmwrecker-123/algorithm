package algorithm.preprocessing;

import com.alibaba.fastjson.JSON;
import com.csvreader.CsvReader;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ohmwrecker
 */
public class WashRetail {
    public static void main(String[] args){
        try{
            String filePath = "C:\\Users\\Ohmwrecker\\Desktop\\实验1-5.29\\Online Retail.csv";
            ArrayList<String[]> originList = new ArrayList<String[]>();

			//读取csv文件
			CsvReader reader = new CsvReader(filePath, ',', StandardCharsets.UTF_8);
            while (reader.readRecord()) {
                originList.add(reader.getValues());
            }
            // 删除第一行的标签
            originList.remove(0);
            Pattern quantityRegex = Pattern.compile("^\\d+$");
            Pattern priceRegex = Pattern.compile("^\\d+(\\.\\d+)?$");
            List<MonthVolume> volumeList = new ArrayList();

            //处理无效数据并统计每月销售量，统计结果写入volumeList对象
			//MonthVolume.Month为月份，格式为yyyy-M
			//MonthVolume.Volume为销售额：每月所有商品×单价的金额之和

            // 订单总数
            System.out.println("订单总数" + originList.size());
            ArrayList<String[]> listCleaned = listCleaner(originList, quantityRegex, priceRegex);
            // 有效订单总数
            System.out.println("有效订单总数" + listCleaned.size());
            // 商品总数
            System.out.println("商品总数" + getMerchandiseQuantity(originList));
            // 有效商品总数
            System.out.println("有效商品总数" + getMerchandiseQuantity(listCleaned));
            // 用户总数
            System.out.println("用户总数" + getUserQuantity(originList));
            // 非空有效用户总数
            System.out.println("非空有效用户总数" + getUserQuantity(listCleaned));
            // 国家数
            System.out.println("国家数" + getCountryQuantity(listCleaned));

            //输出JSON并存储至文件
            String json = JSON.toJSONString(sortMonthVolume(getMonthVolume(listCleaned, volumeList)));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath.replace(".csv", ".json")));
            bufferedWriter.write(json);
            bufferedWriter.flush();
            bufferedWriter.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 用于清洗数据
     * @param originList 输入未清洗数据
     * @param quantityRegex 匹配数量的正则表达式
     * @param priceRegex 匹配价格的正则表达式
     * @return 输出已清洗的数据
     */
    public static ArrayList<String[]> listCleaner(ArrayList<String[]> originList,Pattern quantityRegex,Pattern priceRegex) {
        ArrayList<String[]> listCleaned = new ArrayList<>();
        for (String[] currentList : originList) {
            if (quantityRegex.matcher(currentList[3]).matches()) {
                if (priceRegex.matcher(currentList[5]).matches()) {
                    listCleaned.add(currentList);
                }
            }
        }
        return listCleaned;
    }

    /**
     * 用于获取月份销售额
     * @param originList 输入数据
     * @param volumeList MonthVolume的列表
     * @return 按月份的商品销售额
     */
    public static List<MonthVolume> getMonthVolume(ArrayList<String[]> originList,List<MonthVolume> volumeList){
        for (String[] currentList : originList) {
            String tempString = currentList[4];
            int indexOf = tempString.indexOf("/");
            int lastIndexOf = tempString.lastIndexOf("/");
            MonthVolume monthVolume = new MonthVolume();
            // 拼接年份和月份
            monthVolume.setMonth(tempString.substring(lastIndexOf + 1, lastIndexOf + 5) + "-" + tempString.substring(0,indexOf));
            // 获得该日商品销售额
            monthVolume.setVolume(Double.parseDouble(currentList[3]) * Double.parseDouble(currentList[5]));
            volumeList.add(monthVolume);
        }
        return volumeList;
    }

    /**
     * 用于将以月份分类的销售和汇总为每月总销售额
     * @param volumeList 月份销售额
     * @return 每月总销售额
     */
    public static List<MonthVolume> sortMonthVolume(List<MonthVolume> volumeList){
        LinkedHashMap<String,Double> value = new LinkedHashMap(20);
        List<MonthVolume> sortedMonthVolume = new ArrayList<>();
        for (int i = 1; i < volumeList.size(); i++) {
            if(!value.containsKey(volumeList.get(i-1).getMonth())){
                value.put(volumeList.get(i-1).getMonth(),volumeList.get(i-1).getVolume());
            }
            if (value.containsKey(volumeList.get(i).getMonth())){
                double temp = value.get(volumeList.get(i-1).getMonth());
                temp += volumeList.get(i).getVolume();
                value.replace(volumeList.get(i - 1).getMonth(), temp);
            }
        }
        for (String s : value.keySet()) {
            sortedMonthVolume.add(new MonthVolume().setMonth(s).setVolume(value.get(s)));
        }
        return sortedMonthVolume;
    }

    /**
     * 用于计算货物数量
     * @param list 输入数据
     * @return 货物数量
     */
    public static double getMerchandiseQuantity(ArrayList<String[]> list){
        double merchandiseQuantity = 0;
        for (String[] strings : list) {
            merchandiseQuantity += Double.parseDouble(strings[3]);
        }
        return merchandiseQuantity;
    }

    /**
     * 用于计算用户数量
     * @param list 输入数据
     * @return 用户数量
     */
    public static int getUserQuantity(ArrayList<String[]> list){
        ArrayList<String> userList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (i<1){
                userList.add(list.get(i)[6]);
            }else {
                if (!userList.contains(list.get(i)[6])) {
                    userList.add(list.get(i)[6]);
                }
            }
        }
        return userList.size();
    }

    /**
     * 用于计算国家数量
     * @param list 输入数据
     * @return 国家数量
     */
    public static int getCountryQuantity(ArrayList<String[]> list){
        ArrayList<String> countryList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (i<1){
                countryList.add(list.get(i)[7]);
            }else {
                if (!countryList.contains(list.get(i)[7])) {
                    countryList.add(list.get(i)[7]);
                }
            }
        }
        return countryList.size();
    }
}

/**
 * 按月份分割的销售额
 */
@Data
@Accessors(chain = true)
class MonthVolume {
    private String month = "";
    private double volume = -1;
}

