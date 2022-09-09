package com.gzyslczx.yslc.modes.response;

import java.util.List;

public class ResMainTeacherLiving {

    private boolean IsSuccess;
    private String Message;
    private List<ResMainTeacherLivingObj> ResultObj;

    public boolean isSuccess() {
        return IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public List<ResMainTeacherLivingObj> getResultObj() {
        return ResultObj;
    }
}
