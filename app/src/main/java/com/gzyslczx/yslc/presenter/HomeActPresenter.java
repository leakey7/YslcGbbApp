package com.gzyslczx.yslc.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.events.GuBbAdvEvent;
import com.gzyslczx.yslc.events.GuBbMainSupEvent;
import com.gzyslczx.yslc.events.GuBbMainTeacherLivingEvent;
import com.gzyslczx.yslc.events.GuBbTeacherDynEvent;
import com.gzyslczx.yslc.events.GuBbTokenEvent;
import com.gzyslczx.yslc.events.NoticeLauncherInitEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.modes.response.ResAdv;
import com.gzyslczx.yslc.modes.response.ResAppSet;
import com.gzyslczx.yslc.modes.response.ResMainSup;
import com.gzyslczx.yslc.modes.response.ResMainTeacherLiving;
import com.gzyslczx.yslc.modes.response.ResTeacherDyn;
import com.gzyslczx.yslc.modes.response.ResToken;
import com.gzyslczx.yslc.tools.ConnTool;
import com.gzyslczx.yslc.tools.DateTool;
import com.gzyslczx.yslc.tools.SpTool;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

public class HomeActPresenter {

    private final String TAG = "GBbHomeActPres";

    /*
     * 请求股扒扒Token
     * HasToken： true=存有Token false=新建Token
     * */
    public void RequestToken(Context context, BaseFragment baseFragment, BaseActivity baseActivity){
        boolean HasToken = !(SpTool.Instance(context).GetInfo(SpTool.GuBbToken).equals(SpTool.SpDefault))
                && !(SpTool.Instance(context).GetInfo(SpTool.GuBbTokenTime).equals(SpTool.SpDefault))
                && !DateTool.MoreThan23Hour(SpTool.Instance(context).GetInfo(SpTool.GuBbTokenTime));
        if (HasToken){
            //Token有效，通知接收者
            GuBbTokenEvent tokenEvent = new GuBbTokenEvent(true, SpTool.Instance(context).GetInfo(SpTool.GuBbToken));
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
                        GuBbTokenEvent tokenEvent = new GuBbTokenEvent(true, resToken.getResultObj().getAccess_token());
                        EventBus.getDefault().post(tokenEvent);
                    } else {
                        GuBbTokenEvent tokenEvent = new GuBbTokenEvent(false, null);
                        tokenEvent.setERROR(resToken.getMessage());
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

    /*
     * 请求广告图标
     * 1是首页栏目图标 2战绩榜 3为栏目页广告 4为股扒扒图标 5为开机图标 6为服务默认广告 7为首页顶部滚动广告 8为首页直播图片 9为消息类型
     * */
    public void ContactGuBbAdv(Context context, BaseActivity baseActivity, BaseFragment baseFragment, int id){
        Observable<ResAdv> observable = GuBbBasePresenter.Create().RequestGuBbAdv(context, id, TAG);
        if (baseFragment!=null) {
            observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        }else {
            observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
        }
        observable.subscribe(new Consumer<ResAdv>() {
            @Override
            public void accept(ResAdv res) throws Throwable {
                if (res.isSuccess()){
                    GuBbAdvEvent event = new GuBbAdvEvent(id, res.getResultObj(), true);
                    EventBus.getDefault().postSticky(event);
                }else {
                    GuBbAdvEvent event = new GuBbAdvEvent(id,null, false);
                    event.setError(res.getMessage());
                    EventBus.getDefault().postSticky(event);
                }
                NoticeLauncherInitEvent initEvent = new NoticeLauncherInitEvent(res.isSuccess());
                EventBus.getDefault().post(initEvent);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                NoticeLauncherInitEvent initEvent = new NoticeLauncherInitEvent(false);
                EventBus.getDefault().post(initEvent);
            }
        });
    }

    /*
     * 请求盘前特供
     * */
    public void ContactSpecialSupport(Context context, BaseActivity baseActivity, BaseFragment baseFragment){
        Observable<ResMainSup> observable = GuBbBasePresenter.Create().RequestMainSpecialSupport(context, TAG);
        if (baseFragment!=null) {
            observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        }else {
            observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
        }
        observable.subscribe(new Consumer<ResMainSup>() {
            @Override
            public void accept(ResMainSup res) throws Throwable {
                if (res.isSuccess()){
                    GuBbMainSupEvent event = new GuBbMainSupEvent(res.getResultObj(), true);
                    EventBus.getDefault().postSticky(event);
                }else {
                    GuBbMainSupEvent event = new GuBbMainSupEvent(null, false);
                    event.setError(res.getMessage());
                    EventBus.getDefault().postSticky(event);
                }
                NoticeLauncherInitEvent initEvent = new NoticeLauncherInitEvent(res.isSuccess());
                EventBus.getDefault().post(initEvent);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                NoticeLauncherInitEvent initEvent = new NoticeLauncherInitEvent(false);
                EventBus.getDefault().post(initEvent);
            }
        });
    }

    /*
     * 请求名师动态
     * */
    public void ContactTeacherDyn(Context context, BaseActivity baseActivity, BaseFragment baseFragment){
        Observable<ResTeacherDyn> observable = GuBbBasePresenter.Create().RequestMainTeacherDyn(context, TAG);
        if (baseFragment!=null) {
            observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        }else {
            observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
        }
        observable.subscribe(new Consumer<ResTeacherDyn>() {
            @Override
            public void accept(ResTeacherDyn res) throws Throwable {
                if (res.isSuccess()){
                    GuBbTeacherDynEvent event = new GuBbTeacherDynEvent(true, res.getResultObj());
                    EventBus.getDefault().postSticky(event);
                }else {
                    GuBbTeacherDynEvent event = new GuBbTeacherDynEvent(false, null);
                    event.setError(res.getMessage());
                    EventBus.getDefault().postSticky(event);
                }
                NoticeLauncherInitEvent initEvent = new NoticeLauncherInitEvent(res.isSuccess());
                EventBus.getDefault().post(initEvent);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                NoticeLauncherInitEvent initEvent = new NoticeLauncherInitEvent(false);
                EventBus.getDefault().post(initEvent);
            }
        });
    }

    /*
    * 请求App设置
    * */
    public void ContactAppSet(Context context, BaseActivity baseActivity, BaseFragment baseFragment){
        Observable<ResAppSet> observable = GuBbBasePresenter.Create().RequestAppSet(context, TAG);
        if (baseFragment!=null) {
            observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        }else {
            observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
        }
        observable.subscribe(new Consumer<ResAppSet>() {
            @Override
            public void accept(ResAppSet res) throws Throwable {
                Log.d(TAG, new Gson().toJson(res));
                NoticeLauncherInitEvent initEvent = new NoticeLauncherInitEvent(res.isSuccess());
                if (res.isSuccess()){
                    SpTool.Instance(context).SaveHiddenLive(res.getResultObj().isHidLive());
                }
                EventBus.getDefault().post(initEvent);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
            }
        });
    }

}
