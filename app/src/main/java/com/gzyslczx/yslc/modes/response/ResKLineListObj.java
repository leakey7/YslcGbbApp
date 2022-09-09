package com.gzyslczx.yslc.modes.response;

public class ResKLineListObj {

    private int PageSize, CurrentPage, PageCount, RecordCount;
    public ResKLineListInfo PageInfo;

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

    public ResKLineListInfo getPageInfo() {
        return PageInfo;
    }
}
