package com.gzyslczx.yslc.modes.response;

import java.util.List;

public class ResMainRiskRadar {

    private boolean IsSuccess;
    private String Message;
    private List<ResMainRiskRadarObj> ResultObj;

    public boolean isSuccess() {
        return IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public List<ResMainRiskRadarObj> getResultObj() {
        return ResultObj;
    }
}
