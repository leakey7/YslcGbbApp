package com.gzyslczx.yslc.events;

public class MainFocusPageChangeEvent {

    private int PageNum=0;


    public MainFocusPageChangeEvent(int pageNum) {
        PageNum = pageNum;
    }

    public int getPageNum() {
        return PageNum;
    }
}
