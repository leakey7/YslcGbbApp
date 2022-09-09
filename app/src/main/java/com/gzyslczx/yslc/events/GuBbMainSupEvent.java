package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.modes.response.ResMainSupObj;

import java.util.List;

public class GuBbMainSupEvent {

    private List<ResMainSupObj> DataList;
    private boolean Flag;
    private String Error;

    public GuBbMainSupEvent(List<ResMainSupObj> dataList, boolean flag) {
        DataList = dataList;
        Flag = flag;
    }

    public List<ResMainSupObj> getDataList() {
        return DataList;
    }

    public boolean isFlag() {
        return Flag;
    }

    public void setError(String error) {
        Error = error;
    }

    public String getError() {
        return Error;
    }
}
