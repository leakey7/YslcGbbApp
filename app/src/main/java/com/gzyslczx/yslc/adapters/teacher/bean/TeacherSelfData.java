package com.gzyslczx.yslc.adapters.teacher.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.gzyslczx.yslc.modes.response.ResTSelfList;

public class TeacherSelfData implements MultiItemEntity {

    public static final int JustText = 1;
    public static final int PicText = 2;
    public static final int BVideo = 3;
    public static final int SVideo = 4;
    public static final int OnePic = 5;

    private int ItemType;
    private ResTSelfList listData;
    private String TName, THeadImg;

    public TeacherSelfData( ResTSelfList data, String TName, String THeadImg) {
        this.listData = data;
        this.TName = TName;
        this.THeadImg = THeadImg;
        /*
         * ContentType：1.文章，2.观点，3.语音，4.视频
         * VideoType：视频类型（0为横屏视频、1为竖屏小视频）
         * */
        if (data.getContentType() == 4){
            if (data.getVideoType()==0){
                ItemType = BVideo;
                return;
            }else {
                ItemType = SVideo;
                return;
            }
        }else {
            if (data.getArrImg() != null && data.getArrImg().length > 0) {
                if (data.getArrImg().length==1){
                    ItemType = OnePic;
                }else {
                    ItemType = PicText;
                }
                return;
            }else {
                ItemType = JustText;
            }
        }
    }

    @Override
    public int getItemType() {
        return ItemType;
    }

    public ResTSelfList getListData() {
        return listData;
    }

    public String getTName() {
        return TName;
    }

    public String getTHeadImg() {
        return THeadImg;
    }
}
