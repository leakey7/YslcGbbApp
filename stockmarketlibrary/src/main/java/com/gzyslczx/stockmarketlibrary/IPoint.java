package com.gzyslczx.stockmarketlibrary;

import androidx.annotation.NonNull;

public class IPoint<T>  {

    private float XPoint, YPoint;
    private float XValue;
    private float YValue;
    private T ExtraData;

    public IPoint(@NonNull T extraData, @NonNull float yValue) {
        ExtraData = extraData;
        YValue = yValue;
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

    public float getXValue() {
        return XValue;
    }

    public void setXValue(float XValue) {
        this.XValue = XValue;
    }

    public float getYValue() {
        return YValue;
    }

    public void setYValue(float YValue) {
        this.YValue = YValue;
    }

    public T getExtraData() {
        return ExtraData;
    }

}
