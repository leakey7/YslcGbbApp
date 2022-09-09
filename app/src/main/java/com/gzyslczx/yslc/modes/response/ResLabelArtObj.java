package com.gzyslczx.yslc.modes.response;

public class ResLabelArtObj {

    private String ColumnName, ColumnImg;
    private boolean IsFocus;
    private int ArtNum;
    private ResLabelArtDetail NewsDetail;

    public String getColumnName() {
        return ColumnName;
    }

    public String getColumnImg() {
        return ColumnImg;
    }

    public boolean isFocus() {
        return IsFocus;
    }

    public int getArtNum() {
        return ArtNum;
    }

    public ResLabelArtDetail getNewsDetail() {
        return NewsDetail;
    }
}
