package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.adapters.fundtong.bean.ConceptSelectData;

import java.util.List;

public class FundConceptSelectListEvent {

    private boolean Flag;
    private String Error;
    private List<ConceptSelectData> DataList;
    private List<String> WordList;

    public FundConceptSelectListEvent(boolean flag, List<String> WordList, List<ConceptSelectData> dataList) {
        this.WordList = WordList;
        Flag = flag;
        DataList = dataList;
    }

    public void setError(String error) {
        Error = error;
    }

    public List<String> getWordList() {
        return WordList;
    }

    public boolean isFlag() {
        return Flag;
    }

    public String getError() {
        return Error;
    }

    public List<ConceptSelectData> getDataList() {
        return DataList;
    }
}
