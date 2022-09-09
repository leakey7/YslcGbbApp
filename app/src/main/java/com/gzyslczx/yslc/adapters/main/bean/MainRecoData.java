package com.gzyslczx.yslc.adapters.main.bean;

import android.text.TextUtils;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.gzyslczx.yslc.modes.response.ResMainRecoInfo;

public class MainRecoData implements MultiItemEntity {

    public static final int JustText = 1;
    public static final int MorePic = 2;
    public static final int BVideo = 3;
    public static final int SVideo = 4;
    public static final int OnePic = 5;

    private int ItemType;
    private ResMainRecoInfo info;
    private boolean IsTeacherItem = false;

    public MainRecoData(ResMainRecoInfo info) {
        this.info = info;
        /*
        * 区分栏目类或名师类
        * */
        if (TextUtils.isEmpty(info.getNewLabel()) || (!info.getNewLabel().equals("主题资讯") && !info.getNewLabel().equals("投顾论市"))){
            IsTeacherItem = true;
        }
        /*
        * ContentType：1.文章，2.观点，3.语音，4.视频
        * VideoType：视频类型（0为横屏视频、1为竖屏小视频）
        * */
        if (info.getContentType() == 4){
            if (info.getVideoType()==0){
                ItemType = BVideo;
                return;
            }else {
                ItemType = SVideo;
                return;
            }
        }else if (info.getContentType()==1 || info.getContentType()==2){
            //文章或观点
            if (info.getArrImg() != null && info.getArrImg().length > 0) {
                if (info.getArrImg().length==1){
                    ItemType = OnePic;
                }else {
                    ItemType = MorePic;
                };
                return;
            }else {
                ItemType = JustText;
            }
        }else {
            //语音
        }
    }

    public MainRecoData(int itemType) {
        ItemType = itemType;
    }

    @Override
    public int getItemType() {
        return ItemType;
    }


    public ResMainRecoInfo getInfo() {
        return info;
    }

    public boolean isTeacherItem() {
        return IsTeacherItem;
    }
}
