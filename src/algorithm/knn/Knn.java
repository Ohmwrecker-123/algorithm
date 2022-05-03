package algorithm.knn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ohmwrecker
 */
public class Knn {

    public static void main(String[] args){
        try{
            // 读取训练集

            // 训练集特征向量
            List<int[]> trainVectors = new ArrayList<>();
            // 训练集Label
            List<Integer> trainLabels = new ArrayList<>();
            readDataSet("C:\\Users\\Ohmwrecker\\Downloads\\作业1\\train.csv", trainVectors, trainLabels);
            // 读取测试集

            // 测试集特征向量
            List<int[]> testVectors = new ArrayList<>();
            // 测试集Label
            List<Integer> testLabels = new ArrayList<>();

            readDataSet("C:\\Users\\Ohmwrecker\\Downloads\\作业1\\test_200.csv", testVectors, testLabels);

            int count1 = 0,count2 = 0,count3 = 0,count4 = 0,count5 = 0,count6 = 0,count7 = 0,count8 = 0,count9 = 0,count0 = 0;

            for (Integer integer : knn(testVectors, trainVectors, trainLabels)) {
                if (integer == 1){
                    count1++;
                }
                if (integer == 2){
                    count2++;
                }
                if (integer == 3){
                    count3++;
                }
                if (integer == 4){
                    count4++;
                }
                if (integer == 5){
                    count5++;
                }
                if (integer == 6){
                    count6++;
                }
                if (integer == 7){
                    count7++;
                }
                if (integer == 8){
                    count8++;
                }
                if (integer == 9){
                    count9++;
                }
                if (integer == 0){
                    count0++;
                }
            }
            System.out.println("count1:" + count1);
            System.out.println("count2:" + count2);
            System.out.println("count3:" + count3);
            System.out.println("count4:" + count4);
            System.out.println("count5:" + count5);
            System.out.println("count6:" + count6);
            System.out.println("count7:" + count7);
            System.out.println("count8:" + count8);
            System.out.println("count9:" + count9);
            System.out.println("count0:" + count0);
            System.out.println("allCounts:" + (count0+count1+count2+count3+count4+count5+count6+count7+count8+count9));



        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static List<Integer> knn(List<int[]> testVectors, List<int[]> trainVectors, List<Integer> trainLabels){

//        for (int[] testVector : testVectors) {
//            for (int[] trainVector : trainVectors) {
//                for (int test : testVector) {
//                    for (int train : trainVector) {
//                        int testK = test - train;
//                    }
//                }
//            }
//        }

        final double K = 500.0;
        double result = 0;
        int currentTrainLabel = 0;
        List<Integer> passedTestLabel = new ArrayList<>();



        // 42001/1
        for (int[] trainVector : trainVectors) {
            // 200/1
            for (int[] testVector : testVectors) {
                for (int currentTestVector = 0; currentTestVector < 784; currentTestVector++) {
                    double currentTestResult = testVector[currentTestVector] - trainVector[currentTestVector];
                    result += Math.pow(currentTestResult,2);
                    if (currentTestVector == 783){
                        if (Math.sqrt(result) < K){
                            passedTestLabel.add(trainLabels.get(currentTrainLabel));
                        }
                        result = 0;
                    }
                }
            }
            currentTrainLabel ++;
        }
        return passedTestLabel;
    }

    /**
     * 读取特征向量和label
     * @author Teacher
     */
    private static void readDataSet(String path, List<int[]> vectors, List<Integer> labels) throws Exception{
        // 数据集文件名
        File trainFile = new File(path);
        BufferedReader br = new BufferedReader(new FileReader(trainFile));
        String line;
        // 忽略表头
        br.readLine();
        // 遍历所有行

        // 使用readLine方法，一次读一行
        while ((line = br.readLine()) != null) {
            String[] items = line.split(",");
            int[] vector = new int[items.length - 1];
            // 读取第一列，写入label
            labels.add(Integer.parseInt(items[0]));
            // 遍历后续列，写入特征向量
            for(int i = 1; i < items.length; i ++){
                vector[i - 1] = Integer.parseInt(items[i]);
            }
            vectors.add(vector);
        }
        br.close();
    }
}