//package com.beawan;
//
//import org.apache.pdfbox.cos.COSName;
//import org.apache.pdfbox.cos.COSStream;
//import org.apache.pdfbox.pdfviewer.PageDrawer;
//import org.apache.pdfbox.pdmodel.PDPage;
//import org.apache.pdfbox.pdmodel.PDResources;
//import org.apache.pdfbox.pdmodel.common.PDRectangle;
//import org.apache.pdfbox.util.Matrix;
//import org.apache.pdfbox.util.TextPosition;
//
//import java.awt.*;
//import java.awt.geom.AffineTransform;
//import java.awt.geom.GeneralPath;
//import java.awt.geom.PathIterator;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class PrintPaths extends PageDrawer {
//
//    static List<Coordinate> COORDINATE_LIST = new ArrayList<>();
//
//    public PrintPaths() throws IOException {
//        super();
//    }
//
//    // ignore text
//    @Override
//    protected void processTextPosition(TextPosition text) {
//    }
//
//    // ignore bitmaps
//    @Override
//    public void drawImage(Image awtImage, AffineTransform at) {
//    }
//
//    // ignore shadings
//    @Override
//    public void shFill(COSName shadingName) throws IOException {
//    }
//
//    @Override
//    public void processStream(PDPage aPage, PDResources resources, COSStream cosStream) throws IOException {
//        PDRectangle cropBox = aPage.findCropBox();
//        this.pageSize = cropBox.createDimension();
//        super.processStream(aPage, resources, cosStream);
//    }
//
//    @Override
//    public void fillPath(int windingRule) throws IOException {
//        printPath();
//        getLinePath().reset();
//
//    }
//
//    @Override
//    public void strokePath() throws IOException {
//        printPath();
//        getLinePath().reset();
//    }
//
//    void printPath() {
//        GeneralPath path = getLinePath();
//        PathIterator pathIterator = path.getPathIterator(null);
//        double x = 0, y = 0;
//        double coords[] = new double[6];
//        while (!pathIterator.isDone()) {
//            switch (pathIterator.currentSegment(coords)) {
//                case PathIterator.SEG_MOVETO:
////                    System.out.println("move to " + (int) coords[0] + ", " + (int) coords[1]);
//                    x = coords[0];
//                    y = coords[1];
//                    Coordinate startCoordinate = new Coordinate((int) x, (int) y);
//                    COORDINATE_LIST.add(startCoordinate);
//                    break;
//                case PathIterator.SEG_LINETO:
//                    double width = getEffectiveWidth(coords[0] - x, coords[1] - y);
//                    x = coords[0];
//                    y = coords[1];
////                    System.out.println("line to " + (int) coords[0] + ", " + (int) coords[1]);
//                    Coordinate lineCoordinate = new Coordinate((int) x, (int) y);
//                    COORDINATE_LIST.add(lineCoordinate);
//                    break;
//                case PathIterator.SEG_QUADTO:
////                    System.out.printf("Quad along (%s %s) and (%s %s)\n", coords[0], fixY(coords[1]), coords[2], fixY(coords[3]));
//                    x = coords[2];
//                    y = coords[3];
//                    break;
//                case PathIterator.SEG_CUBICTO:
//                    System.out.printf("Cubic along (%s %s), (%s %s), and (%s %s)\n", coords[0], fixY(coords[1]), coords[2], fixY(coords[3]), coords[4], fixY(coords[5]));
//                    x = coords[4];
//                    y = coords[5];
//                    break;
//                case PathIterator.SEG_CLOSE:
////                    System.out.println("Close path");
//            }
//
//            pathIterator.next();
//        }
//    }
//
//    double getEffectiveWidth(double dirX, double dirY) {
//        if (dirX == 0 && dirY == 0)
//            return 0;
//        Matrix ctm = getGraphicsState().getCurrentTransformationMatrix();
//        double widthX = dirY;
//        double widthY = -dirX;
//        double widthXTransformed = widthX * ctm.getValue(0, 0) + widthY * ctm.getValue(1, 0);
//        double widthYTransformed = widthX * ctm.getValue(0, 1) + widthY * ctm.getValue(1, 1);
//        double factor = Math.sqrt((widthXTransformed * widthXTransformed + widthYTransformed * widthYTransformed) / (widthX * widthX + widthY * widthY));
//        return getGraphicsState().getLineWidth() * factor;
//    }
//}