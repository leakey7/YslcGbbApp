package com.gzyslczx.yslc.adapters.fundtong.bean;

import com.chad.library.adapter.base.entity.JSectionEntity;

public class ConceptSelectData extends JSectionEntity {

    private boolean isHead = false, isSelect = false;
    private String Word, FundName, Code;

    public ConceptSelectData(boolean isHead, boolean isSelect, String word, String fundName) {
        this.isHead = isHead;
        this.isSelect = isSelect;
        Word = word;
        FundName = fundName;
    }

    @Override
    public boolean isHeader() {
        return isHead;
    }

    public String getFundName() {
        return FundName;
    }

    public String getWord() {
        return Word;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getCode() {
        return Code;
    }
}
