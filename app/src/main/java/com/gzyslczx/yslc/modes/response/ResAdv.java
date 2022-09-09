package com.gzyslczx.yslc.modes.response;

import java.util.List;

public class ResAdv {

    private boolean IsSuccess;
    private String Message;
    private List<ResAdvObj> ResultObj;

    public boolean isSuccess() {
        return IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public List<ResAdvObj> getResultObj() {
        return ResultObj;
    }
}
