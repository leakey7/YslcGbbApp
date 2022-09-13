package com.gzyslczx.yslc.events.yourui;

import com.yourui.sdk.message.use.StockTickDetail;

public class MinuteDealDetailEvent {

    private StockTickDetail stockTickDetail;

    public StockTickDetail getStockTickDetail() {
        return stockTickDetail;
    }

    public void setStockTickDetail(StockTickDetail stockTickDetail) {
        this.stockTickDetail = stockTickDetail;
    }
}
