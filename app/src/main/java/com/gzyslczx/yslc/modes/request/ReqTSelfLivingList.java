package com.gzyslczx.yslc.modes.request;

public class ReqTSelfLivingList {

    private int pagesize, currentpage ;
    private String teacherid, phone;

    public ReqTSelfLivingList(int pagesize, int currentpage, String teacherid, String phone) {
        this.pagesize = pagesize;
        this.currentpage = currentpage;
        this.teacherid = teacherid;
        this.phone = phone;
    }
}
