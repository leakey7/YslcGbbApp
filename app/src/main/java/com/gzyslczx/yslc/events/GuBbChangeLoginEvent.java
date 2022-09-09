package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.modes.response.ResLoginObj;

public class GuBbChangeLoginEvent {

    private boolean IsLogin;
    private String Error, StockCode;
    private ResLoginObj Data;

    public GuBbChangeLoginEvent(boolean isLogin) {
        IsLogin = isLogin;
    }

    public boolean isLogin() {
        return IsLogin;
    }

    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }

    public ResLoginObj getData() {
        return Data;
    }

    public void setData(ResLoginObj data) {
        Data = data;
    }

    public String getStockCode() {
        return StockCode;
    }

    public void setStockCode(String stockCode) {
        StockCode = stockCode;
    }
}
