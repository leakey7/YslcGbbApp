package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.modes.response.ResMsgInfoListInfo;

import java.util.List;

public class MsgInfoListEvent {

    private boolean Flag, IsEnd;
    private List<ResMsgInfoListInfo> DataList;
    private String Error;

    public MsgInfoListEvent(boolean flag, List<ResMsgInfoListInfo> dataList) {
        Flag = flag;
        DataList = dataList;
    }

    public void setError(String error) {
        Error = error;
    }

    public boolean isFlag() {
        return Flag;
    }

    public List<ResMsgInfoListInfo> getDataList() {
        return DataList;
    }

    public String getError() {
        return Error;
    }

    public boolean isEnd() {
        return IsEnd;
    }

    public void setEnd(boolean end) {
        IsEnd = end;
    }
}
