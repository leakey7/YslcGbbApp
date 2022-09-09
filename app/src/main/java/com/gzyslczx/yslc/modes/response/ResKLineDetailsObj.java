package com.gzyslczx.yslc.modes.response;

import java.util.List;

public class ResKLineDetailsObj {

    private ResKLDetailInfo ArtDetail, VideoDetail;
    private List<ResKLDetailRec> ArtList, VideoList;

    public ResKLDetailInfo getArtDetail() {
        return ArtDetail;
    }

    public ResKLDetailInfo getVideoDetail() {
        return VideoDetail;
    }

    public List<ResKLDetailRec> getArtList() {
        return ArtList;
    }

    public List<ResKLDetailRec> getVideoList() {
        return VideoList;
    }
}
