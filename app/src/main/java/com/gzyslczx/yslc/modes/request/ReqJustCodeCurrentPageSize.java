package com.gzyslczx.yslc.modes.request;

public class ReqJustCodeCurrentPageSize {

    private String code;
    private int currentpage, pagesize;

    public ReqJustCodeCurrentPageSize(String code, int currentpage, int pagesize) {
        this.code = code;
        this.currentpage = currentpage;
        this.pagesize = pagesize;
    }
}
