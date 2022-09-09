package com.gzyslczx.yslc.modes.request;

public class ReqScrollSmallVideo {

    private String userid, newsid;
    private int pagesize, currentpage;

    public ReqScrollSmallVideo(String userid, String newsid, int pagesize, int currentpage) {
        this.userid = userid;
        this.newsid = newsid;
        this.pagesize = pagesize;
        this.currentpage = currentpage;
    }
}
