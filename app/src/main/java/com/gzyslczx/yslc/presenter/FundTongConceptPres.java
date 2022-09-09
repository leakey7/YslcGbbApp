package com.gzyslczx.yslc.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.events.FundConceptIndexEvent;
import com.gzyslczx.yslc.events.FundConceptListEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.modes.response.ResFundFindIndex;
import com.gzyslczx.yslc.modes.response.ResFundFindRank;
import com.gzyslczx.yslc.tools.ConnTool;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

public class FundTongConceptPres {

    private final String TAG = "FundConceptPres";

    private int CurrentPage = 1;

    /*
     * 请求概念指数数据
     * */
    public void RequestConceptIndex(Context context, BaseFragment baseFragment, String code){
        Observable<ResFundFindIndex> observable = FundTongBasePresenter.Create().RequestConceptIndex(context, code, TAG);
        observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        observable.subscribe(new Consumer<ResFundFindIndex>() {
            @Override
            public void accept(ResFundFindIndex res) throws Throwable {
                FundConceptIndexEvent event = new FundConceptIndexEvent(res.isSuccess(), res.getResultObj());
                if (!res.isSuccess()){
                    event.setError(res.getMessage());
                }
                EventBus.getDefault().post(event);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                FundConceptIndexEvent event = new FundConceptIndexEvent(false, null);
                event.setError(context.getString(R.string.NetError));
                EventBus.getDefault().post(event);
            }
        });
    }

    /*
     * 请求概念基金列表
     * */
    public void RequestFundFindList(Context context, BaseFragment baseFragment, String code){
        Observable<ResFundFindRank> observable = FundTongBasePresenter.Create().RequestFundConceptList(context, code, CurrentPage, TAG);
        observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        observable.subscribe(new Consumer<ResFundFindRank>() {
            @Override
            public void accept(ResFundFindRank data) throws Throwable {
                Log.d(TAG, new Gson().toJson(data));
                if (data.isSuccess()){
                    boolean isEnd = false;
                    if (CurrentPage < data.getResultObj().getPageCount()){
                        isEnd = false;
                        CurrentPage++;
                    }else {
                        isEnd = true;
                    }
                    FundConceptListEvent event = new FundConceptListEvent(true, data.getResultObj().getPageInfo());
                    event.setEnd(isEnd);
                    EventBus.getDefault().post(event);
                }else {
                    FundConceptListEvent event = new FundConceptListEvent(false, null);
                    event.setEnd(false);
                    event.setError(data.getMessage());
                    EventBus.getDefault().post(event);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                FundConceptListEvent event = new FundConceptListEvent(false, null);
                event.setError(context.getString(R.string.NetError));
                event.setEnd(false);
                EventBus.getDefault().post(event);
            }
        });
    }

    public void setCurrentPage(int currentPage) {
        CurrentPage = currentPage;
    }



}
