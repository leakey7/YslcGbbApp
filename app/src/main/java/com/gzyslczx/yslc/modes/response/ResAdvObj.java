package com.gzyslczx.yslc.modes.response;

public class ResAdvObj {

    private String AdId, Title, Img, AppUrl, PicUrl, HeadImg, Remark, AddTime;
    private boolean NeedPara, Active;
    private int Sort, Type;

    public String getAdId() {
        return AdId;
    }

    public String getTitle() {
        return Title;
    }

    public String getImg() {
        return Img;
    }

    public String getAppUrl() {
        return AppUrl;
    }

    public String getPicUrl() {
        return PicUrl;
    }

    public String getHeadImg() {
        return HeadImg;
    }

    public String getRemark() {
        return Remark;
    }

    public String getAddTime() {
        return AddTime;
    }

    public boolean isNeedPara() {
        return NeedPara;
    }

    public boolean isActive() {
        return Active;
    }

    public int getSort() {
        return Sort;
    }

    public int getType() {
        return Type;
    }
}
