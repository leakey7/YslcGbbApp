package com.gzyslczx.yslc.events;

public class NoticeConceptUpdateEvent {

    private String Code;

    public NoticeConceptUpdateEvent(String code) {
        Code = code;
    }

    public String getCode() {
        return Code;
    }
}
