package com.gzyslczx.yslc.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.events.GuBbTokenEvent;
import com.gzyslczx.yslc.events.GuBbTokenOnPushEvent;
import com.gzyslczx.yslc.events.UpdateMsgListEvent;
import com.gzyslczx.yslc.events.UpdatePushEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.modes.response.ResJustStrObj;
import com.gzyslczx.yslc.modes.response.ResToken;
import com.gzyslczx.yslc.tools.ConnTool;
import com.gzyslczx.yslc.tools.DateTool;
import com.gzyslczx.yslc.tools.SpTool;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

public class HomePresenter {

    private final String TAG = "HomePresenter";

    /*
     * 更新推送已读
     * */
    public void RequestUpdatePush(Context context, BaseActivity baseActivity, String url, String title){
        Observable<ResJustStrObj> observable = GuBbBasePresenter.Create().RequestUpdatePush(context, title, url, TAG);
        observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
        observable.subscribe(new Consumer<ResJustStrObj>() {
            @Override
            public void accept(ResJustStrObj resJustStrObj) throws Throwable {
                UpdatePushEvent event = new UpdatePushEvent(resJustStrObj.isSuccess(), resJustStrObj.getMessage());
                EventBus.getDefault().post(event);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                UpdatePushEvent event = new UpdatePushEvent(false, throwable.getMessage());
                EventBus.getDefault().post(event);
            }
        });
    }

    /*
     * 请求股扒扒Token
     * HasToken： true=存有Token false=新建Token
     * */
    public void RequestToken(Context context, BaseFragment baseFragment, BaseActivity baseActivity, Intent pushIntent){
        boolean HasToken = !(SpTool.Instance(context).GetInfo(SpTool.GuBbToken).equals(SpTool.SpDefault))
                && !(SpTool.Instance(context).GetInfo(SpTool.GuBbTokenTime).equals(SpTool.SpDefault))
                && !DateTool.MoreThan23Hour(SpTool.Instance(context).GetInfo(SpTool.GuBbTokenTime));
        if (HasToken){
            //Token有效，通知接收者
            GuBbTokenOnPushEvent tokenEvent = new GuBbTokenOnPushEvent(true, SpTool.Instance(context).GetInfo(SpTool.GuBbToken));
            tokenEvent.setIntent(pushIntent);
            EventBus.getDefault().post(tokenEvent);
        }else {
            Observable<ResToken> observable = GuBbBasePresenter.Create().RequestGuBbToken(TAG);
            if (baseFragment!=null) {
                observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
            }else {
                observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
            }
            observable.subscribe(new Consumer<ResToken>() {
                @Override
                public void accept(ResToken resToken) throws Throwable {
                    if (resToken.isSuccess()) {
                        SpTool.Instance(context).SaveInfo(SpTool.GuBbToken, resToken.getResultObj().getAccess_token());
                        SpTool.Instance(context).SaveInfo(SpTool.GuBbTokenTime, DateTool.GetTodayDate());
                        //获取Token，通知接收者
                        GuBbTokenOnPushEvent tokenEvent = new GuBbTokenOnPushEvent(true, resToken.getResultObj().getAccess_token());
                        tokenEvent.setIntent(pushIntent);
                        EventBus.getDefault().post(tokenEvent);
                    } else {
                        GuBbTokenOnPushEvent tokenEvent = new GuBbTokenOnPushEvent(false, null);
                        tokenEvent.setERROR(resToken.getMessage());
                        tokenEvent.setIntent(pushIntent);
                        EventBus.getDefault().post(tokenEvent);
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

}
