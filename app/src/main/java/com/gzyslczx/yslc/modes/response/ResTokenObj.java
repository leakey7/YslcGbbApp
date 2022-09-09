package com.gzyslczx.yslc.modes.response;

public class ResTokenObj {

    private String access_token, token_type;
    private long expires_in;

    public String getAccess_token() {
        return access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public long getExpires_in() {
        return expires_in;
    }
}
