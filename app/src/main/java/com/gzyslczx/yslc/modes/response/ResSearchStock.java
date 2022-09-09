package com.gzyslczx.yslc.modes.response;

public class ResSearchStock {

    private String StockCode, StockName;
    private boolean IsFocus;

    public String getStockCode() {
        return StockCode;
    }

    public String getStockName() {
        return StockName;
    }

    public boolean isFocus() {
        return IsFocus;
    }

    public void setFocus(boolean focus) {
        IsFocus = focus;
    }
}
