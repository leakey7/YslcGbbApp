package com.gzyslczx.yslc.modes.response;

public class ResDGSCard {
    private String Name, Img, SId, NsId, StockCode, StockName, Remarks, ColumnId, AddDate;
    private boolean IsFocus, IsSelected, IsLike;
    private int LikeNum;

    public String getAddDate() {
        return AddDate;
    }

    public boolean isFocus() {
        return IsFocus;
    }

    public boolean isSelected() {
        return IsSelected;
    }

    public boolean isLike() {
        return IsLike;
    }

    public int getLikeNum() {
        return LikeNum;
    }

    public String getName() {
        return Name;
    }

    public String getImg() {
        return Img;
    }

    public String getSId() {
        return SId;
    }

    public String getNsId() {
        return NsId;
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

    public String getColumnId() {
        return ColumnId;
    }
}
