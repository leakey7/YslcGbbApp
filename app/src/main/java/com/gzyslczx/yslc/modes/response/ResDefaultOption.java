package com.gzyslczx.yslc.modes.response;

import java.util.List;

public class ResDefaultOption {

    private boolean IsSuccess;
    private String Message;
    private List<ResDefaultOptionObj> ResultObj;

    public boolean isSuccess() {
        return IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public List<ResDefaultOptionObj> getResultObj() {
        return ResultObj;
    }
}
