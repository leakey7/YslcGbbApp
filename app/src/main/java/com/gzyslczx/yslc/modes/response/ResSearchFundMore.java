package com.gzyslczx.yslc.modes.response;

import java.util.List;

public class ResSearchFundMore {

    private boolean IsSuccess;
    private String Message;
    private List<ResSearchFund> ResultObj;

    public boolean isSuccess() {
        return IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public List<ResSearchFund> getResultObj() {
        return ResultObj;
    }
}
