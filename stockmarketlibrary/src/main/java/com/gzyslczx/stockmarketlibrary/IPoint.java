package com.gzyslczx.stockmarketlibrary;

import androidx.annotation.NonNull;

public class IPoint<T>  {

    private float XValue;
    private float YValue;
    private T ExtraData;


    public IPoint(@NonNull T extraData, @NonNull float yValue) {
        ExtraData = extraData;
        YValue = yValue;
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
