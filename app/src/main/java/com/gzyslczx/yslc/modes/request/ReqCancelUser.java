package com.gzyslczx.yslc.modes.request;

public class ReqCancelUser {
    private String phone, code;

    public ReqCancelUser(String phone, String code) {
        this.phone = phone;
        this.code = code;
    }
}
