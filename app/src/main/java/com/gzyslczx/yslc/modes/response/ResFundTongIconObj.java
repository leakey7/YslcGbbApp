package com.gzyslczx.yslc.modes.response;

import java.util.List;

public class ResFundTongIconObj {

    private int AdId;
    private String Title, Img, Remark;
    private int Sort;
    private List<ResFundTongIconTab> TList;

    public int getAdId() {
        return AdId;
    }

    public String getTitle() {
        return Title;
    }

    public String getImg() {
        return Img;
    }

    public String getRemark() {
        return Remark;
    }

    public int getSort() {
        return Sort;
    }

    public List<ResFundTongIconTab> getTList() {
        return TList;
    }
}
