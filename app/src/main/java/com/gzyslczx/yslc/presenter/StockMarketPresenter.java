package com.gzyslczx.yslc.presenter;

import android.util.Log;

import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.presenter.yourui.YRBasePresenter;
import com.gzyslczx.yslc.tools.ConnTool;
import com.gzyslczx.yslc.tools.yourui.RequestApi;
import com.yourui.sdk.message.use.Stock;

import java.util.ArrayList;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

public class StockMarketPresenter {

    private final String TAG = "StockMPres";

    /*
    * 请求分时数据
    * */
    public void RequestMinuteChart(BaseActivity baseActivity, Stock stock){
        if (stock.getCodeType()!=-1) {
            ArrayList<Stock> stockArrayList = new ArrayList<Stock>();
            stockArrayList.add(stock);
            Observable<Long> observable = YRBasePresenter.Create().RequestMinuteChart(3);
            observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
            observable.subscribe(new Consumer<Long>() {
                @Override
                public void accept(Long aLong) throws Throwable {
                    RequestApi.getInstance().loadTrend(stock, null);
                    RequestApi.getInstance().loadRealTimeExt(stockArrayList, null);
                }
            });
        }else {
            Log.d(TAG, "匹配不到相应股票类型代码");
        }
    }

    /*
    * 请求日K数据
    * */
    public void RequestDailyChart(Stock stock, short period, short remitMode, int offset){
        YRBasePresenter.Create().RequestDailyChart(stock, period, remitMode, offset);
    }

    /*
    * 请求明细
    * */
    public void RequestMinuteDeal(String TAG, BaseActivity baseActivity, BaseFragment baseFragment, String StockName_Code,
                                  int count, boolean isLoop, int second){
        YRBasePresenter.Create().RequestTick(TAG, baseActivity, baseFragment, StockName_Code, count, isLoop, second);
    }

    /*
    * 请求历史分时线
    * */
    public void RequestHistoryTrend(Stock stock, int time){
        YRBasePresenter.Create().RequestHistoryTrad(stock, time);
    }
    public void RequestHistoryTrendOnLoop(BaseActivity baseActivity, Stock stock, int time){
        Observable<Long> observable = YRBasePresenter.Create().RequestMinuteChart(3);
        observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
        observable.subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Throwable {
                YRBasePresenter.Create().RequestHistoryTrad(stock, time);
            }
        });
    }

}
