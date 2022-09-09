package com.gzyslczx.yslc.events;

public class SearchPageChangeEvent {

    /*
    * 0：搜索历史 1:模糊搜索 2：更多股票搜索 3：更多基金搜索 4：更多相关内容搜索
    * */
    private int Type;
    private String KeyWord;

    public SearchPageChangeEvent(int type, String keyWord) {
        Type = type;
        KeyWord = keyWord;
    }

    public int getType() {
        return Type;
    }

    public String getKeyWord() {
        return KeyWord;
    }
}
