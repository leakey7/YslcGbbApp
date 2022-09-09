package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.adapters.teacher.bean.TeacherSelfData;
import com.gzyslczx.yslc.modes.response.ResTSelfList;

import java.util.ArrayList;
import java.util.List;

public class TeacherSelfListEvent {

    private boolean Flag, IsEnd;
    private List<TeacherSelfData> DataList;
    private String Error, TeacherId;
    private int type;

    public TeacherSelfListEvent(boolean flag, String TName, String THeadImg, String TeacherId, List<ResTSelfList> data, int type) {
        Flag = flag;
        this.TeacherId = TeacherId;
        DataList = new ArrayList<TeacherSelfData>();
        if (data!=null) {
            for (ResTSelfList res : data) {
                DataList.add(new TeacherSelfData(res, TName, THeadImg));
            }
        }
        this.type = type;
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

    public List<TeacherSelfData> getDataList() {
        return DataList;
    }

    public int getType() {
        return type;
    }

    public boolean isEnd() {
        return IsEnd;
    }

    public void setEnd(boolean end) {
        IsEnd = end;
    }

    public String getTeacherId() {
        return TeacherId;
    }
}
