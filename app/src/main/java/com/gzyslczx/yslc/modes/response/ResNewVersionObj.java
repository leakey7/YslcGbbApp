package com.gzyslczx.yslc.modes.response;

public class ResNewVersionObj {

    private String Id, Name, VersionNum, Platform, Url, AddDateTime, Desc;
    private boolean IsForce;

    public String getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public String getVersionNum() {
        return VersionNum;
    }

    public String getPlatform() {
        return Platform;
    }

    public String getUrl() {
        return Url;
    }

    public String getAddDateTime() {
        return AddDateTime;
    }

    public String getDesc() {
        return Desc;
    }

    public boolean isForce() {
        return IsForce;
    }
}
