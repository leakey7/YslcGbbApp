package com.gzyslczx.yslc.modes.request;

public class ReqFundTongRank {

    private int adid, tid, currentpage, pagesize, sort;


    public ReqFundTongRank(int iconId, int typeId, int currentpage, int pagesize) {
        this.adid = iconId;
        this.tid = typeId;
        this.currentpage = currentpage;
        this.pagesize = pagesize;
    }

    /*
     * 0=升序 1=降序
     * */
    public void setSort(int sort) {
        this.sort = sort;
    }

}
