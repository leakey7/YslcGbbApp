package com.gzyslczx.yslc.modes.response;

public class ResMsgBoxObj {

    private int MsgTypId, MsgId;
    private String MsgTypName, MsgContent, MsgImg, MsgNum, DateInfo;

    public int getMsgTypId() {
        return MsgTypId;
    }

    public int getMsgId() {
        return MsgId;
    }

    public String getMsgTypName() {
        return MsgTypName;
    }

    public String getMsgContent() {
        return MsgContent;
    }

    public String getMsgImg() {
        return MsgImg;
    }

    public String getMsgNum() {
        return MsgNum;
    }

    public String getDateInfo() {
        return DateInfo;
    }

    public void setMsgNum(String msgNum) {
        MsgNum = msgNum;
    }
}
