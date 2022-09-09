package com.gzyslczx.yslc.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.adapters.search.bean.SearchMoreData;
import com.gzyslczx.yslc.events.SearchHistoryEvent;
import com.gzyslczx.yslc.events.SearchMoreEvent;
import com.gzyslczx.yslc.events.SearchNormalEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.modes.response.ResSearchAbout;
import com.gzyslczx.yslc.modes.response.ResSearchArticleMore;
import com.gzyslczx.yslc.modes.response.ResSearchFund;
import com.gzyslczx.yslc.modes.response.ResSearchFundMore;
import com.gzyslczx.yslc.modes.response.ResSearchHis;
import com.gzyslczx.yslc.modes.response.ResSearchNor;
import com.gzyslczx.yslc.modes.response.ResSearchStock;
import com.gzyslczx.yslc.modes.response.ResSearchStockMore;
import com.gzyslczx.yslc.tools.ConnTool;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

public class SearchPresenter {

    private final String TAG = "SearchPresenter";

    /*
     * 请求搜索历史
     * */
    public void RequestHistory(Context context, BaseFragment baseFragment){
        Observable<ResSearchHis> observable = GuBbBasePresenter.Create().RequestSearchHistory(context, TAG);
        if (observable!=null) {
            observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
            observable.subscribe(new Consumer<ResSearchHis>() {
                @Override
                public void accept(ResSearchHis res) throws Throwable {
                    Log.d(TAG, new Gson().toJson(res));
                    SearchHistoryEvent event;
                    if (res.isSuccess()) {
                        event = new SearchHistoryEvent(true, res.getResultObj());
                        EventBus.getDefault().post(event);
                    } else {
                        event = new SearchHistoryEvent(false, null);
                        event.setError(res.getMessage());
                        EventBus.getDefault().post(event);
                    }
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Throwable {
                    Log.d(TAG, throwable.getMessage());
                    SearchHistoryEvent event = new SearchHistoryEvent(false, null);
                    event.setError(context.getString(R.string.NetError));
                    EventBus.getDefault().post(event);
                }
            });
        }else {
            SearchHistoryEvent event = new SearchHistoryEvent(false, null);
            EventBus.getDefault().post(event);
        }
    }

    /*
     * 请求模糊搜索
     * */
    public void RequestNormal(Context context, BaseFragment baseFragment, String keyword){
        Observable<ResSearchNor> observable = GuBbBasePresenter.Create().RequestSearchNormal(context, keyword, TAG);
        observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        observable.subscribe(new Consumer<ResSearchNor>() {
            @Override
            public void accept(ResSearchNor resSearchNor) throws Throwable {
                if (resSearchNor.isSuccess()){
                    SearchNormalEvent event = new SearchNormalEvent(true, resSearchNor.getResultObj());
                    EventBus.getDefault().post(event);
                }else {
                    SearchNormalEvent event = new SearchNormalEvent(false, null);
                    event.setError(resSearchNor.getMessage());
                    EventBus.getDefault().post(event);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                SearchNormalEvent event = new SearchNormalEvent(false, null);
                event.setError(context.getString(R.string.NetError));
                EventBus.getDefault().post(event);
            }
        });
    }

    /*
     * 请求更多股票
     * */
    public void RequestMoreStock(Context context, BaseFragment baseFragment, String keyword){
        Observable<ResSearchStockMore> observable = GuBbBasePresenter.Create().RequestSearchStock(context, keyword, TAG);
        observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        observable.subscribe(new Consumer<ResSearchStockMore>() {
            @Override
            public void accept(ResSearchStockMore data) throws Throwable {
                if (data.isSuccess()){
                    List<SearchMoreData> moreData = new ArrayList<SearchMoreData>();
                    for (ResSearchStock stock : data.getResultObj()){
                        moreData.add(new SearchMoreData(SearchMoreData.StockType, stock));
                    }
                    SearchMoreEvent event = new SearchMoreEvent(true, moreData);
                    EventBus.getDefault().post(event);
                }else {
                    SearchMoreEvent event = new SearchMoreEvent(false, null);
                    event.setError(data.getMessage());
                    EventBus.getDefault().post(event);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                SearchMoreEvent event = new SearchMoreEvent(false, null);
                event.setError(context.getString(R.string.NetError));
                EventBus.getDefault().post(event);
            }
        });
    }

    /*
     * 请求更多基金
     * */
    public void RequestMoreFund(Context context, BaseFragment baseFragment, String keyword){
        Observable<ResSearchFundMore> observable = GuBbBasePresenter.Create().RequestSearchFund(context, keyword, TAG);
        observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        observable.subscribe(new Consumer<ResSearchFundMore>() {
            @Override
            public void accept(ResSearchFundMore data) throws Throwable {
                if (data.isSuccess()){
                    List<SearchMoreData> moreData = new ArrayList<SearchMoreData>();
                    for (ResSearchFund fund : data.getResultObj()){
                        moreData.add(new SearchMoreData(SearchMoreData.FundType, fund));
                    }
                    SearchMoreEvent event = new SearchMoreEvent(true, moreData);
                    EventBus.getDefault().post(event);
                }else {
                    SearchMoreEvent event = new SearchMoreEvent(false, null);
                    event.setError(data.getMessage());
                    EventBus.getDefault().post(event);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                SearchMoreEvent event = new SearchMoreEvent(false, null);
                event.setError(context.getString(R.string.NetError));
                EventBus.getDefault().post(event);
            }
        });
    }

    /*
     * 请求更多文章
     * */
    public void RequestMoreArticle(Context context, BaseFragment baseFragment, String keyword){
        Observable<ResSearchArticleMore> observable = GuBbBasePresenter.Create().RequestSearchArticle(context, keyword, TAG);
        observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        observable.subscribe(new Consumer<ResSearchArticleMore>() {
            @Override
            public void accept(ResSearchArticleMore data) throws Throwable {
                if (data.isSuccess()){
                    List<SearchMoreData> moreData = new ArrayList<SearchMoreData>();
                    for (ResSearchAbout about : data.getResultObj()){
                        moreData.add(new SearchMoreData(SearchMoreData.ArticleType, about));
                    }
                    SearchMoreEvent event = new SearchMoreEvent(true, moreData);
                    EventBus.getDefault().post(event);
                }else {
                    SearchMoreEvent event = new SearchMoreEvent(false, null);
                    event.setError(data.getMessage());
                    EventBus.getDefault().post(event);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                SearchMoreEvent event = new SearchMoreEvent(false, null);
                event.setError(context.getString(R.string.NetError));
                EventBus.getDefault().post(event);
            }
        });
    }

}
