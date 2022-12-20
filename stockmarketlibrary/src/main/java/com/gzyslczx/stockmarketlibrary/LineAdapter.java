package com.gzyslczx.stockmarketlibrary;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class LineAdapter {

    private int lineColor;
    private List<IPoint<?>> pointList;
    private float MaxValue=Float.MIN_VALUE, MinValue=Float.MAX_VALUE;

    public LineAdapter(int lineColor) {
        this.lineColor = lineColor;
        pointList = new ArrayList<>();
    }

    public LineAdapter(int lineColor, @NonNull List<IPoint<?>> pointList) {
        this.lineColor = lineColor;
        this.pointList = pointList;
        CountMaxAndMin(this.pointList);
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public List<IPoint<?>> getPointList() {
        return pointList;
    }

    public void setPointList(List<IPoint<?>> pointList) {
        this.pointList.clear();
        this.pointList.addAll(pointList);
        CountMaxAndMin(this.pointList);
    }

    public void addPointList(List<IPoint<?>> pointList){
        this.pointList.addAll(pointList);
        CountMaxAndMin(this.pointList);
    }

    public void addPoint(IPoint<?> point) {
        this.pointList.add(point);
        CountMaxAndMin(this.pointList);
    }

    private void CountMaxAndMin(List<IPoint<?>> points){
        for (IPoint iPoint :  points){
            MaxValue = Math.max(MaxValue, iPoint.getYValue());
            MinValue = Math.min(MinValue, iPoint.getYValue());
        }
    }

    public float getMaxValue() {
        return MaxValue;
    }

    public float getMinValue() {
        return MinValue;
    }
}
