package com.gzyslczx.yslc.modes.response;

public class ResDefaultOptionObj {

    private String StockName, StockCode, Ssswhy_Sjhy;
    private boolean isSelect=true;

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getStockName() {
        return StockName;
    }

    public String getStockCode() {
        return StockCode;
    }

    public String getSsswhy_Sjhy() {
        return Ssswhy_Sjhy;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void changeSelect() {
        isSelect = !isSelect;
    }

}
