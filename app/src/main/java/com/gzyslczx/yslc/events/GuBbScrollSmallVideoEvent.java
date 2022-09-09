package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.modes.response.ResScrollSmallVideoInfo;

import java.util.List;

public class GuBbScrollSmallVideoEvent {

    private boolean Flag;
    private List<ResScrollSmallVideoInfo> DataList;
    private boolean IsEnd;
    private String Error;


    public GuBbScrollSmallVideoEvent(boolean flag, List<ResScrollSmallVideoInfo> dataList, boolean isEnd) {
        Flag = flag;
        DataList = dataList;
        IsEnd = isEnd;
    }

    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }

    public boolean isFlag() {
        return Flag;
    }

    public List<ResScrollSmallVideoInfo> getDataList() {
        return DataList;
    }

    public boolean isEnd() {
        return IsEnd;
    }
}
