package com.gzyslczx.yslc.modes.response;

public class ResArtDetailsStock {

    private String Nid, Title, DateInfo, CheckUrl, APPUrl;
    private int ViewUser, Status;
    private boolean IsBuy;

    public String getNid() {
        return Nid;
    }

    public String getTitle() {
        return Title;
    }

    public String getDateInfo() {
        return DateInfo;
    }

    public String getCheckUrl() {
        return CheckUrl;
    }

    public String getAPPUrl() {
        return APPUrl;
    }

    public int getViewUser() {
        return ViewUser;
    }

    public int getStatus() {
        return Status;
    }

    public boolean isBuy() {
        return IsBuy;
    }
}
