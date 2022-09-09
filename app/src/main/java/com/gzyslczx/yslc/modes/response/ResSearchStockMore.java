package com.gzyslczx.yslc.modes.response;

import java.util.List;

public class ResSearchStockMore {
    private boolean IsSuccess;
    private String Message;
    private List<ResSearchStock> ResultObj;

    public boolean isSuccess() {
        return IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public List<ResSearchStock> getResultObj() {
        return ResultObj;
    }
}
