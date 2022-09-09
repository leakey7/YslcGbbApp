package com.gzyslczx.yslc.modes.response;

import java.util.List;

public class ResTSelfInfo {

    private String Id, Name, Img, Advantage, Number, Introduce;
    private boolean IsFocus;
    private List<ResTSelfList> ArtList;

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

    public String getIntroduce() {
        return Introduce;
    }

    public boolean isFocus() {
        return IsFocus;
    }

    public List<ResTSelfList> getArtList() {
        return ArtList;
    }
}
