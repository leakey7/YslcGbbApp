package com.gzyslczx.yslc.modes.response;

public class ResFundTongLoginObj {

    private int id, status;

    private String userId, nickname, phone, date, lastLoginTime, JG_Id;
    private boolean active;

    public int getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public String getUserId() {
        return userId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPhone() {
        return phone;
    }

    public String getDate() {
        return date;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public String getJG_Id() {
        return JG_Id;
    }

    public boolean isActive() {
        return active;
    }
}
