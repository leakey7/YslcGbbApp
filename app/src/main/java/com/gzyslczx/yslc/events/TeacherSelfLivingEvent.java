package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.adapters.teacher.bean.TeacherSelfLivingData;
import com.gzyslczx.yslc.modes.response.ResTSelfLivingListVideo;

import java.util.ArrayList;
import java.util.List;

public class TeacherSelfLivingEvent {

    private boolean Flag, IsEnd=false;
    private List<TeacherSelfLivingData> DataList;
    private String Error;


    public TeacherSelfLivingEvent(boolean flag, String TName, String THeadImg, List<ResTSelfLivingListVideo> data, boolean isEnd) {
        Flag = flag;
        if (data!=null){
            DataList = new ArrayList<TeacherSelfLivingData>();
            for (ResTSelfLivingListVideo video : data){
                DataList.add(new TeacherSelfLivingData(video, TName, THeadImg));
            }
            IsEnd = isEnd;
        }
    }

    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }

    public boolean isFlag() {
        return Flag;
    }

    public List<TeacherSelfLivingData> getDataList() {
        return DataList;
    }

    public boolean isEnd() {
        return IsEnd;
    }
}
