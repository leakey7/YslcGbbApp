package com.gzyslczx.yslc.modes.response;

public class ResTSelfObj {

    private int PageSize, CurrentPage, PageCount, RecordCount;
    private ResTSelfInfo PageInfo;

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

    public ResTSelfInfo getPageInfo() {
        return PageInfo;
    }
}
