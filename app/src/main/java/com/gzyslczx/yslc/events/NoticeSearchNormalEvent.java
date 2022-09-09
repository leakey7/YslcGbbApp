package com.gzyslczx.yslc.events;

public class NoticeSearchNormalEvent {

    private String KeyWord;
    private boolean IsMore;
    //1：更多股票 2：更多基金 3：更多相关内容
    private int MoreType;

    public NoticeSearchNormalEvent(String keyWord, boolean isMore) {
        KeyWord = keyWord;
        IsMore = isMore;
    }

    public void setMoreType(int moreType) {
        MoreType = moreType;
    }

    public String getKeyWord() {
        return KeyWord;
    }

    public boolean isMore() {
        return IsMore;
    }

    public int getMoreType() {
        return MoreType;
    }
}
