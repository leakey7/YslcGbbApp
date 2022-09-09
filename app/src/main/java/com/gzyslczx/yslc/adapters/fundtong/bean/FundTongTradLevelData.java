package com.gzyslczx.yslc.adapters.fundtong.bean;

import com.gzyslczx.yslc.modes.response.ResFundTongTradeLevelObj;

public class FundTongTradLevelData {

    private ResFundTongTradeLevelObj data;
    private boolean isSelect;

    public FundTongTradLevelData(ResFundTongTradeLevelObj data, boolean isSelect) {
        this.data = data;
        this.isSelect = isSelect;
    }

    public ResFundTongTradeLevelObj getData() {
        return data;
    }

    public boolean isSelect() {
        return isSelect;
    }
}
