package com.gzyslczx.yslc.modes.response;

import java.util.List;

public class ResMainSup {

    private boolean IsSuccess;
    private String Message;
    private List<ResMainSupObj> ResultObj;

    public boolean isSuccess() {
        return IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public List<ResMainSupObj> getResultObj() {
        return ResultObj;
    }
}
