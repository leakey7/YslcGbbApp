package com.gzyslczx.yslc.modes.response;

import java.util.List;

public class ResFundDetailObj {

    private String FCode, name, smallType, parentType, unitNet, date, profitRate,
            weekRate, monthRate, monthRate3, monthRate6, yearRate;
    private boolean Selected;
    private List<ResFundDetailList> ListStock;

    public String getFCode() {
        return FCode;
    }

    public String getName() {
        return name;
    }

    public String getSmallType() {
        return smallType;
    }

    public String getParentType() {
        return parentType;
    }

    public String getUnitNet() {
        return unitNet;
    }

    public String getDate() {
        return date;
    }

    public String getProfitRate() {
        return profitRate;
    }

    public String getWeekRate() {
        return weekRate;
    }

    public String getMonthRate() {
        return monthRate;
    }

    public String getMonthRate3() {
        return monthRate3;
    }

    public String getMonthRate6() {
        return monthRate6;
    }

    public String getYearRate() {
        return yearRate;
    }

    public List<ResFundDetailList> getListStock() {
        return ListStock;
    }

    public boolean isSelected() {
        return Selected;
    }
}
