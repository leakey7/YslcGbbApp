package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.adapters.main.bean.MainMyFocusData;
import com.gzyslczx.yslc.modes.response.ResMyFocusObj;

import java.util.List;

public class GuBbMainMoreFocusEvent {

    private boolean Flag;
    private List<MainMyFocusData> DataList;
    private String Error;

    public GuBbMainMoreFocusEvent(boolean flag, List<MainMyFocusData> dataList) {
        Flag = flag;
        DataList = dataList;
    }

    public void setError(String error) {
        Error = error;
    }

    public boolean isFlag() {
        return Flag;
    }

    public List<MainMyFocusData> getDataList() {
        return DataList;
    }

    public String getError() {
        return Error;
    }

}
