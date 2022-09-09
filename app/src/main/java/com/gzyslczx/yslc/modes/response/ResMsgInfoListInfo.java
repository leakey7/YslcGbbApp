package com.gzyslczx.yslc.modes.response;

public class ResMsgInfoListInfo {

    /*
    * Read： 0=未读 1=已读
    * */
    private int Id, Read, SendType, SendState;
    private String Phone, Title, Content, Url, AddDate;
    private boolean Active;

    public int getId() {
        return Id;
    }

    public int getRead() {
        return Read;
    }

    public int getSendType() {
        return SendType;
    }

    public int getSendState() {
        return SendState;
    }

    public String getPhone() {
        return Phone;
    }

    public String getTitle() {
        return Title;
    }

    public String getContent() {
        return Content;
    }

    public String getUrl() {
        return Url;
    }

    public String getAddDate() {
        return AddDate;
    }

    public boolean isActive() {
        return Active;
    }

    public void setRead(int read) {
        Read = read;
    }
}
