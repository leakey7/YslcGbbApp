package com.gzyslczx.yslc.presenter;

import android.content.Context;
import android.util.Log;

import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.events.GuBbTeacherAllEvent;
import com.gzyslczx.yslc.modes.response.ResTeacherAll;
import com.gzyslczx.yslc.tools.ConnTool;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

public class GuBbTeacherAllPresenter {

    private final String TAG = "TAllPresenter";

    /*
    * 请求名师专栏
    * */
    public void RequestTeacherAllList(Context context, BaseActivity baseActivity){
        Observable<ResTeacherAll> observable = GuBbBasePresenter.Create().RequestTeacherAllList(context, TAG);
        observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
        observable.subscribe(new Consumer<ResTeacherAll>() {
            @Override
            public void accept(ResTeacherAll res) throws Throwable {
                GuBbTeacherAllEvent event;
                if (res.isSuccess()){
                    event = new GuBbTeacherAllEvent(true, res.getResultObj());
                }else {
                    event = new GuBbTeacherAllEvent(false, null);
                    event.setError(res.getMessage());
                }
                EventBus.getDefault().post(event);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                GuBbTeacherAllEvent event = new GuBbTeacherAllEvent(false, null);
                event.setError(context.getString(R.string.NetError));
                EventBus.getDefault().post(event);
            }
        });
    }

}
