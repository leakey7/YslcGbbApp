package com.gzyslczx.yslc.adapters.main.bean;

import android.text.TextUtils;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.gzyslczx.yslc.modes.response.ResMyFocusObj;

public class MainMyFocusData implements MultiItemEntity {

    public static final int TeacherItem = 1;
    public static final int LabelItem = 2;
    private ResMyFocusObj data;
    private int ItemType;

    public MainMyFocusData(ResMyFocusObj data) {
        this.data = data;
        if (TextUtils.isEmpty(data.getRemark())){
            ItemType = LabelItem;
        }else {
            ItemType = TeacherItem;
        }
    }

    @Override
    public int getItemType() {
        return ItemType;
    }

    public ResMyFocusObj getData() {
        return data;
    }
}
