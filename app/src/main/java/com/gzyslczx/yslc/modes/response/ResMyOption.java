package com.gzyslczx.yslc.modes.response;

import java.util.List;

public class ResMyOption {

    private boolean IsSuccess;
    private String Message;
    private List<ResMyOptionObj> ResultObj;

    public boolean isSuccess() {
        return IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public List<ResMyOptionObj> getResultObj() {
        return ResultObj;
    }
}
