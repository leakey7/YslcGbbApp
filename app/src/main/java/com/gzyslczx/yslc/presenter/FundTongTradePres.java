package com.gzyslczx.yslc.presenter;

import android.content.Context;
import android.util.Log;

import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.events.FundTongTradeIndexEvent;
import com.gzyslczx.yslc.events.FundTongTradeLevelEvent;
import com.gzyslczx.yslc.events.FundTradeListEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.modes.response.ResFundFindIndex;
import com.gzyslczx.yslc.modes.response.ResFundFindRank;
import com.gzyslczx.yslc.modes.response.ResFundTongTradeLevel;
import com.gzyslczx.yslc.tools.ConnTool;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

public class FundTongTradePres {

    private final String TAG = "FundTradePres";
    private int CurrentPage = 1;


    /*
     * 请求分级列表
     * */
    public void RequestLevelList(Context context, BaseFragment baseFragment, int Level, String ParentCode){
        Observable<ResFundTongTradeLevel> observable = FundTongBasePresenter.Create().RequestTradeLevelList(context, Level, ParentCode, TAG);
        observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        observable.subscribe(new Consumer<ResFundTongTradeLevel>() {
            @Override
            public void accept(ResFundTongTradeLevel res) throws Throwable {
                FundTongTradeLevelEvent event = new FundTongTradeLevelEvent(res.isSuccess(), res.getResultObj(), Level);
                if (!res.isSuccess()){
                    event.setError(res.getMessage());
                }
                EventBus.getDefault().post(event);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                FundTongTradeLevelEvent event = new FundTongTradeLevelEvent(false, null, Level);
                event.setError(context.getString(R.string.NetError));
                EventBus.getDefault().post(event);
            }
        });
    }

    /*
     * 请求行业指数数据
     * */
    public void RequestTradeIndex(Context context, BaseFragment baseFragment, String code){
        Observable<ResFundFindIndex> observable = FundTongBasePresenter.Create().RequestTradeIndex(context, code, TAG);
        observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        observable.subscribe(new Consumer<ResFundFindIndex>() {
            @Override
            public void accept(ResFundFindIndex res) throws Throwable {
                FundTongTradeIndexEvent event = new FundTongTradeIndexEvent(res.isSuccess(), res.getResultObj());
                if (!res.isSuccess()){
                    event.setError(res.getMessage());
                }
                EventBus.getDefault().post(event);

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                FundTongTradeIndexEvent event = new FundTongTradeIndexEvent(false, null);
                event.setError(context.getString(R.string.NetError));
                EventBus.getDefault().post(event);
            }
        });
    }


    /*
     * 请求行业基金列表
     * */
    public void RequestFundFindList(Context context, BaseFragment baseFragment, String code){
        Observable<ResFundFindRank> observable = FundTongBasePresenter.Create().RequestFundTradeList(context, code, CurrentPage, TAG);
        observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        observable.subscribe(new Consumer<ResFundFindRank>() {
            @Override
            public void accept(ResFundFindRank data) throws Throwable {
                if (data.isSuccess()){
                    boolean isEnd = false;
                    if (CurrentPage < data.getResultObj().getPageCount()){
                        isEnd = false;
                        CurrentPage++;
                    }else {
                        isEnd = true;
                    }
                    FundTradeListEvent event = new FundTradeListEvent(true,data.getResultObj().getPageInfo());
                    event.setEnd(isEnd);
                    EventBus.getDefault().post(event);
                }else {
                    FundTradeListEvent event = new FundTradeListEvent(false,null);
                    event.setEnd(false);
                    event.setError(data.getMessage());
                    EventBus.getDefault().post(event);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                FundTradeListEvent event = new FundTradeListEvent(false, null);
                event.setEnd(false);
                event.setError(context.getString(R.string.NetError));
                EventBus.getDefault().post(event);
            }
        });
    }

    public void setCurrentPage(int currentPage) {
        CurrentPage = currentPage;
    }

}
