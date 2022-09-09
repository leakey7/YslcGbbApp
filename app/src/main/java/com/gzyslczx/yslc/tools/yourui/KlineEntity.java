package com.gzyslczx.yslc.tools.yourui;

import com.yourui.sdk.message.use.StockKLine;

import java.util.List;


public class KlineEntity {
    private String stockCode;
    private int stockCodeType;
    private List<StockKLine> stockKLineList;

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public int getStockCodeType() {
        return stockCodeType;
    }

    public void setStockCodeType(int stockCodeType) {
        this.stockCodeType = stockCodeType;
    }

    public List<StockKLine> getStockKLineList() {
        return stockKLineList;
    }

    public void setStockKLineList(List<StockKLine> stockKLineList) {
        this.stockKLineList = stockKLineList;
    }

}
