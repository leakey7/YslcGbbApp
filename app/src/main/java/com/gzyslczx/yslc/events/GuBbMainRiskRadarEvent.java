package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.modes.response.ResMainRiskRadarObj;

import java.util.List;

public class GuBbMainRiskRadarEvent {

    private boolean Flag;
    private List<ResMainRiskRadarObj> DataList;
    private String Error;


    public GuBbMainRiskRadarEvent(boolean flag, List<ResMainRiskRadarObj> dataList) {
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

    public List<ResMainRiskRadarObj> getDataList() {
        return DataList;
    }
}
