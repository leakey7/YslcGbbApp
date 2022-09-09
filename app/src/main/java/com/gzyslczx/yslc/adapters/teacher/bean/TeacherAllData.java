package com.gzyslczx.yslc.adapters.teacher.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.gzyslczx.yslc.modes.response.ResTeacherAllObj;

public class TeacherAllData implements MultiItemEntity {

    public static final int NoneContent = 0;
    public static final int JustText = 1;
    public static final int MorePic = 2;
    public static final int BVideo = 3;
    public static final int SVideo = 4;
    public static final int OnePic = 5;

    private int ItemType;
    private ResTeacherAllObj ObjData;


    public TeacherAllData(ResTeacherAllObj objData) {
        ObjData = objData;
        /*
         * ContentType：1.文章，2.观点，3.语音，4.视频
         * VideoType：视频类型（0为横屏视频、1为竖屏小视频）
         * */
        if (objData.getContentType() == 4){
            if (objData.getVideoType()==0){
                ItemType = BVideo;
                return;
            }else {
                ItemType = SVideo;
                return;
            }
        }else if (objData.getContentType() == 0){
            ItemType = NoneContent;
        } else {
            if (objData.getArrImg() != null && objData.getArrImg().length > 0) {
                if (objData.getArrImg().length==1){
                    ItemType = OnePic;
                }else {
                    ItemType = MorePic;
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

    public ResTeacherAllObj getObjData() {
        return ObjData;
    }
}
