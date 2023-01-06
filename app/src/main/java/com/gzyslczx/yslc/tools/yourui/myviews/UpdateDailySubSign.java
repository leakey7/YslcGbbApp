package com.gzyslczx.yslc.tools.yourui.myviews;

import com.yourui.sdk.message.kline.KlineASI;
import com.yourui.sdk.message.kline.KlineBIAS;
import com.yourui.sdk.message.kline.KlineBOLL;
import com.yourui.sdk.message.kline.KlineKDJ;
import com.yourui.sdk.message.kline.KlineMACD;
import com.yourui.sdk.message.kline.KlineRSI;
import com.yourui.sdk.message.kline.KlineVR;
import com.yourui.sdk.message.kline.KlineWR;
import com.yourui.sdk.message.use.StockKLine;

public interface UpdateDailySubSign {

    void onChangeSubSign(int type, KlineKDJ klineKDJ, KlineMACD klineMACD, StockKLine klineVOL, KlineBOLL klineBOLL,
                         KlineASI klineASI, KlineWR klineWR, KlineBIAS klineBIAS, KlineRSI klineRSI, KlineVR klineVR);

}
