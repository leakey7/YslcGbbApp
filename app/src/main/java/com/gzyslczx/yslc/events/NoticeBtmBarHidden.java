package com.gzyslczx.yslc.events;

public class NoticeBtmBarHidden {

    private int state;

    /*
    * state 1：隐藏 0：显示
    * */
    public NoticeBtmBarHidden(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }
}
