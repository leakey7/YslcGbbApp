package com.gzyslczx.yslc.tools.yourui;

public class YRTokenRes {

    private int code=-1;
    private String msg, token;
    private long endDate;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public boolean isSuccess() {
        if (code==0){
            return true;
        }
        return false;
    }
}
