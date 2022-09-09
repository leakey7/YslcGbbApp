package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.adapters.main.bean.FlashData;

import java.util.List;

public class GuBbYsFlashEvent {

    private boolean Flag;
    private List<FlashData> DataList;
    private String Error;
    private int ListType;
    private boolean IsEnd;

    public GuBbYsFlashEvent(boolean flag, int listType, List<FlashData> dataList) {
        Flag = flag;
        DataList = dataList;
        ListType = listType;
    }

    public boolean isEnd() {
        return IsEnd;
    }

    public void setEnd(boolean end) {
        IsEnd = end;
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

    public List<FlashData> getDataList() {
        return DataList;
    }

    public int getListType() {
        return ListType;
    }
}
