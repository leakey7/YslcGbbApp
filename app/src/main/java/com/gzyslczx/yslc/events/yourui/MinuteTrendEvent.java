package com.gzyslczx.yslc.events.yourui;

import com.gzyslczx.yslc.tools.yourui.TrendExtEntity;
import com.yourui.sdk.message.use.TrendDataModel;

import java.util.List;

public class MinuteTrendEvent {

    private TrendExtEntity trendExtEntity;

    public MinuteTrendEvent(TrendExtEntity trendExtEntity) {
        this.trendExtEntity = trendExtEntity;
    }

    public TrendExtEntity getTrendExtEntity() {
        return trendExtEntity;
    }
}
