package com.gzyslczx.yslc.modes.request;

public class ReqMainReco {

    private int pagesize, currentpage, type;
    private String userid;

    public ReqMainReco(int pagesize, int currentpage, int type, String userid) {
        this.pagesize = pagesize;
        this.currentpage = currentpage;
        this.type = type;
        this.userid = userid;
    }
}
