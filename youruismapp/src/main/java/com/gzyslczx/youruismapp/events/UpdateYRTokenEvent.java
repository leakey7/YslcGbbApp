package com.gzyslczx.youruismapp.events;

public class UpdateYRTokenEvent {

    private boolean isEff;
    private String token;

    public UpdateYRTokenEvent(boolean isEff, String token) {
        this.isEff = isEff;
        this.token = token;
    }

    public boolean isEff() {
        return isEff;
    }

    public String getToken() {
        return token;
    }
}
