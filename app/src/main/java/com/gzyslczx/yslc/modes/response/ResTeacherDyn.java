package com.gzyslczx.yslc.modes.response;

import java.util.List;

public class ResTeacherDyn {

    private boolean IsSuccess;
    private String Message;
    private List<ResTeacherDynObj> ResultObj;

    public boolean isSuccess() {
        return IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public List<ResTeacherDynObj> getResultObj() {
        return ResultObj;
    }
}
