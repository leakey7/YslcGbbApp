package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.modes.response.ResMsgBoxObj;

import java.util.List;

public class MsgBoxStyleEvent {

    private boolean Flag;
    private String Error;
    private List<ResMsgBoxObj> DataList;

    public MsgBoxStyleEvent(boolean flag, List<ResMsgBoxObj> dataList) {
        Flag = flag;
        DataList = dataList;
    }

    public String getError() {
        return Error;
    }

    public boolean isFlag() {
        return Flag;
    }

    public List<ResMsgBoxObj> getDataList() {
        return DataList;
    }

    public void setError(String error) {
        Error = error;
    }
}
