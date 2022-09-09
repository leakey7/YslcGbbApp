package com.gzyslczx.yslc.modes.request;

public class ReqPhonePlatform {

    private String platform, phone, jgid;

    public ReqPhonePlatform(String phone, String jgid) {
        this.phone = phone;
        platform = "android";
        this.jgid = jgid;
    }

}
