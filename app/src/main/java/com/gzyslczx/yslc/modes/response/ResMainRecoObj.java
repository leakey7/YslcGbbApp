package com.gzyslczx.yslc.modes.response;

import java.util.List;

public class ResMainRecoObj {

    private int PageSize, CurrentPage, PageCount, RecordCount;
    private List<ResMainRecoInfo> PageInfo;

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

    public List<ResMainRecoInfo> getPageInfo() {
        return PageInfo;
    }
}
