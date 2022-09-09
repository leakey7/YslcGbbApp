package com.gzyslczx.yslc.modes.response;

import java.util.List;

public class ResTSelfLivingListInfo {

    private String Id, Name, Img, Advantage, Number;
    private int SortInfo;
    private boolean IsFocus;
    private List<ResTSelfLivingListVideo> VideoList;

    public String getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public String getImg() {
        return Img;
    }

    public String getAdvantage() {
        return Advantage;
    }

    public String getNumber() {
        return Number;
    }

    public int getSortInfo() {
        return SortInfo;
    }

    public boolean isFocus() {
        return IsFocus;
    }

    public List<ResTSelfLivingListVideo> getVideoList() {
        return VideoList;
    }
}
