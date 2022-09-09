package com.gzyslczx.yslc.modes.response;

import java.util.List;

public class ResMainNorthBuy {

    private boolean IsSuccess;
    private String Message;
    private List<ResMainNorthBuyObj> ResultObj;

    public boolean isSuccess() {
        return IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public List<ResMainNorthBuyObj> getResultObj() {
        return ResultObj;
    }
}
