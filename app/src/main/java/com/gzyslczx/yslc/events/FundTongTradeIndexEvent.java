package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.modes.response.ResTradeIndexObj;

import java.util.List;

public class FundTongTradeIndexEvent {

    private boolean Frag;
    private String Error;
    private List<ResTradeIndexObj> DataList;

    public FundTongTradeIndexEvent(boolean frag, List<ResTradeIndexObj> dataList) {
        Frag = frag;
        DataList = dataList;
    }

    public void setError(String error) {
        Error = error;
    }

    public boolean isFrag() {
        return Frag;
    }

    public String getError() {
        return Error;
    }

    public List<ResTradeIndexObj> getDataList() {
        return DataList;
    }
}
