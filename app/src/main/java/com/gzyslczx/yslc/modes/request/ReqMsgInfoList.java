package com.gzyslczx.yslc.modes.request;

public class ReqMsgInfoList {

    private int msgtype, pagesize, currentpage;
    private String phone;

    public ReqMsgInfoList(int msgtype, int pagesize, int currentpage, String phone) {
        this.msgtype = msgtype;
        this.pagesize = pagesize;
        this.currentpage = currentpage;
        this.phone = phone;
    }
}
