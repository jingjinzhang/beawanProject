//package com.beawan;
//
//import com.google.gson.Gson;
//import org.apache.log4j.Logger;
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.pdmodel.PDPage;
//import org.apache.pdfbox.pdmodel.common.PDStream;
//import org.apache.pdfbox.util.PDFTextStripper;
//import org.apache.pdfbox.util.PDFTextStripperByArea;
//
//import java.awt.*;
//import java.util.List;
//import java.util.*;
//
///**
// * @author jingjinzhang
// */
//public class Test {
//    private static Logger logger = Logger.getLogger(Test.class);
//    private static int FIRST_APPEAR = 0;
//    private static final String Annotations_Consolidated_Financial_Statements = "合并财务报表项目注释";
//    private static final String Accounts_Receivable_Debtor = "按欠款方归集的期末余额前五名的应收账款情况";
//    private static final String Organization_Name = "单位名称";
//
//    public static void main(String[] args) throws Exception {
//
//        String fileName = "D:\\pdf\\600571_2017_nB.pdf";
//        PDDocument document = null;
//
//        document = PDDocument.load(fileName);
//
//        List<?> allPages = document.getDocumentCatalog().getAllPages();
//        int pageNum = getKeyPosition(allPages.size(), null, document);
//        List<List<String>> rowList = new ArrayList<>();
//        while (true) {
//            rowList = parsingFile(fileName, pageNum, false);
//            if (rowList == null) {
//                break;
//            } else {
//                List<List<String>> tempLists = parsingFile(fileName, pageNum + 1,true);
//                for (List<String> tempList : tempLists) {
//                    rowList.add(tempList);
//                }
//                break;
//            }
//        }
//        List<Map<String, String>> mapList = new ArrayList<>();
//        Map<String, String> tempMap = new HashMap<>();
//        for (int i = 0; i < rowList.size(); i++) {
//            for (int j = 0; j < rowList.get(i).size(); j++) {
//                if (i == 0) {
//                } else {
//                    tempMap.put(rowList.get(0).get(j), rowList.get(i).get(j));
//                }
//            }
//            if (i != 0) {
//                mapList.add(tempMap);
//            }
//            tempMap = new HashMap<>();
//        }
//        Gson gson = new Gson();
//        String jsonStr = gson.toJson(mapList);
//        System.out.println(jsonStr);
//        System.out.println("[INFO]:解析完成!");
//
//    }
//
//    private static List<List<String>> parsingFile(String fileName, int pageNum,boolean hasNext) throws Exception {
//        System.out.println("[INFO]:正在读取第" + (pageNum + 1) + "页信息");
//        PDDocument document = null;
//        document = PDDocument.load(fileName);
//        List<?> allPages = document.getDocumentCatalog().getAllPages();
//        PDPage page = (PDPage) allPages.get(pageNum);
//        PrintPaths printPaths = new PrintPaths();
//        PDStream contents = page.getContents();
//        if (contents != null) {
//            PrintPaths.COORDINATE_LIST.clear();
//            printPaths.processStream(page, page.findResources(), page.getContents().getStream());
//        }
//        List<Coordinate> coordinateList = PrintPaths.COORDINATE_LIST;
//
//        // judge coordinate is useful
//        List<List<String>> rowList = judgeCoordinate(coordinateList, document, pageNum, hasNext);
//        return rowList;
//    }
//
//    /**
//     * 查找关键词位置
//     */
//    private static int getKeyPosition(int pageNums, String key, PDDocument document) throws Exception {
//
//        List<?> allPages = document.getDocumentCatalog().getAllPages();
//        PDFTextStripper stripper = new PDFTextStripper();
//        stripper.setSortByPosition(true);
//        String content;
//        int targetNum = 0;
//        for (int i = 0; i < pageNums; i++) {
//            stripper.setStartPage(i);
//            stripper.setEndPage(i);
//            content = stripper.getText(document);
//            if (content.contains(Accounts_Receivable_Debtor) && FIRST_APPEAR < 1) {
//                targetNum = i - 1;
//                FIRST_APPEAR++;
//                System.out.println("[INFO]:关键字在" + i + "页");
//            }
//        }
//        return targetNum;
//    }
//
//    /**
//     * 去重排序
//     *
//     * @param coordinateList
//     * @param document
//     * @return
//     */
//    private static List<List<String>> judgeCoordinate(List<Coordinate> coordinateList, PDDocument document,
//                                                      int pageNum, boolean hasNext) {
//
////        System.out.println("去重前....");
////        for (Coordinate c : coordinateList) {
////            System.out.println(c);
////        }
//
//        // 去重
//        for (int i = 0; i < coordinateList.size() - 1; i++) {
//            for (int j = coordinateList.size() - 1; j > i; j--) {
//                if (coordinateList.get(j).equals(coordinateList.get(i))) {
//                    coordinateList.remove(j);
//                }
//            }
//        }
//
////        System.out.println("去重后...");
////        for (Coordinate c : coordinateList) {
////            System.out.println(c.toString());
////        }
//        // 区分多个形状的表格
//        int[] target = new int[coordinateList.size()];
//        List<Table> tableList = new ArrayList<>();
//        for (int i = 0; i < coordinateList.size(); i++) {
//            Coordinate coordinate = coordinateList.get(i);
//            for (int j = 0; j < coordinateList.size(); j++) {
//                if (Math.abs(coordinate.getY() - coordinateList.get(j).getY()) <= 2) {
//                    target[i]++;
//                }
//            }
//        }
//
//        // 除去横线
////        for (int k = 0; k < target.length; k++) {
////            if (target[k] <= 1) {
////                for (int j = 0; j < target.length - 1; j++) {
////                    target[k] = target[k + 1];
////                }
////            }
////        }
////        System.out.println();
////        for(int i = 0 ; i < target.length; i++){
////            System.out.print(target[i] +"-");
////        }
//        // 组成表格
//        for (int i = 0; i < coordinateList.size(); i++) {
//            if (i >= 1 && target[i] != target[i - 1] && target[i] > 1) {
//                tableList.add(new Table(i, target[i]));
//            }
//            if (i == 0 && target[i] > 1) {
//                tableList.add(new Table(i, target[i]));
//            }
//        }
//        // 计算每个表格的横纵值
//        int flag = 0;
//        if (tableList.size() == 1) {
//            tableList.get(tableList.size() - 1).total = coordinateList.size();
//
//        } else if (tableList.size() >= 1) {
//            for (int k = 0; k < tableList.size(); k++) {
//                if (k == 0) {
//                    tableList.get(k).total = tableList.get(k + 1).index;
//                } else if (k == tableList.size() - 1) {
//                    tableList.get(k).total = coordinateList.size() - tableList.get(k).index;
//                } else {
//                    tableList.get(k).total = tableList.get(k + 1).index - tableList.get(k).index;
//                }
//            }
//        }
//
//        // 判断是否有不同格式的表格
//        Gson gson = new Gson();
//        List<List<String>> rowList = new ArrayList<>();
//        String mapListString;
//        if (tableList.size() >= 1) {
//            for (int k = 0; k < tableList.size(); k++) {
//                rowList = printTable(tableList.get(k), tableList, coordinateList, document, pageNum);
//                mapListString = gson.toJson(rowList);
//                if(tableList.get(k).yRange == 2){
//                    continue;
//                }
//                if (!mapListString.contains(Organization_Name) && !hasNext) {
//                    rowList = new ArrayList<>();
//                    continue;
//                } else {
//                    return rowList;
//                }
//
//            }
//        } else {
//            rowList = printTable(tableList.get(0), tableList, coordinateList, document, pageNum);
//            mapListString = gson.toJson(rowList);
//        }
//        if (rowList.size() <= 7) {
//            return rowList;
//        }
//        return null;
//    }
//
//
//    private static String readRectangleInfo(int x, int y, int width, int height, PDDocument document
//            , int pageNum) throws Exception {
//
//        PDFTextStripperByArea stripper = new PDFTextStripperByArea();
//        stripper.setSortByPosition(true);
//        Rectangle rect = new Rectangle(x, y, width, height);
//        stripper.addRegion("rect", rect);
//        List allPages = document.getDocumentCatalog().getAllPages();
//        PDPage firstPage = (PDPage) allPages.get(pageNum);
//        stripper.extractRegions(firstPage);
//        return stripper.getTextForRegion("rect");
//    }
//
//    private static List<List<String>> printTable(Table table, List<Table> tableList, List<Coordinate> coordinateList, PDDocument document
//            , int pageNum) {
//        int index = table.index;
//        int range = table.yRange;
//        int total = table.total;
//        int col = range;
//        int row = total / range;
//        Coordinate[][] coordinates = new Coordinate[row][col];
//
//        Collections.sort(coordinateList, new Comparator<Coordinate>() {
//            @Override
//            public int compare(Coordinate o1, Coordinate o2) {
//                if (Math.abs(o1.getY() - o2.getY()) <= 2) {
//                    return o1.getX() - o2.getX();
//                }
//                return 0;
//            }
//        });
//        // 填充二维数组
//        int a = index;
//        for (int i = 0; i < row; i++) {
//            for (int j = 0; j < col; j++) {
//                coordinates[i][j] = coordinateList.get(a);
//                a++;
//            }
//        }
//        // 打印二维数组
////        for (int i = 0; i < row; i++) {
////            for (int j = 0; j < col; j++) {
////                System.out.print("(" + coordinates[i][j].getX() + "," + coordinates[i][j].getY() + ")" + " ");
////            }
////            System.out.println();
////        }
//
//        // 解析表格
//        List<String> title = new ArrayList<>();
//        List<List<String>> rowList = new ArrayList<>();
//        for (int i = 0; i < coordinates.length; i++) {
//            List<String> blockString = new ArrayList<>();
//            for (int j = 0; j < coordinates[0].length; j++) {
//
//                int newI = i + 1;
//                int newJ = j + 1;
//                if (newI < coordinates.length && newJ < coordinates[0].length) {
//                    Coordinate startCoordinate = coordinates[i][j];
//                    Coordinate endCoordinate = coordinates[newI][newJ];
//                    try {
//                        String info = readRectangleInfo(startCoordinate.getX(), startCoordinate.getY(),
//                                endCoordinate.getX() - startCoordinate.getX(),
//                                endCoordinate.getY() - startCoordinate.getY(), document, pageNum);
//                        info = info.replaceAll("\r|\n", "");
//                        info = info.replaceAll(" ", "");
//
//                        blockString.add(info);
////                        System.out.print(info.trim() + " ");
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            rowList.add(blockString);
//        }
//        return rowList;
//    }
//
//}
//
//class Table {
//    int index; // 起始位置
//    int yRange; // 列数
//    int total; // 总个数
//
//    public Table(int index, int yRange) {
//        this.index = index;
//        this.yRange = yRange;
//    }
//
//    @Override
//    public String toString() {
//        return "Table{" +
//                "index=" + index +
//                ", yRange=" + yRange +
//                ", total=" + total +
//                '}';
//    }
//}
