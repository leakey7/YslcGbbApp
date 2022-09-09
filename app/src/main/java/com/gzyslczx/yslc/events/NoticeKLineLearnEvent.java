package com.gzyslczx.yslc.events;

public class NoticeKLineLearnEvent {

    private boolean IsLearn;
    //type 1=视频； 2=文章
    private int type, Id, CId;

    public NoticeKLineLearnEvent(boolean isLearn, int type, int id, int cid) {
        IsLearn = isLearn;
        this.type = type;
        Id = id;
        CId = cid;
    }

    public boolean isLearn() {
        return IsLearn;
    }

    public int getId() {
        return Id;
    }

    public int getType() {
        return type;
    }

    public int getCId() {
        return CId;
    }
}
