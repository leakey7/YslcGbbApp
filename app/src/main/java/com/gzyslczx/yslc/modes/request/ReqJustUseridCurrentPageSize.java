package com.gzyslczx.yslc.modes.request;

public class ReqJustUseridCurrentPageSize {

    private String userid;
    private int currentpage, pagesize;

    public ReqJustUseridCurrentPageSize(String userid, int currentpage, int pagesize) {
        this.userid = userid;
        this.currentpage = currentpage;
        this.pagesize = pagesize;
    }
}
