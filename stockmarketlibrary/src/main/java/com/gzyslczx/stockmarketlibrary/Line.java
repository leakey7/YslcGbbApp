package com.gzyslczx.stockmarketlibrary;

public class Line <T>{

    private float XPoint;
    private float YPoint;
    private T Data;
    private int Color;

    public Line(T data, int color) {
        Data = data;
        Color = color;
    }

    public float getXPoint() {
        return XPoint;
    }

    public void setXPoint(float XPoint) {
        this.XPoint = XPoint;
    }

    public float getYPoint() {
        return YPoint;
    }

    public void setYPoint(float YPoint) {
        this.YPoint = YPoint;
    }

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }

    public int getColor() {
        return Color;
    }

    public void setColor(int color) {
        Color = color;
    }
}
