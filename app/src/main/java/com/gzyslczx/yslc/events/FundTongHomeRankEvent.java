package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.modes.response.ResFundTongRankInfo;

import java.util.List;

public class FundTongHomeRankEvent {

    private boolean Flag, IsEnd, IsInit;
    private String Error;
    private List<ResFundTongRankInfo> DataList;

    public FundTongHomeRankEvent(boolean flag, List<ResFundTongRankInfo> dataList) {
        Flag = flag;
        DataList = dataList;
    }

    public boolean isInit() {
        return IsInit;
    }

    public void setInit(boolean init) {
        IsInit = init;
    }

    public boolean isEnd() {
        return IsEnd;
    }

    public void setEnd(boolean end) {
        IsEnd = end;
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

    public List<ResFundTongRankInfo> getDataList() {
        return DataList;
    }
}
