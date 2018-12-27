package com.beawan.OcrJavaTest;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.List;

public class ImgTest {
    public int[][] pixelList; // 存储像素模型
    public int[][] pixelListTarget; // 存储像素
    public int[] verticalMapping; // 垂直投影黑点数量
    public int[] horizontalMapping; // 水平投影黑点数量

    public void readPixel(String fileName) throws Exception {
        File file = new File(fileName);
        BufferedImage bi = ImageIO.read(file);
        int width = bi.getWidth();
        int height = bi.getHeight();
        int minX = bi.getMinX();
        int minY = bi.getMinY();
        pixelList = new int[height - minY][width - minX];
        pixelListTarget = new int[height - minY][width - minX];
        verticalMapping = new int[width - minX]; // 纵向映射
        horizontalMapping = new int[height - minY]; // 横向映射

        for (int i = minX; i < width; i++) {
            for (int j = minY; j < height; j++) {
                int pixel = bi.getRGB(i, j);
                pixelListTarget[j][i] = pixel;
                if (pixel != -1) {
                    pixel = 1;
                } else {
                    pixel = 0;
                }
                pixelList[j][i] = pixel;
                verticalMapping[i] += pixel;
            }
        }
        for (int i = minY; i < height; i++) {
            for (int j = minX; j < width; j++) {
                horizontalMapping[i] += pixelList[i][j];
            }
        }
        System.out.println("横向映射...");
        for (int i : horizontalMapping) {
            System.out.print(i + " ");
        }
        System.out.println();
        System.out.println("纵向映射...");
        for (int i : verticalMapping) {
            System.out.print(i + " ");
        }
        System.out.println();
    }

    /**
     * 生成图片
     *
     * @param x      左上角x轴
     * @param y      左上角y轴
     * @param height 向下偏移
     * @param wight  向左偏移
     * @throws Exception
     */
    public String paintImg(int x, int y, int height, int wight) throws Exception {

        // 百度识别图片必须大于宽或高15px
        int fetchUpHeightImg = 0;
        int fetchUpWighttImg = 0;
        if (height < 15) {
            fetchUpHeightImg = 15 - height;
        }
        if (wight < 15) {
            fetchUpWighttImg = 15 - wight;
        }
        BufferedImage bufferedImage = new BufferedImage(wight + fetchUpWighttImg, height + fetchUpHeightImg
                , BufferedImage.TYPE_INT_RGB);
        Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics();
        g2D.fillRect(0, 0, wight + fetchUpWighttImg, height + fetchUpHeightImg);
        g2D.drawImage(bufferedImage, 0, 0, Color.lightGray, null);

        for (int j = y; j < y + height; j++) {
            for (int i = x; i < x + wight; i++) {
                int pixel = pixelListTarget[j][i];
                bufferedImage.setRGB(i - x, j - y, pixel);
            }
        }
        String file = "D:\\pdf\\jpg\\result\\" + UUID.randomUUID() + ".jpg";
        File imgFile = new File(file);
        ImageIO.write(bufferedImage, "jpg", imgFile);
        return file;
    }

    /**
     * 获得行块
     *
     * @param horizontalThreshold
     * @param horizontalMaxThreshold
     * @return
     */
    public List<Rectangle> getLineBlock(int horizontalThreshold, int horizontalMaxThreshold) {

        Rectangle rectangle = new Rectangle();
        int type = 1;
        rectangle.setX(0);
        rectangle.setWidth(verticalMapping.length);

        /**
         * 分配y坐标
         */
        List<Rectangle> returnRectList = new ArrayList<>();   // 用于返回结果矩阵的列表
        type = 1;
        Rectangle copyRect = new Rectangle();
        for (int j = 0; j < horizontalMapping.length; j++) {
            if ((horizontalMapping[j] > horizontalThreshold || horizontalMapping[j] >= horizontalMaxThreshold) && type == 1) {
                copyRect = new Rectangle(rectangle.getX(), rectangle.getWidth());
                copyRect.setY(j);
                type = 2;
            }
            if (((horizontalMapping[j] <= horizontalThreshold || horizontalMapping[j] >= horizontalMaxThreshold) ||
                    (j == (horizontalMapping.length - 1) && horizontalMapping[j] > horizontalThreshold)) && type == 2) {
                type = 1;
                int height = j - copyRect.getY();
                if (height <= 6) {
                    continue;
                }
                copyRect.setHeight(height);
                returnRectList.add(copyRect);
            }
        }
        return returnRectList;
    }

