package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.adapters.main.bean.MainRecoData;

import java.util.List;

public class GuBbMainRecoAndVideoListEvent {

    private boolean Flag;
    private String Error;
    private List<MainRecoData> dataList;
    private boolean ListPageEnd;
    private int ListType;
    private int CurrentPage;

    public GuBbMainRecoAndVideoListEvent(boolean flag, String error, List<MainRecoData> dataList, int listType, int CurrentPage) {
        Flag = flag;
        Error = error;
        this.dataList = dataList;
        ListType = listType;
        this.CurrentPage = CurrentPage;
    }

    public boolean isFlag() {
        return Flag;
    }

    public String getError() {
        return Error;
    }

    public List<MainRecoData> getDataList() {
        return dataList;
    }

    public boolean isListPageEnd() {
        return ListPageEnd;
    }

    public void setListPageEnd(boolean listPageEnd) {
        ListPageEnd = listPageEnd;
    }

    public int getListType() {
        return ListType;
    }

    public int getCurrentPage() {
        return CurrentPage;
    }
}
