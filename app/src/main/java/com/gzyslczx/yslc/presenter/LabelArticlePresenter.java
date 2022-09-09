package com.gzyslczx.yslc.presenter;

import android.content.Context;
import android.util.Log;

import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.events.GuBbLabelDetailEvent;
import com.gzyslczx.yslc.modes.response.ResLabelArt;
import com.gzyslczx.yslc.tools.ConnTool;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

public class LabelArticlePresenter {

    private final String TAG = "LArtDetail";

    /*
    * 请求资讯详情
    * */
    public void RequestLabelDetail(Context context, BaseActivity baseActivity, String Nid){
        Observable<ResLabelArt> observable = GuBbBasePresenter.Create().RequestLabelArtDetail(context, Nid, TAG);
        observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
        observable.subscribe(new Consumer<ResLabelArt>() {
            @Override
            public void accept(ResLabelArt res) throws Throwable {
                GuBbLabelDetailEvent event;
                if (res.isSuccess()){
                    event = new GuBbLabelDetailEvent(true, res.getResultObj());
                }else {
                    event = new GuBbLabelDetailEvent(false, null);
                    event.setError(res.getMessage());
                }
                EventBus.getDefault().post(event);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                GuBbLabelDetailEvent event = new GuBbLabelDetailEvent(false, null);
                event.setError(context.getString(R.string.NetError));
                EventBus.getDefault().post(event);
            }
        });
    }

}
