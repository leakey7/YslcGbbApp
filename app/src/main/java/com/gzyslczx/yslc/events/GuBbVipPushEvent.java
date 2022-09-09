package com.gzyslczx.yslc.events;

public class GuBbVipPushEvent {

    private String VipWebPath;

    public GuBbVipPushEvent(String vipWebPath) {
        VipWebPath = vipWebPath;
    }

    public String getVipWebPath() {
        return VipWebPath;
    }
}
