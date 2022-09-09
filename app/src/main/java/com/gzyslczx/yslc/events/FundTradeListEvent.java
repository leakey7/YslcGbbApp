package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.modes.response.ResFundFindRankInfo;

import java.util.List;

public class FundTradeListEvent {

    private boolean Flag, IsEnd;
    private String Error;
    private List<ResFundFindRankInfo> DataList;

    public FundTradeListEvent(boolean flag, List<ResFundFindRankInfo> dataList) {
        Flag = flag;
        DataList = dataList;
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

    public boolean isEnd() {
        return IsEnd;
    }

    public String getError() {
        return Error;
    }

    public List<ResFundFindRankInfo> getDataList() {
        return DataList;
    }
}
