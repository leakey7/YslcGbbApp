package com.gzyslczx.yslc.modes.response;

import java.util.List;

public class ResKLineListInfo {

    private int LearnNum, ArtNum, VideoNum;
    private String CateName, Theme, Desc;
    private boolean JoinLearn;
    private List<ResKLineListArt> ArtList;
    private List<ResKLineListVideo> VideoList;

    public String getCateName() {
        return CateName;
    }

    public int getLearnNum() {
        return LearnNum;
    }

    public int getArtNum() {
        return ArtNum;
    }

    public String getTheme() {
        return Theme;
    }

    public String getDesc() {
        return Desc;
    }

    public boolean isJoinLearn() {
        return JoinLearn;
    }

    public List<ResKLineListArt> getArtList() {
        return ArtList;
    }

    public int getVideoNum() {
        return VideoNum;
    }

    public List<ResKLineListVideo> getVideoList() {
        return VideoList;
    }
}