    public List<Rectangle> cutTableBlock(int horizontalThreshold, int horizontalMaxThreshold,
                                         int verticalThreshold, int verticalMaxThreshold) {
        Rectangle rectangle = new Rectangle();
        int type = 1;
        List<Rectangle> rectangleList = new ArrayList<>();
        for (int i = 0; i < verticalMapping.length; i++) {
            if ((verticalMapping[i] > verticalThreshold || verticalMapping[i] >= verticalMaxThreshold) && type == 1) {
                rectangle = new Rectangle();
                rectangle.setX(i);
                type = 2;
            }
            if (((verticalMapping[i] <= verticalThreshold || verticalMapping[i] >= verticalMaxThreshold) ||
                    (i == (verticalMapping.length - 1) && verticalMapping[i] > verticalThreshold)) && type == 2) {
                type = 1;
                int width = i - rectangle.getX();
                if (width <= 10) {
                    continue;
                }
                rectangle.setWidth(width);
                rectangleList.add(rectangle);
            }
        }

        /**
         * 按字体阀值重新分割
         */
//        for (int i = 0; i < rectangleList.size() - 1; i++) {
//            int spacingFont = rectangleList.get(i).getFontSpacing(rectangleList.get(i + 1));
//            if (spacingFont <= 4) { // 4 字体间隔阀值
//                int newWidth = rectangleList.get(i).getWidth() + rectangleList.get(i + 1).getWidth() + spacingFont;
//                rectangleList.get(i).setWidth(newWidth);
//                rectangleList.remove(i + 1);
//            }
//        }

        List<Rectangle> returnRectList = new ArrayList<>();
        Rectangle copyRect = new Rectangle();
        for (int i = 0; i < rectangleList.size(); i++) {
            type = 1;
            rectangle = rectangleList.get(i);
            if (rectangle.getWidth() != 0) {

                for (int j = 0, k = 0; j < horizontalMapping.length; j++) {
                    if ((horizontalMapping[j] > horizontalThreshold || horizontalMapping[j] >= horizontalMaxThreshold) && type == 1) {
                        copyRect = new Rectangle(rectangle.getX(), rectangle.getWidth());
                        copyRect.setY(j);
                        type = 2;
                        continue;
                    }
                    if (((horizontalMapping[j] <= horizontalThreshold || horizontalMapping[j] >= horizontalMaxThreshold) ||
                            (j == (horizontalMapping.length - 1) && horizontalMapping[j] > horizontalThreshold)) && type == 2) {
                        int height = j - copyRect.getY();
                        if (height <= 10) {
                            continue;
                        }
                        copyRect.setHeight(height);
                        returnRectList.add(copyRect);
                        type = 1;
                    }
                }
            }
        }
        return returnRectList;
    }

    /**
     * 去除字体左右留白
     * @param verticalThreshold
     * @return
     */
    public Rectangle cutBlack(int verticalThreshold) {

        Rectangle rectangle = new Rectangle();
        int endPoint = -1;
        for (int i = 1; i < verticalMapping.length-1; i++) {
            if(rectangle.getX()==0 && verticalMapping[i] > verticalThreshold && verticalMapping[i-1] <= verticalThreshold){
                rectangle.setX(i);
            }
            if(endPoint == -1 && verticalMapping[verticalMapping.length-1-i] > 0 &&
                    verticalMapping[verticalMapping.length-i] <= verticalThreshold){
                endPoint = verticalMapping.length-1-i;
            }
        }
        rectangle.setWidth(endPoint-rectangle.getX());
        rectangle.setY(0);
        rectangle.setHeight(horizontalMapping.length);
        return rectangle;
    }


    public static void main(String[] args) throws Exception {

        String fileName = "D:\\pdf\\jpg\\result\\27eb050b-01ab-4f4a-b9a6-d2fe84079b90.jpg";
        ImgTest imgTest = new ImgTest();
        imgTest.readPixel(fileName);
        Rectangle rectangle = imgTest.cutBlack(0);
        System.out.println(rectangle);
        imgTest.paintImg(rectangle.getX(),rectangle.getY(),rectangle.getHeight(),rectangle.getWidth());
    }
}

