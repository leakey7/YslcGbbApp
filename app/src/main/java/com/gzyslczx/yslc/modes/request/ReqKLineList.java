package com.gzyslczx.yslc.modes.request;

public class ReqKLineList {

    private String phone;
    private int categoryid, pagesize, currentpage;

    public ReqKLineList(String phone, int categoryid, int pagesize, int currentpage) {
        this.phone = phone;
        this.categoryid = categoryid;
        this.pagesize = pagesize;
        this.currentpage = currentpage;
    }
}
