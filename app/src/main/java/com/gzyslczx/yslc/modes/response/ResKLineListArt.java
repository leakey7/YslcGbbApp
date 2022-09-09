package com.gzyslczx.yslc.modes.response;

public class ResKLineListArt {

    private int Id, CategoryId, SortInfo, HomeTj, Audit, Likes, ReadTimes;
    private String Title, Describe, PicUrl, Author, AddTime, CategoryName, Fxsm;
    private boolean IsLikes, IsLearn;

    public void setLikes(int likes) {
        Likes = likes;
    }

    public int getId() {
        return Id;
    }

    public int getCategoryId() {
        return CategoryId;
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

    public String getTitle() {
        return Title;
    }

    public String getDescribe() {
        return Describe;
    }

    public String getPicUrl() {
        return PicUrl;
    }

    public String getAuthor() {
        return Author;
    }

    public int getReadTimes() {
        return ReadTimes;
    }

    public int getLikes() {
        return Likes;
    }

    public String getAddTime() {
        return AddTime;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public boolean isLikes() {
        return IsLikes;
    }

    public boolean isLearn() {
        return IsLearn;
    }

    public String getFxsm() {
        return Fxsm;
    }

    public void setLearn(boolean learn) {
        IsLearn = learn;
    }
}
