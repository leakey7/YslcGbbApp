package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.modes.response.ResMyOptionObj;

import java.util.List;

public class MyOptionEvent {

    private boolean Flag;
    private String Error;
    private List<ResMyOptionObj> DataList;

    public MyOptionEvent(boolean flag, List<ResMyOptionObj> dataList) {
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

    public List<ResMyOptionObj> getDataList() {
        return DataList;
    }
}
