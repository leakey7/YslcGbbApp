package com.gzyslczx.yslc.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.events.TeacherSelfInfoEvent;
import com.gzyslczx.yslc.events.TeacherSelfListEvent;
import com.gzyslczx.yslc.events.TeacherSelfLivingEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.modes.response.ResTSelf;
import com.gzyslczx.yslc.modes.response.ResTSelfLivingList;
import com.gzyslczx.yslc.tools.ConnTool;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

public class TeacherSelfPresenter {

    private final String TAG = "TeacherSelfPres";

    /*
    * 请求名师个人页信息
    * */
    public void RequestTeacherSelf(Context context, BaseFragment baseFragment, BaseActivity baseActivity, int type, int CurrentPage, String TId,
                                   boolean InitTInfo){
        Observable<ResTSelf> observable = GuBbBasePresenter.Create().RequestTeacherSelf(context, type, CurrentPage, TId, TAG);
        if (baseFragment != null){
            observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        }else {
            observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
        }
        observable.subscribe(new Consumer<ResTSelf>() {
            @Override
            public void accept(ResTSelf res) throws Throwable {
                if (res.isSuccess()) {
                    if (InitTInfo) {
                        //初始化名师个人信息
                        TeacherSelfInfoEvent infoEvent = new TeacherSelfInfoEvent(true, res.getResultObj().getPageInfo());
                        EventBus.getDefault().post(infoEvent);
                    }else {
                        TeacherSelfListEvent listEvent =new TeacherSelfListEvent(true, res.getResultObj().getPageInfo().getName(),
                                res.getResultObj().getPageInfo().getImg(), res.getResultObj().getPageInfo().getId(),
                                res.getResultObj().getPageInfo().getArtList(), type);
                        listEvent.setEnd(res.getResultObj().getCurrentPage() >= res.getResultObj().getPageCount());
                        EventBus.getDefault().post(listEvent);
                    }
                }else {
                    if (InitTInfo){
                        TeacherSelfInfoEvent infoEvent = new TeacherSelfInfoEvent(false, null);
                        infoEvent.setError(res.getMessage());
                        EventBus.getDefault().post(infoEvent);
                    }else {
                        TeacherSelfListEvent listEvent =new TeacherSelfListEvent(false, null, null, null, null, type);
                        listEvent.setEnd(false);
                        listEvent.setError(res.getMessage()+"\n可下拉刷新");
                        EventBus.getDefault().post(listEvent);
                    }
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                if (InitTInfo){
                    TeacherSelfInfoEvent infoEvent = new TeacherSelfInfoEvent(false, null);
                    infoEvent.setError(type+throwable.getMessage());
                    EventBus.getDefault().post(infoEvent);
                }else {
                    TeacherSelfListEvent listEvent =new TeacherSelfListEvent(false, null, null, null, null, type);
                    listEvent.setEnd(false);
                    listEvent.setError("网络不给力\n下拉刷新重试");
                    EventBus.getDefault().post(listEvent);
                }
            }
        });
    }

    /*
    * 请求名师个人直播列
    * */
    public void RequestLivingList(Context context, BaseFragment baseFragment, BaseActivity baseActivity, int CurrentPage, String TId){
        Observable<ResTSelfLivingList> observable = GuBbBasePresenter.Create().RequestTeacherSelfLivingList(context, CurrentPage, TId, TAG);
        if (baseFragment != null){
            observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        }else {
            observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
        }
        observable.subscribe(new Consumer<ResTSelfLivingList>() {
            @Override
            public void accept(ResTSelfLivingList res) throws Throwable {
                if (res.isSuccess()){
                    TeacherSelfLivingEvent event = new TeacherSelfLivingEvent(true, res.getResultObj().getPageInfo().getName(),
                            res.getResultObj().getPageInfo().getImg(), res.getResultObj().getPageInfo().getVideoList(),
                            res.getResultObj().getCurrentPage() >= res.getResultObj().getPageCount());
                    EventBus.getDefault().post(event);
                }else {
                    TeacherSelfLivingEvent event = new TeacherSelfLivingEvent(false, null, null, null, false);
                    event.setError(res.getMessage()+"\n可下拉刷新");
                    EventBus.getDefault().post(event);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                TeacherSelfLivingEvent event = new TeacherSelfLivingEvent(false, null, null, null, false);
                event.setError("网络不给力\n下拉刷新重试");
                EventBus.getDefault().post(event);
            }
        });


    }


}
