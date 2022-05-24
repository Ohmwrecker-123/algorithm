package algorithm.image_filtering;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

/**
 * @author Teacher
 */
public class ImagePixelTool {
    /**
     * 读取图片像素值
     * @param path
     * @return
     * @throws IOException
     */
    public static int[][][] readPixelsFromImage(String path) throws IOException {
        BufferedImage image = ImageIO.read(new File(path));
        byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        int width = image.getWidth(), height = image.getHeight();
        int[][][] result = new int[height][width][];
        int colorSize = image.getColorModel().getPixelSize() / 8;
        for (int pixelIndex = 0, row = 0, col = 0; pixelIndex < pixels.length; pixelIndex += colorSize) {
            result[row][col] = new int[colorSize];
            for(int i = 0; i < colorSize; i ++){
                result[row][col][i] = pixels[pixelIndex + (colorSize - i - 1)] & 0xFF;
            }
            col++;
            if (col == width) {
                col = 0;
                row++;
            }
        }
        return result;
    }

    /**
     * 为灰度图添加椒盐噪声
     * @param pixels
     * @param p 噪声像素概率
     * @return
     */
    public static int[][] addNoise(int[][] pixels, double p){
        int height = pixels.length, width = pixels[0].length;
        int[][] pixelsWithNoise = new int[height][width];
        for(int row = 0; row < height; row ++){
            for(int col = 0; col < width; col ++){
                double noiseFlag = Math.random();
                //添加噪声像素
                if(noiseFlag <= p) {
                    pixelsWithNoise[row][col] = noiseFlag < p / 2 ? 0 : 255; //噪声颜色
                }
                    //非噪声像素
                else {
                    pixelsWithNoise[row][col] = pixels[row][col];
                }
            }
        }
        return  pixelsWithNoise;
    }

    /**
     * 写入灰度图片像素值
     * @param path
     * @param grayPixels
     * @throws IOException
     */
    public static void writePixelsToImage(String path, int[][] grayPixels) throws IOException{
        int height = grayPixels.length, width = grayPixels[0].length;
        int[][][] pixels = new int[height][width][1];
        for(int row = 0; row < height; row ++){
            for(int col = 0; col < width; col ++){
                pixels[row][col] = new int[]{ grayPixels[row][col] };
            }
        }
        writePixelsToImage(path, pixels);
    }

    /**
     * 执行写入图片像素值
     * @param path
     * @param pixels
     * @throws IOException
     */
    public static void writePixelsToImage(String path, int[][][] pixels) throws IOException{
        int height = pixels.length, width = pixels[0].length, colorSize = pixels[0][0].length;
        int colorType = -1;
        switch (colorSize){
            case  4:
                colorType = BufferedImage.TYPE_INT_ARGB;
                break;
            case  3:
                colorType = BufferedImage.TYPE_INT_RGB;
                break;
            default:
                colorType = BufferedImage.TYPE_BYTE_GRAY;
        }
        BufferedImage imageNew = new BufferedImage(width,height, colorType);
        WritableRaster rasterNewImage = imageNew.getRaster();
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int[] bytes = new int[pixels[row][col].length];
                for(int i = 0; i < pixels[row][col].length; i ++) {
                    bytes[i] = (int)(byte)pixels[row][col][i];
                }
                rasterNewImage.setPixel(col, row, bytes);
            }
        }
        imageNew.setData(rasterNewImage);
        File outFile = new File(path);
        String fileName = outFile.getName();
        //写图片
        ImageIO.write(imageNew, fileName.substring(fileName.lastIndexOf(".") + 1), outFile);
    }

    /**
     * 彩色像素转灰度
     * @param pixels
     * @return
     */
    public static int[][] convertColorToGray(int[][][] pixels) {
        int height = pixels.length;
        int[][] grayPixels = new int[height][];
        for(int row = 0; row < height; row ++){
            int width = pixels[row].length;
            grayPixels[row] = new int[width];
            for(int col = 0; col < width; col ++){
                switch (pixels[row][col].length){
                    case 4:
                        grayPixels[row][col] = (int)((double)pixels[row][col][1] * 0.299 + (double)pixels[row][col][2] * 0.58 + (double)pixels[row][col][3] * 0.114);
                        break;
                    case 3:
                        grayPixels[row][col] = (int)((double)pixels[row][col][0] * 0.299 + (double)pixels[row][col][1] * 0.58 + (double)pixels[row][col][2] * 0.114);
                        break;
                    default:
                        grayPixels[row][col] = pixels[row][col][0];
                }
            }
        }
        return grayPixels;
    }
}
