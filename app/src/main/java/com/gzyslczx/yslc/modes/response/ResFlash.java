package com.gzyslczx.yslc.modes.response;

import java.util.List;

public class ResFlash {

    private boolean IsSuccess;
    private String Message;
    private List<ResFlashObj> ResultObj;


    public boolean isSuccess() {
        return IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public List<ResFlashObj> getResultObj() {
        return ResultObj;
    }
}
