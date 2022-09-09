package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.adapters.teacher.bean.TeacherAllData;
import com.gzyslczx.yslc.modes.response.ResTeacherAllObj;

import java.util.ArrayList;
import java.util.List;

public class GuBbTeacherAllEvent {

    private boolean Flag;
    private List<TeacherAllData> DataList;
    private String Error;

    public GuBbTeacherAllEvent(boolean flag, List<ResTeacherAllObj> objList) {
        Flag = flag;
        if (objList!=null){
            DataList = new ArrayList<TeacherAllData>();
            for (ResTeacherAllObj obj : objList){
                DataList.add(new TeacherAllData(obj));
            }
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

    public List<TeacherAllData> getDataList() {
        return DataList;
    }
}
