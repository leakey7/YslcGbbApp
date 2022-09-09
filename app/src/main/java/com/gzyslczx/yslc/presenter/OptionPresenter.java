package com.gzyslczx.yslc.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.events.DefaultOptionEvent;
import com.gzyslczx.yslc.events.MyOptionEvent;
import com.gzyslczx.yslc.events.NoticeOptionPageChangeEvent;
import com.gzyslczx.yslc.events.OptionDragEvent;
import com.gzyslczx.yslc.events.OptionSetTopEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.modes.response.ResDefaultOption;
import com.gzyslczx.yslc.modes.response.ResJustStrObj;
import com.gzyslczx.yslc.modes.response.ResMyOption;
import com.gzyslczx.yslc.tools.ConnTool;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

public class OptionPresenter {

    private final String TAG = "OptionPresenter";

    /*
    * 请求默认自选股
    * */
    public void RequestDefaultOption(Context context, BaseFragment baseFragment){
        Observable<ResDefaultOption> observable = GuBbBasePresenter.Create().RequestDefOption(context, TAG);
        observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        observable.subscribe(new Consumer<ResDefaultOption>() {
            @Override
            public void accept(ResDefaultOption res) throws Throwable {
                DefaultOptionEvent event = new DefaultOptionEvent(res.isSuccess(), res.getResultObj());
                if (!res.isSuccess()){
                    event.setError(event.getError());
                }
                EventBus.getDefault().post(event);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                DefaultOptionEvent event = new DefaultOptionEvent(false,null);
                event.setError(context.getString(R.string.NetError));
                EventBus.getDefault().post(event);
            }
        });
    }


    /*
    * 请求我的自选股
    * */
    public void RequestMyOption(Context context, BaseFragment baseFragment, boolean needTrun){
        Observable<ResMyOption> observable = GuBbBasePresenter.Create().RequestMyOption(context, TAG);
        if (observable!=null) {
            observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
            observable.subscribe(new Consumer<ResMyOption>() {
                @Override
                public void accept(ResMyOption res) throws Throwable {
                    Log.d(TAG, new Gson().toJson(res));
                    if (res.isSuccess()){
                        MyOptionEvent event = new MyOptionEvent(true, res.getResultObj());
                        EventBus.getDefault().post(event);
                        if (needTrun){
                            NoticeOptionPageChangeEvent changeEvent = new NoticeOptionPageChangeEvent(0);
                            EventBus.getDefault().post(changeEvent);
                        }
                    }else {
                        NoticeOptionPageChangeEvent changeEvent = new NoticeOptionPageChangeEvent(1);
                        EventBus.getDefault().post(changeEvent);
                    }
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Throwable {
                    Log.d(TAG, throwable.getMessage());
                    NoticeOptionPageChangeEvent changeEvent = new NoticeOptionPageChangeEvent(1);
                    EventBus.getDefault().post(changeEvent);
                }
            });
        }else {
            NoticeOptionPageChangeEvent changeEvent = new NoticeOptionPageChangeEvent(1);
            EventBus.getDefault().post(changeEvent);
        }
    }

    /*
     * 请求置顶我的自选股
     * */
    public void RequestSetTopMyOption(Context context, String stockCode, BaseFragment baseFragment, BaseActivity baseActivity){
        Observable<ResJustStrObj> observable = GuBbBasePresenter.Create().RequestSetTopOption(context, stockCode, TAG);
        if (observable!=null) {
            if (baseFragment!=null){
                observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
            }else {
                observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
            }
            observable.subscribe(new Consumer<ResJustStrObj>() {
                @Override
                public void accept(ResJustStrObj res) throws Throwable {
                    OptionSetTopEvent event = new OptionSetTopEvent(res.isSuccess());
                    if (!res.isSuccess()){
                        event.setError(res.getMessage());
                    }
                    EventBus.getDefault().post(event);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Throwable {
                    Log.d(TAG, throwable.getMessage());
                }
            });
        }else {
            NoticeOptionPageChangeEvent changeEvent = new NoticeOptionPageChangeEvent(1);
            EventBus.getDefault().post(changeEvent);
        }
    }

    /*
     * 请求置顶我的自选股
     * */
    public void RequestDragMyOption(Context context, List<String> codes, List<Integer> pos,  BaseActivity baseActivity){
        Observable<ResJustStrObj> observable = GuBbBasePresenter.Create().RequestDragOption(context, codes, pos,  TAG);
        if (observable!=null) {
            observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
            observable.subscribe(new Consumer<ResJustStrObj>() {
                @Override
                public void accept(ResJustStrObj res) throws Throwable {
                    OptionDragEvent event = new OptionDragEvent(res.isSuccess());
                    if (!res.isSuccess()){
                        event.setError(res.getMessage());
                    }
                    EventBus.getDefault().post(event);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Throwable {
                    Log.d(TAG, throwable.getMessage());
                }
            });
        }else {
            NoticeOptionPageChangeEvent changeEvent = new NoticeOptionPageChangeEvent(1);
            EventBus.getDefault().post(changeEvent);
        }
    }
}
