package com.gzyslczx.stockmarketlibrary;

import androidx.annotation.NonNull;

public class IPoint<T> extends IPoint<Object> {

    private float XValue;
    private float YValue;
    private T ExtraData;


    public IPoint(@NonNull T extraData) {
        ExtraData = extraData;
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
