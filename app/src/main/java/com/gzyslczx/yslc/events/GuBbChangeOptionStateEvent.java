package com.gzyslczx.yslc.events;

public class GuBbChangeOptionStateEvent {

    private boolean Flag;
    private String[] StockCodes;
    private String Error;
    private boolean IsAddOption;

    public GuBbChangeOptionStateEvent(boolean flag, String[] stockCodes, boolean isAddOption) {
        Flag = flag;
        StockCodes = stockCodes;
        IsAddOption = isAddOption;
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

    public String[] getStockCodes() {
        return StockCodes;
    }

    public boolean isAddOption() {
        return IsAddOption;
    }
}
