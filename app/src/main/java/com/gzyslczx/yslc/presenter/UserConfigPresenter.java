package com.gzyslczx.yslc.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.events.UserChangeHeadImgEvent;
import com.gzyslczx.yslc.events.UserChangeNickNameEvent;
import com.gzyslczx.yslc.modes.response.ResChangeNickName;
import com.gzyslczx.yslc.modes.response.ResJustStrObj;
import com.gzyslczx.yslc.tools.ConnTool;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

public class UserConfigPresenter {

    private final String TAG = "UserConfigPres";

    /*
    * 请求更换头像
    * */
    public void RequestChangeHeadImg(Context context, BaseActivity baseActivity, File file){
        Log.d(TAG, "发出更换头像请求");
        Observable<ResJustStrObj> observable = GuBbBasePresenter.Create().RequestChangeHeadImg(context, file, TAG);
        observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
        observable.subscribe(new Consumer<ResJustStrObj>() {
            @Override
            public void accept(ResJustStrObj resJustStrObj) throws Throwable {
                Log.d(TAG, new Gson().toJson(resJustStrObj));
                UserChangeHeadImgEvent event = new UserChangeHeadImgEvent(resJustStrObj.isSuccess(), resJustStrObj.getResultObj());
                if (!resJustStrObj.isSuccess()){
                    event.setError(resJustStrObj.getMessage());
                }
                EventBus.getDefault().post(event);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                UserChangeHeadImgEvent event = new UserChangeHeadImgEvent(false, null);
                event.setError(context.getString(R.string.NetError));
                EventBus.getDefault().post(event);
            }
        });
    }

    /*
    * 请求更换昵称
    * */
    public void RequestChangeNickName(Context context, BaseActivity baseActivity, String nickName){
        Observable<ResChangeNickName> observable = GuBbBasePresenter.Create().RequestChangeNickName(context, nickName, TAG);
        observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
        observable.subscribe(new Consumer<ResChangeNickName>() {
            @Override
            public void accept(ResChangeNickName resChangeNickName) throws Throwable {
                UserChangeNickNameEvent event = new UserChangeNickNameEvent(resChangeNickName.isSuccess(), resChangeNickName.getResultObj());
                if (!resChangeNickName.isSuccess()){
                    event.setError(resChangeNickName.getMessage());
                }
                EventBus.getDefault().post(event);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                UserChangeNickNameEvent event = new UserChangeNickNameEvent(false, null);
                event.setError(context.getString(R.string.NetError));
                EventBus.getDefault().post(event);
            }
        });
    }


}
