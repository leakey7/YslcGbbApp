package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.modes.response.ResKLineDetailsObj;

public class GuBbKLineDetailEvent {

    private boolean Flag;
    private ResKLineDetailsObj DetailsObj;
    //type 1=视频；2=文章
    private int type;
    private String Error;

    public GuBbKLineDetailEvent(boolean flag, ResKLineDetailsObj detailsObj) {
        Flag = flag;
        DetailsObj = detailsObj;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isFlag() {
        return Flag;
    }

    public ResKLineDetailsObj getDetailsObj() {
        return DetailsObj;
    }

    public int getType() {
        return type;
    }

    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }
}
