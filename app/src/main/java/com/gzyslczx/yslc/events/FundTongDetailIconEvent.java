package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.modes.response.ResAdvObj;

import java.util.List;

public class FundTongDetailIconEvent {

    private boolean Flag;
    private String Error;
    private List<ResAdvObj> DataList;

    public FundTongDetailIconEvent(boolean flag, List<ResAdvObj> dataList) {
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

    public List<ResAdvObj> getDataList() {
        return DataList;
    }
}
