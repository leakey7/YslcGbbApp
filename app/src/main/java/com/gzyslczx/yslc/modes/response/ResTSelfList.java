package com.gzyslczx.yslc.modes.response;

public class ResTSelfList {

    private String NewsId, Title, Desc, AddTime, Img, FileUrl;
    private int ContentType, VideoType, Reading, Praise;
    private String[] ArrImg;
    private boolean IsLike;

    public String getNewsId() {
        return NewsId;
    }

    public String getTitle() {
        return Title;
    }

    public String getDesc() {
        return Desc;
    }

    public String getAddTime() {
        return AddTime;
    }

    public String getImg() {
        return Img;
    }

    public String getFileUrl() {
        return FileUrl;
    }

    public int getContentType() {
        return ContentType;
    }

    public int getVideoType() {
        return VideoType;
    }

    public int getReading() {
        return Reading;
    }

    public int getPraise() {
        return Praise;
    }

    public String[] getArrImg() {
        return ArrImg;
    }

    public boolean isLike() {
        return IsLike;
    }

    public void setPraise(int praise) {
        Praise = praise;
    }

    public void setLike(boolean like) {
        IsLike = like;
    }
}
