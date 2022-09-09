package com.gzyslczx.yslc.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.events.MsgBoxStyleEvent;
import com.gzyslczx.yslc.modes.response.ResMsgBox;
import com.gzyslczx.yslc.tools.ConnTool;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

public class MsgBoxPresenter {

    private final String TAG = "MsgBoxPres";


    /*
    * 请求消息盒子类型
    * */
    public void RequestMsgBoxStyle(Context context, BaseActivity baseActivity){
        Observable<ResMsgBox> observable = GuBbBasePresenter.Create().RequestMsgBoxStyle(context, TAG);
        observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
        observable.subscribe(new Consumer<ResMsgBox>() {
            @Override
            public void accept(ResMsgBox res) throws Throwable {
                if (res.isSuccess()){
                    MsgBoxStyleEvent event = new MsgBoxStyleEvent(true, res.getResultObj());
                    EventBus.getDefault().post(event);
                }else {
                    MsgBoxStyleEvent event = new MsgBoxStyleEvent(false, null);
                    event.setError(res.getMessage());
                    EventBus.getDefault().post(event);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                MsgBoxStyleEvent event = new MsgBoxStyleEvent(false, null);
                event.setError(context.getString(R.string.NetError));
                EventBus.getDefault().post(event);
            }
        });
    }

}
