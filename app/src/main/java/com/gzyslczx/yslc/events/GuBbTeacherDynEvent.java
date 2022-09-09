package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.modes.response.ResTeacherDynObj;

import java.util.List;

public class GuBbTeacherDynEvent {

    private boolean Flag;
    private List<ResTeacherDynObj> DataList;
    private String Error;

    public GuBbTeacherDynEvent(boolean flag, List<ResTeacherDynObj> dataList) {
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

    public List<ResTeacherDynObj> getDataList() {
        return DataList;
    }
}
