package algorithm.data_mining_weka;

import com.csvreader.CsvReader;
import weka.classifiers.Evaluation;

import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author Ohmwrecker git test
 */
public class wekaTest {
    static List<String> attrTags = Arrays.asList("Iris-setosa","Iris-versicolor","Iris-virginica");
    static List<String> numAttrTags = Arrays.asList("0","1","2","3","4","5","6","7","8","9");
    static List<String> wineAttrTags = Arrays.asList("0","1","2","3","4","5","6","7","8","9");

    public static Instances createIrisSample(){

        return new Instances("iris", new ArrayList<>(Arrays.asList(
                // 花萼
                new Attribute("calyxLength"),
                new Attribute("calyxWidth"),
                // 花瓣
                new Attribute("petalLength"),
                new Attribute("petalWidth"),
                new Attribute("irisType",attrTags))),0);
    }

    public static Instances createWineSample(){
        return new Instances("wine", new ArrayList<>(Arrays.asList(
                new Attribute("fixed acidity"),
                new Attribute("volatile acidity"),
                new Attribute("citric acid"),
                new Attribute("residual sugar"),
                new Attribute("chlorides"),
                new Attribute("free sulfur dioxide"),
                new Attribute("total sulfur dioxide"),
                new Attribute("density"),
                new Attribute("pH"),
                new Attribute("sulphates"),
                new Attribute("alcohol"),
                new Attribute("quality",wineAttrTags))),0);
    }

    public static Instances writeWineInstances(Instances instances,ArrayList<String[]> originList) {
        for (String[] strings : originList) {
            instances.add(new DenseInstance(1.0, new double[]{
                    Double.parseDouble(strings[0]),
                    Double.parseDouble(strings[1]),
                    Double.parseDouble(strings[2]),
                    Double.parseDouble(strings[3]),
                    Double.parseDouble(strings[4]),
                    Double.parseDouble(strings[5]),
                    Double.parseDouble(strings[6]),
                    Double.parseDouble(strings[7]),
                    Double.parseDouble(strings[8]),
                    Double.parseDouble(strings[9]),
                    Double.parseDouble(strings[10]),
                    wineAttrTags.indexOf(strings[11]),
            }));
        }
        instances.setClassIndex(instances.numAttributes() - 1);
        return instances;
    }

    public static Instances getNumTrainSet() throws Exception {

        CSVLoader csvLoader = new CSVLoader();
        csvLoader.setSource(new File("C:\\Users\\Ohmwrecker\\Desktop\\作业1\\train.csv"));
        Instances train = csvLoader.getDataSet();
        List<Integer> numList = new ArrayList<>();
        for (int i = 0; i < train.size(); i++) {
            numList.add(i, (int) train.get(i).value(0));
        }
        train.replaceAttributeAt(new Attribute("numType",numAttrTags),0);
        for (int i = 0; i < train.size(); i++) {
            train.get(i).setValue(0,numList.get(i));
        }
        train.setClassIndex(0);
        return train;
    }

    public static Instances getNumTestSet() throws Exception {

        CSVLoader csvLoader = new CSVLoader();
        csvLoader.setSource(new File("C:\\Users\\Ohmwrecker\\Desktop\\作业1\\test_200.csv"));
        Instances test = csvLoader.getDataSet();
        List<Integer> numList = new ArrayList<>();
        for (int i = 0; i < test.size(); i++) {
            numList.add(i, (int) test.get(i).value(0));
        }
        test.replaceAttributeAt(new Attribute("numType",numAttrTags),0);
        for (int i = 0; i < test.size(); i++) {
            test.get(i).setValue(0,numList.get(i));
        }
        test.setClassIndex(0);
        return test;
    }

    public static ArrayList<String[]> getIrisList() throws IOException {
        String filePath = "C:\\Users\\Ohmwrecker\\Desktop\\实验2-6.2\\Iris\\iris.csv";
        ArrayList<String[]> originList = new ArrayList<>();

        //读取csv文件
        CsvReader reader = new CsvReader(filePath, ',', StandardCharsets.UTF_8);
        while (reader.readRecord()) {
            originList.add(reader.getValues());
        }
        return originList;
    }

    public static ArrayList<String[]> getWineList() throws IOException {
        String filePath = "C:\\Users\\Ohmwrecker\\Desktop\\大作业-6.12\\大作业选题v2.0\\大作业选题\\Red_Wine_Quality\\winequality-red.csv";
        ArrayList<String[]> originList = new ArrayList<>();

        //读取csv文件
        CsvReader reader = new CsvReader(filePath, ',', StandardCharsets.UTF_8);
        while (reader.readRecord()) {
            originList.add(reader.getValues());
        }
        originList.remove(0);
        return originList;
    }

    public static Instances writeIrisInstances(Instances instances,ArrayList<String[]> originList){

        //创建一个密集向量DenseInstance，写入样本集Instances
        for (String[] strings : originList) {
            instances.add(new DenseInstance(1.0, new double[]{
                    //写入第1列score，数值型
                    Double.parseDouble(strings[0]),
                    //写入第2列date，日期型（日期字符串转为Double）
                    Double.parseDouble(strings[1]),
                    //写入第3列subject，标称型（标称值转为List下标值）
                    Double.parseDouble(strings[2]),
                    //写入第4列name，字符串型（字符串转为Double）
                    Double.parseDouble(strings[3]),
                    //第五行
                    attrTags.indexOf(strings[4]),
            }));
            //设置类别属性（标签属性）
            //设置最后一列为标签
            instances.setClassIndex(instances.numAttributes() - 1);
        }
        return instances;
    }

    public static void main(String[] args) throws Exception {

        Instances data = writeWineInstances(createWineSample(),getWineList());
        //打乱顺序
        data.randomize(new Random());
        //划分训练集和测试集，按8:2的比例
        int trainSize = (int)(data.size() * 0.8);
        //将data从下标0开始划一个长度为trainSize的训练集
        Instances train = new Instances(data, 0, trainSize);
//        Instances train = getNumTrainSet();
        //将data从下标trainSize开始划一个长度为data.size() - trainSize的测试集
        Instances test = new Instances(data, trainSize, data.size() - trainSize);
//        Instances test = getNumTestSet();
        //1、创建样本集，划分训练集和测试集
        //2、模型的创建和训练
        //创建选项
        String[] options = new String[1];
        //不修剪树（不同分类器的选项值参见2.5官方文档）
        options[0] = "-U";
        //创建J48决策树分类器对象
        J48 cls = new J48();
        //设置选项
//        cls.setOptions(options);
        //训练模型
        cls.buildClassifier(train);
        //3、模型的测试（评估）
        Evaluation eval = new Evaluation(train);
        //执行测试
        eval.evaluateModel(cls, test);
        //打印测试结果摘要信息
        System.out.println(eval.toSummaryString("Test Summary\n", false));

    }
}
