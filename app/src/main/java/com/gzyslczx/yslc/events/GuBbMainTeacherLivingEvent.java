package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.adapters.main.bean.MainTeacherLivingData;
import com.gzyslczx.yslc.modes.response.ResMainTeacherLivingObj;

import java.util.ArrayList;
import java.util.List;

public class GuBbMainTeacherLivingEvent {

    private List<MainTeacherLivingData> LivingData;
    private boolean Flag;
    private String Error;

    public GuBbMainTeacherLivingEvent(List<ResMainTeacherLivingObj> data, boolean flag) {
        LivingData = new ArrayList<MainTeacherLivingData>();
        for (ResMainTeacherLivingObj obj : data){
            switch (obj.getCurrentstate()){
                case 0:
                    //未开始
                    LivingData.add(new MainTeacherLivingData(MainTeacherLivingData.OnSub, obj));
                    break;
                case 1:
                    //直播中
                    LivingData.add(new MainTeacherLivingData(MainTeacherLivingData.OnLiving, obj));
                    break;
                case 3:
                    //录播
                    LivingData.add(new MainTeacherLivingData(MainTeacherLivingData.PlayBack, obj));
                    break;
            }
        }
        Flag = flag;
    }

    public List<MainTeacherLivingData> getLivingData() {
        return LivingData;
    }

    public boolean isFlag() {
        return Flag;
    }

    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }
}
