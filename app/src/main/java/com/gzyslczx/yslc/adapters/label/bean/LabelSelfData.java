package com.gzyslczx.yslc.adapters.label.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.gzyslczx.yslc.modes.response.ResMainRecoInfo;

public class LabelSelfData implements MultiItemEntity {

    public static final int JustText = 1;
    public static final int PicText = 2;
    public static final int BVideo = 3;
    public static final int SVideo = 4;
    public static final int OnePic = 5;

    private int ItemType;
    private ResMainRecoInfo info;

    @Override
    public int getItemType() {
        return ItemType;
    }

    public LabelSelfData(ResMainRecoInfo info) {
        this.info = info;
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
        }else {
            if (info.getArrImg() != null && info.getArrImg().length > 0) {
                if (info.getArrImg().length==1){
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

    public ResMainRecoInfo getInfo() {
        return info;
    }
}
