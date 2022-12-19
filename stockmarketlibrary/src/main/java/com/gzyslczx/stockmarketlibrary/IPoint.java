package com.gzyslczx.stockmarketlibrary;

public class IPoint<T> {

    private float XValue;
    private float YValue;
    private T ExtraData;


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

    public void setExtraData(T extraData) {
        ExtraData = extraData;
    }
}
