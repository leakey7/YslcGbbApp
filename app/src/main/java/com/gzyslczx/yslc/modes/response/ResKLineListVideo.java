package com.gzyslczx.yslc.modes.response;

public class ResKLineListVideo {

    private int Id, CategoryId, PlayTimes, Likes, SortInfo, HomeTj, Audit;
    private String Title, Describe, VideoUrl, PicUrl, Author, CategoryName, AddTime, Fxsm;
    private boolean IsLikes, IsLearn;

    public void setLikes(int likes) {
        Likes = likes;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public int getPlayTimes() {
        return PlayTimes;
    }

    public int getLikes() {
        return Likes;
    }

    public int getSortInfo() {
        return SortInfo;
    }

    public int getHomeTj() {
        return HomeTj;
    }

    public int getAudit() {
        return Audit;
    }

    public int getId() {
        return Id;
    }

    public String getTitle() {
        return Title;
    }

    public String getDescribe() {
        return Describe;
    }

    public String getVideoUrl() {
        return VideoUrl;
    }

    public String getPicUrl() {
        return PicUrl;
    }

    public String getAuthor() {
        return Author;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public String getAddTime() {
        return AddTime;
    }

    public String getFxsm() {
        return Fxsm;
    }

    public boolean isLikes() {
        return IsLikes;
    }

    public boolean isLearn() {
        return IsLearn;
    }

    public void setLearn(boolean learn) {
        IsLearn = learn;
    }
}
