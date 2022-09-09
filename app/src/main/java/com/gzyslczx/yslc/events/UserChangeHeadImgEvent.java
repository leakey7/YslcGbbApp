package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.modes.response.ResJustStrObj;

public class UserChangeHeadImgEvent {

    private boolean Frag;
    private String Error;
    private String ImgUrl;

    public UserChangeHeadImgEvent(boolean frag, String data) {
        Frag = frag;
        ImgUrl = data;
    }

    public void setError(String error) {
        Error = error;
    }

    public boolean isFrag() {
        return Frag;
    }

    public String getError() {
        return Error;
    }

    public String getImgUrl() {
        return ImgUrl;
    }
}
