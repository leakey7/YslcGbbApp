package com.gzyslczx.yslc.modes.response;

import java.util.List;

public class ResSearchNorObj {

    private List<ResSearchStock> StockList;
    private List<ResSearchFund> FundList;
    private List<ResSearchAbout> ArtList;

    public List<ResSearchStock> getStockList() {
        return StockList;
    }

    public List<ResSearchFund> getFundList() {
        return FundList;
    }

    public List<ResSearchAbout> getArtList() {
        return ArtList;
    }
}
