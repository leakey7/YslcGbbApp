package com.gzyslczx.yslc.presenter.yourui;

import static com.gzyslczx.yslc.tools.ConnTool.AddExtraReqOfAct;
import static com.gzyslczx.yslc.tools.ConnTool.AddRetryReq;

import android.util.Log;

import com.google.gson.Gson;
import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.events.yourui.YRTokenEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.modes.yourui.ReqToken;
import com.gzyslczx.yslc.tools.ConnTool;
import com.gzyslczx.yslc.tools.SpTool;
import com.gzyslczx.yslc.tools.yourui.RequestApi;
import com.gzyslczx.yslc.tools.yourui.YRConnMode;
import com.gzyslczx.yslc.tools.yourui.YRConnUnit;
import com.gzyslczx.yslc.tools.yourui.YRTokenRes;
import com.yourui.sdk.message.api.protocol.QuoteConstants;
import com.yourui.sdk.message.entity.CodeInfo;
import com.yourui.sdk.message.entity.ReqLimitTick;
import com.yourui.sdk.message.kline.KlineASI;
import com.yourui.sdk.message.kline.KlineBIAS;
import com.yourui.sdk.message.kline.KlineBOLL;
import com.yourui.sdk.message.kline.KlineKDJ;
import com.yourui.sdk.message.kline.KlineMACD;
import com.yourui.sdk.message.kline.KlineRSI;
import com.yourui.sdk.message.kline.KlineVOL;
import com.yourui.sdk.message.kline.KlineVR;
import com.yourui.sdk.message.kline.KlineWR;
import com.yourui.sdk.message.use.Stock;
import com.yourui.sdk.message.use.StockKLine;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class YRBasePresenter {

    private static volatile YRBasePresenter mBase;
    private YRConnMode mReqBase;

    private YRBasePresenter() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .baseUrl(YRConnUnit.MainConn)
                .build();
        mReqBase = retrofit.create(YRConnMode.class);
    }

    public static YRBasePresenter Create() {
        if (mBase == null) {
            synchronized (YRBasePresenter.class) {
                if (mBase == null) {
                    mBase = new YRBasePresenter();
                }
            }
        }
        return mBase;
    }

    private void PrintLogD(String log){
        Log.d(getClass().getSimpleName(), log);
    }

    /*
     * 轮询-数据
     * */
    public Observable<Long> RequestMinuteChart(long second){
        Observable<Long> observable = Observable.interval(0, second, TimeUnit.SECONDS);
        return observable;
    }

    /*
     * 分笔-明细
     * */
    public void RequestTick(String TAG, BaseActivity baseActivity, BaseFragment baseFragment, String name_code, int count, boolean isLoop, int second){
        ReqLimitTick reqLimitTick = new ReqLimitTick();
        CodeInfo codeInfo = new CodeInfo(name_code);
        reqLimitTick.setCodeInfo(codeInfo);
        if (count!=-1) {
            reqLimitTick.setCount(count);
        }
        if (isLoop) {
            Observable<Long> observable = YRBasePresenter.Create().RequestMinuteChart(second);
            if (baseActivity != null) {
                observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
            } else if (baseFragment != null) {
                observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
            }
            observable.subscribe(new Consumer<Long>() {
                @Override
                public void accept(Long aLong) throws Throwable {
                    RequestApi.getInstance().loadLimitTick(reqLimitTick, null);
                }
            });
        }else {
            RequestApi.getInstance().loadLimitTick(reqLimitTick, null);
        }
    }


    /*
     * 日K图数据
     * */
    public void RequestDailyChart(Stock stock, short period, short remitMode, int offset){
        RequestApi.getInstance().loadKLine(stock, period, remitMode, offset, 250, 0, 0, null);
    }

    /*
    * 请求Token
    * */
    public void RequestToke(BaseActivity baseActivity){
        ReqToken req = new ReqToken();
        Observable<YRTokenRes> observable = mReqBase.RequestToken(YRConnUnit.ORIGINVALUE, req);
        observable = AddRetryReq(observable, getClass().getSimpleName());
        observable = AddExtraReqOfAct(observable, getClass().getSimpleName(), baseActivity);
        observable.subscribe(new Consumer<YRTokenRes>() {
            @Override
            public void accept(YRTokenRes yrTokenRes) throws Throwable {
                if (yrTokenRes.isSuccess()){
                    if (SpTool.Instance(baseActivity).SaveInfo(SpTool.YRToken, yrTokenRes.getToken())){
                        YRTokenEvent event = new YRTokenEvent();
                        event.setUpdate(true);
                        event.setToken(yrTokenRes.getToken());
                        EventBus.getDefault().post(event);
                    }
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                PrintLogD(throwable.getMessage());
            }
        });
    }

    /*
    * K线成交量
    * */
    public KlineVOL GetKLineVol(List<StockKLine> stockKLineList){
        return new KlineVOL(stockKLineList);
    }

    /*
    * K线KDJ
    * */
    public KlineKDJ GetKLineKDJ(List<StockKLine> stockKLineList){
        return new KlineKDJ(stockKLineList);
    }

    /*
     * K线MACD
     * */
    public KlineMACD GetKLineMACD(List<StockKLine> stockKLineList){
        return new KlineMACD(stockKLineList);
    }

    /*
     * K线BOLL
     * */
    public KlineBOLL GetKLineBOLL(List<StockKLine> stockKLineList){
        return new KlineBOLL(stockKLineList);
    }

    /*
     * K线ASI
     * */
    public KlineASI GetKLineASI(List<StockKLine> stockKLineList){
        return new KlineASI(stockKLineList);
    }

    /*
     * K线WR
     * */
    public KlineWR GetKLineWR(List<StockKLine> stockKLineList){
        return new KlineWR(stockKLineList);
    }

    /*
     * K线BIAS
     * */
    public KlineBIAS GetKLineBIAS(List<StockKLine> stockKLineList){
        return new KlineBIAS(stockKLineList);
    }

    /*
     * K线RSI
     * */
    public KlineRSI GetKLineRSI(List<StockKLine> stockKLineList){
        return new KlineRSI(stockKLineList);
    }

    /*
     * K线VR
     * */
    public KlineVR GetKLineVR(List<StockKLine> stockKLineList){
        return new KlineVR(stockKLineList);
    }

}
