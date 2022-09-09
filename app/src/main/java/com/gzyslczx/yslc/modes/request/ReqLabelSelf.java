package com.gzyslczx.yslc.modes.request;

public class ReqLabelSelf {

    private int pagesize, currentpage;
    private String userid, label;

    public ReqLabelSelf(int pagesize, int currentpage, String userid, String label) {
        this.pagesize = pagesize;
        this.currentpage = currentpage;
        this.userid = userid;
        this.label = label;
    }
}
