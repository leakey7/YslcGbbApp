package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.modes.response.ResFundTongIconObj;

import java.util.List;

public class FundTongHomeIconTabEvent {

    private boolean Flag;
    private String Error;
    private List<ResFundTongIconObj> DataList;

    public FundTongHomeIconTabEvent(boolean flag, List<ResFundTongIconObj> dataList) {
        Flag = flag;
        DataList = dataList;
    }

    public void setError(String error) {
        Error = error;
    }

    public boolean isFlag() {
        return Flag;
    }

    public String getError() {
        return Error;
    }

    public List<ResFundTongIconObj> getDataList() {
        return DataList;
    }
}
