package com.gzyslczx.yslc.adapters.kline.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.gzyslczx.yslc.modes.response.ResKLineListArt;
import com.gzyslczx.yslc.modes.response.ResKLineListVideo;

public class KLineData implements MultiItemEntity {

    private int ItemType;
    private ResKLineListArt aListData;
    private ResKLineListVideo vListData;
    //ContentType：1=视频；2=文章
    private int ContentType;
    public static final int UnLoginType = 0;
    public static final int IsLoginType = 1;


    public KLineData(boolean isLogin, ResKLineListArt listData) {
        if (isLogin){
            ItemType = IsLoginType;
        }else {
            ItemType = UnLoginType;
        }
        aListData = listData;
        ContentType = 2;
    }

    public KLineData(boolean isLogin, ResKLineListVideo listData) {
        if (isLogin){
            ItemType = IsLoginType;
        }else {
            ItemType = UnLoginType;
        }
        vListData = listData;
        ContentType = 1;
    }

    @Override
    public int getItemType() {
        return ItemType;
    }

    public ResKLineListArt getaListData() {
        return aListData;
    }

    public ResKLineListVideo getvListData() {
        return vListData;
    }

    public int getContentType() {
        return ContentType;
    }
}
