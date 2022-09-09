package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.modes.response.ResMyFundOptionObj;

import java.util.List;

public class MineFundOptionEvent {

    private boolean Flag;
    private String Error;
    private List<ResMyFundOptionObj> DataList;

    public MineFundOptionEvent(boolean flag, List<ResMyFundOptionObj> dataList) {
        Flag = flag;
        DataList = dataList;
    }

    public boolean isFlag() {
        return Flag;
    }

    public List<ResMyFundOptionObj> getDataList() {
        return DataList;
    }

    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }
}
