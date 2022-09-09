package com.gzyslczx.yslc.modes.request;

public class ReqLoginByCode {
    private String phone, code, platform, jgid;

    public ReqLoginByCode(String phone, String code, String jgid) {
        this.phone = phone;
        this.code = code;
        platform = "android";
        this.jgid = jgid;
    }
}
