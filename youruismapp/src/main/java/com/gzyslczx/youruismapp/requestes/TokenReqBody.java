package com.gzyslczx.youruismapp.requestes;

public class TokenReqBody {

    private String appkey, appsecret;
    private int devicetype;

    public TokenReqBody(String appkey, String appsecret, int devicetype) {
        this.appkey = appkey;
        this.appsecret = appsecret;
        this.devicetype = devicetype;
    }

    public String getAppkey() {
        return appkey;
    }

    public String getAppsecret() {
        return appsecret;
    }

    public int getDevicetype() {
        return devicetype;
    }
}
