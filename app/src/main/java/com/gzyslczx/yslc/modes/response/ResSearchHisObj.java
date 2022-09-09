package com.gzyslczx.yslc.modes.response;

import java.util.List;

public class ResSearchHisObj {

    private List<ResSearchStockHis> StockList;
    private List<ResSearchFundHis> FundList;

    public ResSearchHisObj(List<ResSearchStockHis> stockList, List<ResSearchFundHis> fundList) {
        StockList = stockList;
        FundList = fundList;
    }

    public List<ResSearchStockHis> getStockList() {
        return StockList;
    }

    public List<ResSearchFundHis> getFundList() {
        return FundList;
    }
}
