package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.adapters.label.bean.LabelSelfData;

import java.util.List;

public class LabelSelfListEvent {

    private boolean Flag, IsEnd;
    private String Error;
    private List<LabelSelfData> DataList;

    public LabelSelfListEvent(boolean flag, List<LabelSelfData> dataList) {
        Flag = flag;
        DataList = dataList;
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

    public List<LabelSelfData> getDataList() {
        return DataList;
    }
}
