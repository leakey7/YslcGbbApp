package com.gzyslczx.yslc.adapters.search.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.gzyslczx.yslc.modes.response.ResSearchAbout;
import com.gzyslczx.yslc.modes.response.ResSearchFund;
import com.gzyslczx.yslc.modes.response.ResSearchStock;

public class SearchMoreData implements MultiItemEntity {

    public final static int StockType = 1;
    public final static int FundType = 2;
    public final static int ArticleType = 3;
    private int ItemType;
    private ResSearchStock StockData;
    private ResSearchFund FundData;
    private ResSearchAbout ArtData;

    public SearchMoreData(int itemType, ResSearchStock stockData) {
        ItemType = itemType;
        StockData = stockData;
    }

    public SearchMoreData(int itemType, ResSearchFund fundData) {
        ItemType = itemType;
        FundData = fundData;
    }

    public SearchMoreData(int itemType, ResSearchAbout artData) {
        ItemType = itemType;
        ArtData = artData;
    }

    @Override
    public int getItemType() {
        return ItemType;
    }

    public ResSearchStock getStockData() {
        return StockData;
    }

    public ResSearchFund getFundData() {
        return FundData;
    }

    public ResSearchAbout getArtData() {
        return ArtData;
    }

}
