package com.gzyslczx.yslc.presenter;

import android.content.Context;
import android.util.Log;

import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.events.MineInfoEvent;
import com.gzyslczx.yslc.events.NewVersionEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.modes.response.ResMineInfo;
import com.gzyslczx.yslc.modes.response.ResNewVersion;
import com.gzyslczx.yslc.tools.ConnTool;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

public class MinePresenter {

    private final String TAG = "MinePresenter";


    /*
    * 请求我的信息
    * */
    public void RequestMineInfo(Context context, BaseFragment baseFragment){
        Observable<ResMineInfo> observable = GuBbBasePresenter.Create().RequestMineInfo(context, TAG);
        if (observable!=null){
            observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
            observable.subscribe(new Consumer<ResMineInfo>() {
                @Override
                public void accept(ResMineInfo resMineInfo) throws Throwable {
                    MineInfoEvent event = new MineInfoEvent(resMineInfo.isSuccess(), resMineInfo.getResultObj());
                    if (!resMineInfo.isSuccess()){
                        event.setError(resMineInfo.getMessage());
                    }
                    EventBus.getDefault().post(event);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Throwable {
                    Log.d(TAG, throwable.getMessage());
                    MineInfoEvent event = new MineInfoEvent(false,null);
                    event.setError(context.getString(R.string.NetError));
                    EventBus.getDefault().post(event);
                }
            });
        }
    }

    /*
    * 更新版本二
    * */
    public void UpdateVersion(Context context, BaseFragment baseFragment){
        Observable<ResNewVersion> observable = GuBbBasePresenter.Create().RequestUpdateNewVersion(context, TAG);
        observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        observable.subscribe(new Consumer<ResNewVersion>() {
            @Override
            public void accept(ResNewVersion res) throws Throwable {
                NewVersionEvent event = new NewVersionEvent(res.isSuccess(), res.getResultObj());
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
    }

}
