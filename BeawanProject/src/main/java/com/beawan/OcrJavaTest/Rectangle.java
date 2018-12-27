package com.beawan.OcrJavaTest;

public class Rectangle {

    private int x;
    private int y;
    private int width;
    private int height;

    public Rectangle() {

    }

    public Rectangle(int x, int width) {
        this.x = x;
        this.width = width;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "Rectangle{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                '}';
    }

    public void releaseYandHeight() {
        this.y = 0;
        this.height = 0;
    }

    public int getFontSpacing(Rectangle rectangle){
        return rectangle.getX() - (this.x + this.width);
    }
}
