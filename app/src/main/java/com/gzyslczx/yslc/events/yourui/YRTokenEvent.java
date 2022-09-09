package com.gzyslczx.yslc.events.yourui;

public class YRTokenEvent {

    private boolean isUpdate;
    private String token;

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
