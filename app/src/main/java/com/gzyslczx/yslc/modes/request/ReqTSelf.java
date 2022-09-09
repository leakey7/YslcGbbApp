package com.gzyslczx.yslc.modes.request;

public class ReqTSelf {

    /*
     * ntype： 0=全部； 1=文章； 2=观点； 3=视频； 4=小视频
     * */
    private int pagesize, currentpage, ntype ;
    private String teacherid, userid;

    public ReqTSelf(int pagesize, int currentpage, int ntype, String teacherid, String userid) {
        this.pagesize = pagesize;
        this.currentpage = currentpage;
        this.ntype = ntype;
        this.teacherid = teacherid;
        this.userid = userid;
    }
}
