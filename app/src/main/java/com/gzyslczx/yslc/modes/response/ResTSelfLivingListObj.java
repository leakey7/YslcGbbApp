package com.gzyslczx.yslc.modes.response;

public class ResTSelfLivingListObj {

    private int PageSize, CurrentPage, PageCount, RecordCount;
    private ResTSelfLivingListInfo PageInfo;


    public int getPageSize() {
        return PageSize;
    }

    public int getCurrentPage() {
        return CurrentPage;
    }

    public int getPageCount() {
        return PageCount;
    }

    public int getRecordCount() {
        return RecordCount;
    }

    public ResTSelfLivingListInfo getPageInfo() {
        return PageInfo;
    }
}
