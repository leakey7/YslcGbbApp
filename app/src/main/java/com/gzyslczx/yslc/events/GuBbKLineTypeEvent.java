package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.modes.response.ResKLineTypeObj;

import java.util.List;

public class GuBbKLineTypeEvent {

    private boolean Flag;
    private List<ResKLineTypeObj> DataList;
    private String Error;

    public GuBbKLineTypeEvent(boolean flag, List<ResKLineTypeObj> dataList) {
        Flag = flag;
        DataList = dataList;
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

    public List<ResKLineTypeObj> getDataList() {
        return DataList;
    }
}
