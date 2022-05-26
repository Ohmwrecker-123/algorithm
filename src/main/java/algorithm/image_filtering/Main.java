package algorithm.image_filtering;


/**
 * @author Ohmwrecker
 */
public class Main {
    public static void main(String[] args){
        try {
            String path = "C:\\Users\\Ohmwrecker\\Desktop\\作业2-5.26\\";
            int[][][] pixels = ImagePixelTool.readPixelsFromImage(path + "origin.jpeg");
            // 执行去色，输出灰度图
            int[][] grayPixes = ImagePixelTool.convertColorToGray(pixels);
            // 执行存储
            ImagePixelTool.writePixelsToImage(path + "gray.jpeg", grayPixes);
            // 执行噪声添加，输出噪声灰度图
            int[][] grayPixelsWithNoise = ImagePixelTool.addNoise(grayPixes, 0.02);
            // 执行存储
            ImagePixelTool.writePixelsToImage(path + "gray-noise.jpeg", grayPixelsWithNoise);
            // 图片高宽
            int height = grayPixelsWithNoise.length, width = grayPixelsWithNoise[0].length;
            // Mask大小设置
            int maskLength = 9;
            int maskHeight = 9;
            // 操作grayPixelsWithNoise数组实现各种滤波器
            int[][] grayPixelsWithoutNoise = medianFilter(grayPixelsWithNoise, height, width, maskLength, maskHeight);
            // 输出中值滤波降噪后的图片
            ImagePixelTool.writePixelsToImage(path + "gray-withoutNoise.jpeg", grayPixelsWithoutNoise);
        }
        catch (Exception e){
            e.printStackTrace();
        }
//        int[][] test = new int[3][3];
//        for (int i = 0; i < 3; i++) {
//            for (int i1 = 0; i1 < 3; i1++) {
//                test[i][i1] = i + i1;
//                System.out.println("test[" +i+ "]" + "[" + i1 + "]=" + (i + i1));
//            }
//        }
//
//        System.out.println(maskFilter(test));


    }

    public static int[][] medianFilter(int[][] grayPixelsWithNoise,int height,int length,int maskLength,int maskHeight){
        int[][] currentMask = new int[maskHeight][maskLength];
        int[][] grayPixelsWithoutNoise = new int[height-maskHeight+1][length-maskLength+1];
        for (int startRow = 0; startRow < length - maskLength + 1; startRow++) {
            for (int startColumn = 0; startColumn < height - maskHeight + 1; startColumn++) {
                for (int currentMaskLength = 0; currentMaskLength < maskLength; currentMaskLength++) {
                    for (int currentMaskHeight = 0; currentMaskHeight < maskHeight; currentMaskHeight++) {
                        currentMask[currentMaskLength][currentMaskHeight] = grayPixelsWithNoise[startRow+currentMaskLength][startColumn+currentMaskHeight];
                    }
                }
                int currentData = maskFilter(currentMask);
                grayPixelsWithoutNoise[startRow][startColumn] = currentData;
            }
        }
        return grayPixelsWithoutNoise;
    }

    public static int maskFilter(int[][] mask){
        int height = mask.length;
        int length = mask[0].length;
        int returnNumber = 0;
        int compareLength = height*length;
        int[] compare = new int[compareLength];
        int currentCompare = 0;
        for (int i1 = 0; i1 < height; i1++) {
            for (int i2 = 0; i2 < length; i2++) {
                compare[currentCompare] = mask[i1][i2];
//                System.out.println("compare[" +currentCompare+ "]" + "=" + mask[i1][i2]);
                currentCompare++;
            }
        }
        for (int i2 = 0; i2 < compareLength; i2++) {
            for (int i = 0; i < compareLength; i++) {
                int currentNumberFirst;
                int currentNumberSecond;
                currentNumberFirst = compare[i];
                currentNumberSecond = compare[i2];
                if (currentNumberFirst > currentNumberSecond){
                    compare[i] = currentNumberSecond;
                    compare[i2] = currentNumberFirst;
                }
            }
        }
        for (int i = 0; i < compareLength; i++) {
//            System.out.println("compare[" +i+ "]" + "=" + compare[i]);
        }
        returnNumber = compare[(compareLength-1)/2];
        return returnNumber;
    }

}