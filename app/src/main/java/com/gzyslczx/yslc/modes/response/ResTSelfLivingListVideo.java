package com.gzyslczx.yslc.modes.response;

public class ResTSelfLivingListVideo {

    private int Id, Status, ReadNumber;
    private String LiveId, TeacherId, Title, Desc, Img, DateInfo;
    private boolean IsBook;

    public int getId() {
        return Id;
    }

    public int getStatus() {
        return Status;
    }

    public int getReadNumber() {
        return ReadNumber;
    }

    public String getLiveId() {
        return LiveId;
    }

    public String getTeacherId() {
        return TeacherId;
    }

    public String getTitle() {
        return Title;
    }

    public String getDesc() {
        return Desc;
    }

    public String getImg() {
        return Img;
    }

    public String getDateInfo() {
        return DateInfo;
    }

    public boolean isBook() {
        return IsBook;
    }
}
