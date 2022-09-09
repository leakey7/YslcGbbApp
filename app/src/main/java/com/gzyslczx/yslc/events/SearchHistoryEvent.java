package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.modes.response.ResSearchHisObj;

public class SearchHistoryEvent {

    private boolean Flag;
    private ResSearchHisObj ObjData;
    private String Error;

    public SearchHistoryEvent(boolean flag, ResSearchHisObj objData) {
        Flag = flag;
        ObjData = objData;
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

    public ResSearchHisObj getObjData() {
        return ObjData;
    }
}
