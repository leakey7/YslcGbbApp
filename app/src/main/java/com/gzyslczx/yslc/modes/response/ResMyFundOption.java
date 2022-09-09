package com.gzyslczx.yslc.modes.response;

import java.util.List;

public class ResMyFundOption {

    private boolean IsSuccess;
    private String Message;
    private List<ResMyFundOptionObj> ResultObj;

    public boolean isSuccess() {
        return IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public List<ResMyFundOptionObj> getResultObj() {
        return ResultObj;
    }
}
