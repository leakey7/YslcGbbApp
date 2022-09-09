package com.gzyslczx.yslc.modes.response;

import java.util.List;

public class ResLabelSelfInfo {

    private int FocusNum, LikeNum, SortInfo;
    private String Id, Name, Desc, Img, AdImg, Remark;
    private boolean Active, IsFocus;
    private List<ResMainRecoInfo> nList;

    public int getFocusNum() {
        return FocusNum;
    }

    public int getLikeNum() {
        return LikeNum;
    }

    public int getSortInfo() {
        return SortInfo;
    }

    public String getId() {
        return Id;
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

    public String getAdImg() {
        return AdImg;
    }

    public String getRemark() {
        return Remark;
    }

    public boolean isActive() {
        return Active;
    }

    public boolean isFocus() {
        return IsFocus;
    }

    public List<ResMainRecoInfo> getnList() {
        return nList;
    }
}
