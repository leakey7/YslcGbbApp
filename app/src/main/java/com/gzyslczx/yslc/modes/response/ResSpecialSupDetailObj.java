package com.gzyslczx.yslc.modes.response;

public class ResSpecialSupDetailObj {

    private String Name, Desc, Img, SId, StockCode, StockName, Remarks, Fxsm, AddDate, ColumnId;
    private boolean IsFocus, IsSelected, IsLike;
    private int FocusNum, LikeNum;

    public String getColumnId() {
        return ColumnId;
    }

    public String getName() {
        return Name;
    }

    public String getDesc() {
        return Desc;
    }

    public String getImg() {
        return Img;
    }

    public String getSId() {
        return SId;
    }

    public String getStockCode() {
        return StockCode;
    }

    public String getStockName() {
        return StockName;
    }

    public String getRemarks() {
        return Remarks;
    }

    public String getFxsm() {
        return Fxsm;
    }

    public String getAddDate() {
        return AddDate;
    }

    public boolean isFocus() {
        return IsFocus;
    }

    public boolean isSelected() {
        return IsSelected;
    }

    public void setSelected(boolean selected) {
        IsSelected = selected;
    }

    public boolean isLike() {
        return IsLike;
    }

    public int getFocusNum() {
        return FocusNum;
    }

    public int getLikeNum() {
        return LikeNum;
    }
}
