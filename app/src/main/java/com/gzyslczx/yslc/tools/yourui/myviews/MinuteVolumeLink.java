package com.gzyslczx.yslc.tools.yourui.myviews;

import com.gzyslczx.yslc.tools.yourui.HisTrendExtEntity;
import com.yourui.sdk.message.use.TrendDataModel;

import java.util.List;

public interface MinuteVolumeLink {

    //分时数据联动
    void DataLink(List<TrendDataModel> dataModels, float yesterdayPrice);

    //手势长按-滑动联动
    void LongPressLink(boolean isMove, float moveX, float moveY);

    //五日分时数据联动
    void FiveDataLink(HisTrendExtEntity...his);

}
