package com.gzyslczx.yslc.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.gzyslczx.yslc.events.GuBbKLineLearnEvent;
import com.gzyslczx.yslc.events.GuBbKLineListEvent;
import com.gzyslczx.yslc.events.GuBbKLinePraiseEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.modes.response.ResJustIntObj;
import com.gzyslczx.yslc.modes.response.ResKLineList;
import com.gzyslczx.yslc.tools.ConnTool;
import com.gzyslczx.yslc.tools.SpTool;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

public class KLineListPresenter {

    private final String TAG = "KLListPres";
    private int CurrentPage = 1;

    public void setCurrentPage(int currentPage) {
        CurrentPage = currentPage;
    }

    /*
     * 请求K线秘籍列表
     * */
    public void RequestKLineArtList(Context context, BaseFragment baseFragment, int Cid, int type){
        Observable<ResKLineList> observable = GuBbBasePresenter.Create().RequestKLineArtList(context, Cid, CurrentPage, TAG);
        observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        observable.subscribe(new Consumer<ResKLineList>() {
            @Override
            public void accept(ResKLineList res) throws Throwable {
                boolean isLogin = SpTool.Instance(context).IsGuBbLogin();
                boolean isEnd=false;
                if (res.isSuccess()){
                    if (res.getResultObj().getCurrentPage() >= res.getResultObj().getPageCount()){
                        isEnd = true;
                    }else {
                        isEnd = false;
                        CurrentPage++;
                    }
                    GuBbKLineListEvent event = new GuBbKLineListEvent(true, isLogin, res.getResultObj().getPageInfo(), type, Cid);
                    event.setEnd(isEnd);
                    EventBus.getDefault().post(event);
                }else {
                    GuBbKLineListEvent event = new GuBbKLineListEvent(true, isLogin, null, type, Cid);
                    event.setError(res.getMessage());
                    event.setEnd(isEnd);
                    EventBus.getDefault().post(event);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());

            }
        });
    }

    /*
     * 请求K线秘籍列表
     * */
    public void RequestKLineVideoList(Context context, BaseFragment baseFragment, int type, int cid){
        Observable<ResKLineList> observable = GuBbBasePresenter.Create().RequestKLineVideoList(context, TAG);
        observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        observable.subscribe(new Consumer<ResKLineList>() {
            @Override
            public void accept(ResKLineList res) throws Throwable {
                Log.d(TAG, new Gson().toJson(res.getResultObj().getPageInfo().getVideoList()));
                boolean isLogin = SpTool.Instance(context).IsGuBbLogin();
                boolean isEnd=false;
                if (res.isSuccess()){
                    if (res.getResultObj().getCurrentPage() >= res.getResultObj().getPageCount()){
                        isEnd = true;
                    }else {
                        isEnd = false;
                        CurrentPage++;
                    }
                    GuBbKLineListEvent event = new GuBbKLineListEvent(true, isLogin, res.getResultObj().getPageInfo(), type, cid);
                    event.setEnd(isEnd);
                    EventBus.getDefault().post(event);
                }else {
                    GuBbKLineListEvent event = new GuBbKLineListEvent(true, isLogin, null, type, cid);
                    event.setError(res.getMessage());
                    event.setEnd(isEnd);
                    EventBus.getDefault().post(event);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());

            }
        });
    }

    /*
    * K线秘籍请求学习或点赞
    * state 0=学习 1=点赞
    * */
    public void RequestKLineLearnOrPraise(Context context, BaseFragment baseFragment, int id, int state, int typeInfo){
        Observable<ResJustIntObj> observable = GuBbBasePresenter.Create().RequestKLineLearnPraise(context, id, state, typeInfo, TAG);
        observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        observable.subscribe(new Consumer<ResJustIntObj>() {
            @Override
            public void accept(ResJustIntObj res) throws Throwable {
                if (state==0){
                    //学习
                    GuBbKLineLearnEvent event = new GuBbKLineLearnEvent(res.isSuccess(), id);
                    if (!res.isSuccess()){
                        event.setError(res.getMessage());
                    }
                    EventBus.getDefault().post(event);
                    return;
                }
                if (state==1){
                    //点赞
                    GuBbKLinePraiseEvent event = new GuBbKLinePraiseEvent(res.isSuccess(), res.getResultObj()==1, id, res.getCount());
                    if (!res.isSuccess()){
                        event.setError(res.getMessage());
                    }
                    EventBus.getDefault().post(event);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
            }
        });
    }

}
