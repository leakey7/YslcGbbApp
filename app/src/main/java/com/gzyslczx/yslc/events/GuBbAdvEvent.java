package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.modes.response.ResAdvObj;

import java.util.List;

public class GuBbAdvEvent {

    private List<ResAdvObj> DataList;
    private boolean Flag;
    private String Error;
    private int id;

    public GuBbAdvEvent(int id, List<ResAdvObj> dataList, boolean flag) {
        this.id = id;
        DataList = dataList;
        Flag = flag;
    }

    public void setError(String error) {
        Error = error;
    }

    public List<ResAdvObj> getDataList() {
        return DataList;
    }

    public boolean isFlag() {
        return Flag;
    }

    public String getError() {
        return Error;
    }

    public int getId() {
        return id;
    }
}
