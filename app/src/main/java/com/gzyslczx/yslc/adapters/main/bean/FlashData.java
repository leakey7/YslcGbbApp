package com.gzyslczx.yslc.adapters.main.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.gzyslczx.yslc.modes.response.ResFlashObj;

public class FlashData implements MultiItemEntity {

    public static final int DateType = 1;
    public static final int InfoType = 2;

    private int ItemType = 0;
    private ResFlashObj data;

    public FlashData(int ItemType, ResFlashObj data){
        this.ItemType = ItemType;
        this.data = data;
    }


    @Override
    public int getItemType() {
        return ItemType;
    }

    public ResFlashObj getData() {
        return data;
    }

}
