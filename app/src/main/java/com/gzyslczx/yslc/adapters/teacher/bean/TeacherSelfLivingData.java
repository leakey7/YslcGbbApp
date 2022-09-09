package com.gzyslczx.yslc.adapters.teacher.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.gzyslczx.yslc.modes.response.ResTSelfLivingListVideo;

public class TeacherSelfLivingData implements MultiItemEntity {

    public static final int OnLiving=1;
    public static final int OnSub=2;
    public static final int LivingEnd=3;

    private int ItemType;
    private ResTSelfLivingListVideo LivingData;
    private String TName, THeadImg;


    public TeacherSelfLivingData(ResTSelfLivingListVideo livingData, String TName, String THeadImg) {
        if (livingData!=null) {
            LivingData = livingData;
            this.TName = TName;
            this.THeadImg = THeadImg;
            //Status：直播状态 1为未开始  2为直播中  3为已结束
            if (livingData.getStatus()==1){
                ItemType = OnSub;
                return;
            }
            if (livingData.getStatus()==2){
                ItemType = OnLiving;
                return;
            }
            if (livingData.getStatus()==3){
                ItemType = LivingEnd;
                return;
            }
        }
    }

    @Override
    public int getItemType() {
        return ItemType;
    }

    public ResTSelfLivingListVideo getLivingData() {
        return LivingData;
    }

    public String getTName() {
        return TName;
    }

    public String getTHeadImg() {
        return THeadImg;
    }
}
