package com.gzyslczx.yslc.modes.response;

public class ResLabelSelfObj {

    private int PageSize, CurrentPage, PageCount, RecordCount;
    private ResLabelSelfInfo PageInfo;

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

    public ResLabelSelfInfo getPageInfo() {
        return PageInfo;
    }
}
