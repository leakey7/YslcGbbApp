package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.modes.response.ResDefaultOptionObj;

import java.util.List;

public class DefaultOptionEvent {

    private boolean Flag;
    private List<ResDefaultOptionObj> DatList;
    private String Error;

    public DefaultOptionEvent(boolean flag, List<ResDefaultOptionObj> datList) {
        Flag = flag;
        DatList = datList;
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

    public List<ResDefaultOptionObj> getDatList() {
        return DatList;
    }
}
