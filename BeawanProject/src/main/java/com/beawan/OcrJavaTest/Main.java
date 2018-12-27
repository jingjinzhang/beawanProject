package com.beawan.OcrJavaTest;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args){

        boolean hasTable = false;
        String startKeyword = "按欠款方归集的期末余额前五名的应收账款";
        String endKeyword = "因金融资产转移而终止确认的应收账款";
        try {
            ImgTest test = new ImgTest();
//            List<String> fileNames = PdfToPhoto.change("D:\\pdf\\600571_2017_nB.pdf", "D:\\pdf\\jpg\\pdfresult", 300);
            String fileName = "D:\\pdf\\jpg\\pdfresult\\600006_2017_n\\600006_2017_n_105.jpg";

            test.readPixel(fileName);
            System.out.println(Util.findMostNumber(test.horizontalMapping) + "," +
                    Util.findMaxValue(test.horizontalMapping));
            List<Rectangle> rectangleList = test.getLineBlock(Util.findMostNumber(test.horizontalMapping), Integer.MAX_VALUE);
            for (Rectangle rectangle : rectangleList) {
                System.out.println(rectangle);
                String newImgFileName = test.paintImg(rectangle.getX(), rectangle.getY(),
                        rectangle.getHeight(), rectangle.getWidth());
                File file = new File(newImgFileName);
                String result = Check.checkFile(newImgFileName);
                if (Util.compareKeyword(result,startKeyword)) {
                    hasTable = true;
                    file.delete();
                    continue;
                }
                if(Util.compareKeyword(result,endKeyword)){
                    hasTable = false;
                    file.delete();
                    continue;
                }
                if(result.contains("适用")){
                    file.delete();
                    continue;
                }
                if (!hasTable) {
                    file.delete();
                    continue;
                }
                // 解析下一级图片
//                ImgTest innerTable = new ImgTest();
//                innerTable.readPixel(newImgFileName);
//                List<Rectangle> rectangles = innerTable.cutTableBlock(
//                        Util.findMostNumber(innerTable.horizontalMapping), Util.findMaxValue(innerTable.horizontalMapping) - 5,
//                        Util.findMostNumber(innerTable.verticalMapping), Util.findMaxValue(innerTable.verticalMapping) - 5);
//
//                List<String> bodyList = new ArrayList<>();
//                for (int i = 0; i < rectangles.size(); i++) {
//                    Rectangle r = rectangles.get(i);
//                    if (r == null) {
//                        continue;
//                    }
//                    String newImgFileName2 = innerTable.paintImg(r.getX(), r.getY(), r.getHeight(), r.getWidth());
//
//                    // 去除留白
//                    ImgTest cutLineBlock = new ImgTest();
//                    cutLineBlock.readPixel(newImgFileName2);
//                    Rectangle rectangle1 = cutLineBlock.cutBlack(0);
//                    String newImgFileName3 = cutLineBlock.paintImg(rectangle1.getX(),rectangle1.getY(),rectangle1.getHeight(),rectangle1.getWidth());
//
//                    String orcResult = Check.checkFile(newImgFileName3);
//                    if("".equals(orcResult)){
//                        continue;
//                    }
//                    bodyList.add(orcResult);
//                }
//
//                Gson gson = new Gson();
//                System.out.println(gson.toJson(bodyList, List.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
