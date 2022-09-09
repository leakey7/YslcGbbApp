package com.gzyslczx.yslc.tools.yourui.myviews;

import com.yourui.sdk.message.use.StockKLine;

import java.util.List;

public interface DailyVolumeLink {

    //数据联动
    void DataLink(List<StockKLine> dataModels);

    //手势长按-滑动联动
    void LongPressLink(boolean isMove, float moveX, float moveY);

    //缩放联动
    void ScaleLink(boolean invalidate, int itemSize, int markScaleIndex);

    //平移滑动联动
    void MoveLink(float moveDistance, int markScaleIndex);

    //长按数据显示联动
    void ShowDataOnLongPress(int targetIndex);

}
