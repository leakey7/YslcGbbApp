package com.gzyslczx.yslc.modes.response;

public class ResTeacherAllObj {
    private String Id, Name, Img, Advantage, Number, NewsId, Title, Desc, ArtImg;
    private int SortInfo, ContentType, VideoType, LikeNum;
    private boolean IsFocus, IsLike;
    private String[] ArrImg;

    public void setLikeNum(int likeNum) {
        LikeNum = likeNum;
    }

    public void setFocus(boolean focus) {
        IsFocus = focus;
    }

    public void setLike(boolean like) {
        IsLike = like;
    }

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

    public String getNewsId() {
        return NewsId;
    }

    public String getTitle() {
        return Title;
    }

    public String getDesc() {
        return Desc;
    }

    public String getArtImg() {
        return ArtImg;
    }

    public int getSortInfo() {
        return SortInfo;
    }

    public int getContentType() {
        return ContentType;
    }

    public int getVideoType() {
        return VideoType;
    }

    public int getLikeNum() {
        return LikeNum;
    }

    public boolean isFocus() {
        return IsFocus;
    }

    public boolean isLike() {
        return IsLike;
    }

    public String[] getArrImg() {
        return ArrImg;
    }
}
