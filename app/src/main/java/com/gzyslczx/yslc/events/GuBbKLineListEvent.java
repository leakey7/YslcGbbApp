package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.adapters.kline.bean.KLineData;
import com.gzyslczx.yslc.modes.response.ResKLineListArt;
import com.gzyslczx.yslc.modes.response.ResKLineListInfo;
import com.gzyslczx.yslc.modes.response.ResKLineListVideo;

import java.util.ArrayList;
import java.util.List;

public class GuBbKLineListEvent {

    private boolean Flag, IsEnd;
    private List<KLineData> DataList;
    private String Error;
    private ResKLineListInfo Info;
    private int CId, ContentType;

    public GuBbKLineListEvent(boolean flag, boolean isLogin, ResKLineListInfo info, int type, int cid) {
        Flag = flag;
        Info = info;
        if (info!=null){
            DataList = new ArrayList<KLineData>();
            if (type==1){
                for (ResKLineListVideo video : info.getVideoList()){
                    DataList.add(new KLineData(isLogin, video));
                }
            }else {
                for (ResKLineListArt art : info.getArtList()) {
                    DataList.add(new KLineData(isLogin, art));
                }
            }
            ContentType = type;
            CId = cid;
        }
    }

    public void setError(String error) {
        Error = error;
    }

    public void setEnd(boolean end) {
        IsEnd = end;
    }

    public boolean isFlag() {
        return Flag;
    }

    public List<KLineData> getDataList() {
        return DataList;
    }

    public String getError() {
        return Error;
    }

    public ResKLineListInfo getInfo() {
        return Info;
    }

    public boolean isEnd() {
        return IsEnd;
    }

    public int getCId() {
        return CId;
    }

    public int getContentType() {
        return ContentType;
    }
}
