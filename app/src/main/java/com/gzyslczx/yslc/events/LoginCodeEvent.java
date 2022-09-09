package com.gzyslczx.yslc.events;

public class LoginCodeEvent {

    private boolean Flag;
    private String LoginCode, Error;

    public LoginCodeEvent(boolean flag, String loginCode) {
        Flag = flag;
        LoginCode = loginCode;
    }

    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }

    public boolean isFlag() {
        return Flag;
    }

    public String getLoginCode() {
        return LoginCode;
    }
}
