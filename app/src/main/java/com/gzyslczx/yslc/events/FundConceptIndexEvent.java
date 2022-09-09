package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.modes.response.ResTradeIndexObj;

import java.util.List;

public class FundConceptIndexEvent {

    private boolean Flag;
    private String Error;
    private List<ResTradeIndexObj> DataList;

    public FundConceptIndexEvent(boolean flag, List<ResTradeIndexObj> dataList) {
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

    public List<ResTradeIndexObj> getDataList() {
        return DataList;
    }
}
