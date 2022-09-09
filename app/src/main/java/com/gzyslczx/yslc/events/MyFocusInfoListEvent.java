package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.adapters.main.bean.MainRecoData;

import java.util.List;

public class MyFocusInfoListEvent {

    private boolean Flag, IsEnd, IsNoData=false;
    private String Error;
    private List<MainRecoData> DataList;

    public MyFocusInfoListEvent(boolean flag, boolean isEnd, List<MainRecoData> dataList) {
        Flag = flag;
        IsEnd = isEnd;
        DataList = dataList;
    }

    public void setError(String error) {
        Error = error;
    }

    public boolean isEnd() {
        return IsEnd;
    }

    public boolean isFlag() {
        return Flag;
    }

    public String getError() {
        return Error;
    }

    public List<MainRecoData> getDataList() {
        return DataList;
    }

    public boolean isNoData() {
        return IsNoData;
    }

    public void setNoData(boolean noData) {
        IsNoData = noData;
    }
}
