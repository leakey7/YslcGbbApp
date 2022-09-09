package com.gzyslczx.yslc.tools.yourui;

import com.yourui.sdk.message.use.TrendDataModel;

import java.util.List;

/**
 * 项目名称：youruitougu
 * 类描述：
 * 创建人：zhouY
 * 创建时间：2021/2/25
 * 修改人：zhouY
 * 修改时间：2021/2/25
 * 修改备注：
 */
public class TrendExtEntity {

    private float preClosePrice;
    private  double mNewPrice;
    private String mPriceChange;
    private String mPriceChangePercent;
    private float mTotalAmount;
    private float mMaxPrice;
    private float mMinPrice;
    private List<TrendDataModel> trendDataModelList;
    private String stockCode;

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public double getmNewPrice() {
        return mNewPrice;
    }

    public void setmNewPrice(double mNewPrice) {
        this.mNewPrice = mNewPrice;
    }

    public float getMaxPrice() {
        return mMaxPrice;
    }

    public void setMaxPrice(float mMaxPrice) {
        this.mMaxPrice = mMaxPrice;
    }

    public float getMinPrice() {
        return mMinPrice;
    }

    public void setMinPrice(float mMinPrice) {
        this.mMinPrice = mMinPrice;
    }

    public float getTotalAmount() {
        return mTotalAmount;
    }

    public void setTotalAmount(float mTotalAmount) {
        this.mTotalAmount = mTotalAmount;
    }

    public float getPreClosePrice() {
        return preClosePrice;
    }

    public void setPreClosePrice(float preClosePrice) {
        this.preClosePrice = preClosePrice;
    }

    public List<TrendDataModel> getTrendDataModelList() {
        return trendDataModelList;
    }

    public void setTrendDataModelList(List<TrendDataModel> trendDataModelList) {
        this.trendDataModelList = trendDataModelList;
    }


    public double getNewPrice() {
        return mNewPrice;
    }

    public void setNewPrice(double mNewPrice) {
        this.mNewPrice = mNewPrice;
    }

    public String getPriceChange() {
        return mPriceChange;
    }

    public void setPriceChange(String mPriceChange) {
        this.mPriceChange = mPriceChange;
    }

    public String getPriceChangePercent() {
        return mPriceChangePercent;
    }

    public void setPriceChangePercent(String mPriceChangePercent) {
        this.mPriceChangePercent = mPriceChangePercent;
    }
}
