package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.modes.response.ResFundTongTradeLevelObj;

import java.util.List;

public class FundTongTradeLevelEvent {

    private boolean Flag;
    private String Error;
    private int level;
    private List<ResFundTongTradeLevelObj> DataList;

    public FundTongTradeLevelEvent(boolean flag, List<ResFundTongTradeLevelObj> dataList, int level) {
        Flag = flag;
        DataList = dataList;
        this.level = level;
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

    public List<ResFundTongTradeLevelObj> getDataList() {
        return DataList;
    }

    public int getLevel() {
        return level;
    }
}
