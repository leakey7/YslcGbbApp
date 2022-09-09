package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.modes.response.ResMainFinancingBuy;
import com.gzyslczx.yslc.modes.response.ResMainInstitutionBuy;
import com.gzyslczx.yslc.modes.response.ResMainNorthBuy;
import com.gzyslczx.yslc.modes.response.ResMainNorthBuyObj;

public class GuBbMainMovementEvent {

    private boolean Flag;
    private int ItemType;
    private ResMainNorthBuy northBuy;
    private ResMainInstitutionBuy institutionBuy;
    private ResMainFinancingBuy financingBuy;
    private String Error;


    public GuBbMainMovementEvent(boolean flag, int itemType) {
        Flag = flag;
        ItemType = itemType;
    }

    public ResMainNorthBuy getNorthBuy() {
        return northBuy;
    }

    public void setNorthBuy(ResMainNorthBuy northBuy) {
        this.northBuy = northBuy;
    }

    public ResMainInstitutionBuy getInstitutionBuy() {
        return institutionBuy;
    }

    public void setInstitutionBuy(ResMainInstitutionBuy institutionBuy) {
        this.institutionBuy = institutionBuy;
    }

    public ResMainFinancingBuy getFinancingBuy() {
        return financingBuy;
    }

    public void setFinancingBuy(ResMainFinancingBuy financingBuy) {
        this.financingBuy = financingBuy;
    }

    public boolean isFlag() {
        return Flag;
    }

    public int getItemType() {
        return ItemType;
    }

    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }
}



