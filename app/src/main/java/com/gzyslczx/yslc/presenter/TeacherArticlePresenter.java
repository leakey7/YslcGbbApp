package com.gzyslczx.yslc.presenter;

import android.content.Context;
import android.util.Log;

import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.events.GuBbTeacherDetailEvent;
import com.gzyslczx.yslc.modes.response.ResTeacherArt;
import com.gzyslczx.yslc.tools.ConnTool;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

public class TeacherArticlePresenter {

    private final String TAG = "TArtPresenter";

    /*
    * 请求资讯详情
    * */
    public void RequestDetail(Context context, BaseActivity baseActivity, String Nid){
        Observable<ResTeacherArt> observable = GuBbBasePresenter.Create().RequestTeacherArtDetail(context, Nid, TAG);
        observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
        observable.subscribe(new Consumer<ResTeacherArt>() {
            @Override
            public void accept(ResTeacherArt res) throws Throwable {
                GuBbTeacherDetailEvent event;
                if (res.isSuccess()){
                    event = new GuBbTeacherDetailEvent(true, res.getResultObj());
                }else {
                    event = new GuBbTeacherDetailEvent(false, null);
                    event.setError(res.getMessage());
                }
                EventBus.getDefault().post(event);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                GuBbTeacherDetailEvent event = new GuBbTeacherDetailEvent(false, null);
                event.setError(context.getString(R.string.NetError));
                EventBus.getDefault().post(event);
            }
        });
    }

}
