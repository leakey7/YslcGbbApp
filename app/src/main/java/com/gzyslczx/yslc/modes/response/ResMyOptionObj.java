package com.gzyslczx.yslc.modes.response;

import java.io.Serializable;

public class ResMyOptionObj implements Serializable, Comparable<ResMyOptionObj>{

    private String StockName, StockCode, Ssswhy_Sjhy, NothCgRatio, RzRation, Xsjll;
    private boolean IsFinanc, IsTop;
    private int SortInfo;
    private boolean IsSelected=false;

    public String getStockName() {
        return StockName;
    }

    public void setStockName(String stockName) {
        StockName = stockName;
    }

    public String getStockCode() {
        return StockCode;
    }

    public void setStockCode(String stockCode) {
        StockCode = stockCode;
    }

    public String getSsswhy_Sjhy() {
        return Ssswhy_Sjhy;
    }

    public void setSsswhy_Sjhy(String ssswhy_Sjhy) {
        Ssswhy_Sjhy = ssswhy_Sjhy;
    }

    public String getNothCgRatio() {
        return NothCgRatio;
    }

    public void setNothCgRatio(String nothCgRatio) {
        NothCgRatio = nothCgRatio;
    }

    public String getRzRation() {
        return RzRation;
    }

    public void setRzRation(String rzRation) {
        RzRation = rzRation;
    }

    public String getXsjll() {
        return Xsjll;
    }

    public void setXsjll(String xsjll) {
        Xsjll = xsjll;
    }

    public boolean isFinanc() {
        return IsFinanc;
    }

    public void setFinanc(boolean financ) {
        IsFinanc = financ;
    }

    public boolean isTop() {
        return IsTop;
    }

    public void setTop(boolean top) {
        IsTop = top;
    }

    public int getSortInfo() {
        return SortInfo;
    }

    public void setSortInfo(int sortInfo) {
        SortInfo = sortInfo;
    }

    public boolean isSelected() {
        return IsSelected;
    }

    public void setSelected(boolean selected) {
        IsSelected = selected;
    }

    @Override
    public int compareTo(ResMyOptionObj resMyOptionObj) {
        if (this.isTop()){
            return -1;
        }else {
            if (this.SortInfo > resMyOptionObj.SortInfo){
                return -1;
            }else if (this.SortInfo < resMyOptionObj.SortInfo){
                return 1;
            }
        }
        return 0;
    }

    public boolean ChangeSelect(){
        IsSelected = !IsSelected;
        return IsSelected;
    }

}
