package com.gzyslczx.yslc.adapters.main.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.gzyslczx.yslc.modes.response.ResMainTeacherLivingObj;

public class MainTeacherLivingData implements MultiItemEntity {

    public static final int OnLiving=1;
    public static final int OnSub=2;
    public static final int PlayBack=3;
    public static final int VipLiving=4;
    private int ItemType;
    private ResMainTeacherLivingObj DataObj;

    public MainTeacherLivingData(int itemType, ResMainTeacherLivingObj dataObj) {
        ItemType = itemType;
        DataObj = dataObj;
    }

    @Override
    public int getItemType() {
        return ItemType;
    }

    public ResMainTeacherLivingObj getDataObj() {
        return DataObj;
    }
}
