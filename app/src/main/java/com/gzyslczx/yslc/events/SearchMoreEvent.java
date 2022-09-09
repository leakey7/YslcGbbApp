package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.adapters.search.bean.SearchMoreData;

import java.util.List;

public class SearchMoreEvent {

    private boolean Flag;
    private List<SearchMoreData> DataList;
    private String Error;

    public SearchMoreEvent(boolean flag, List<SearchMoreData> dataList) {
        Flag = flag;
        DataList = dataList;
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

    public List<SearchMoreData> getDataList() {
        return DataList;
    }
}
