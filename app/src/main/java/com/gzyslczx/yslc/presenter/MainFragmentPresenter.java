package com.gzyslczx.yslc.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.gzyslczx.yslc.adapters.main.MainMovementAdapter;
import com.gzyslczx.yslc.events.GuBbAdvEvent;
import com.gzyslczx.yslc.events.GuBbMainMovementEvent;
import com.gzyslczx.yslc.events.GuBbMainRiskRadarEvent;
import com.gzyslczx.yslc.events.GuBbMainSupEvent;
import com.gzyslczx.yslc.events.GuBbMainTeacherLivingEvent;
import com.gzyslczx.yslc.events.GuBbTeacherDynEvent;
import com.gzyslczx.yslc.events.GuBbTokenEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.modes.response.ResAdv;
import com.gzyslczx.yslc.modes.response.ResMainFinancingBuy;
import com.gzyslczx.yslc.modes.response.ResMainInstitutionBuy;
import com.gzyslczx.yslc.modes.response.ResMainNorthBuy;
import com.gzyslczx.yslc.modes.response.ResMainRiskRadar;
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

public class MainFragmentPresenter {

    private final String TAG = "MainFragPres";

    /*
     * 请求股扒扒Token
     * HasToken： true=存有Token false=新建Token
     * */
    public void RequestToken(Context context, BaseFragment baseFragment){
        boolean HasToken = !(SpTool.Instance(context).GetInfo(SpTool.GuBbToken).equals(SpTool.SpDefault))
                && !(SpTool.Instance(context).GetInfo(SpTool.GuBbTokenTime).equals(SpTool.SpDefault))
                && !DateTool.MoreThan23Hour(SpTool.Instance(context).GetInfo(SpTool.GuBbTokenTime));
        if (HasToken){
            //Token有效，通知接收者
            GuBbTokenEvent tokenEvent = new GuBbTokenEvent(true, SpTool.Instance(context).GetInfo(SpTool.GuBbToken));
            EventBus.getDefault().postSticky(tokenEvent);
        }else {
            Observable<ResToken> observable = GuBbBasePresenter.Create().RequestGuBbToken(TAG);
            observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
            observable.subscribe(new Consumer<ResToken>() {
                @Override
                public void accept(ResToken resToken) throws Throwable {
                    if (resToken.isSuccess()) {
                        SpTool.Instance(context).SaveInfo(SpTool.GuBbToken, resToken.getResultObj().getAccess_token());
                        SpTool.Instance(context).SaveInfo(SpTool.GuBbTokenTime, DateTool.GetTodayDate());
                        //获取Token，通知接收者
                        GuBbTokenEvent tokenEvent = new GuBbTokenEvent(true, resToken.getResultObj().getAccess_token());
                        EventBus.getDefault().postSticky(tokenEvent);
                    } else {
                        GuBbTokenEvent tokenEvent = new GuBbTokenEvent(false, null);
                        tokenEvent.setERROR(resToken.getMessage());
                        EventBus.getDefault().postSticky(tokenEvent);
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
    public void ContactGuBbAdv(Context context, BaseFragment baseFragment, int id){
        Observable<ResAdv> observable = GuBbBasePresenter.Create().RequestGuBbAdv(context, id, TAG);
        observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        observable.subscribe(new Consumer<ResAdv>() {
            @Override
            public void accept(ResAdv res) throws Throwable {
                if (res.isSuccess()){
                    GuBbAdvEvent event = new GuBbAdvEvent(id, res.getResultObj(), true);
                    EventBus.getDefault().post(event);
                }else {
                    GuBbAdvEvent event = new GuBbAdvEvent(id,null, false);
                    event.setError(res.getMessage());
                    EventBus.getDefault().post(event);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
            }
        });
    }

    /*
    * 请求盘前特供
    * */
    public void ContactSpecialSupport(Context context, BaseFragment baseFragment){
        Observable<ResMainSup> observable = GuBbBasePresenter.Create().RequestMainSpecialSupport(context, TAG);
        observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        observable.subscribe(new Consumer<ResMainSup>() {
            @Override
            public void accept(ResMainSup res) throws Throwable {
                if (res.isSuccess()){
                    GuBbMainSupEvent event = new GuBbMainSupEvent(res.getResultObj(), true);
                    EventBus.getDefault().post(event);
                }else {
                    GuBbMainSupEvent event = new GuBbMainSupEvent(null, false);
                    event.setError(res.getMessage());
                    EventBus.getDefault().post(event);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
            }
        });
    }

    /*
    * 请求名师动态
    * */
    public void ContactTeacherDyn(Context context, BaseFragment baseFragment){
        Observable<ResTeacherDyn> observable = GuBbBasePresenter.Create().RequestMainTeacherDyn(context, TAG);
        observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        observable.subscribe(new Consumer<ResTeacherDyn>() {
            @Override
            public void accept(ResTeacherDyn res) throws Throwable {
                if (res.isSuccess()){
                    GuBbTeacherDynEvent event = new GuBbTeacherDynEvent(true, res.getResultObj());
                    EventBus.getDefault().post(event);
                }else {
                    GuBbTeacherDynEvent event = new GuBbTeacherDynEvent(false, null);
                    event.setError(res.getMessage());
                    EventBus.getDefault().post(event);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
            }
        });
    }

    /*
     * 请求名师直播
     * */
    public void ContactTeacherLiving(Context context, BaseFragment baseFragment){
        Observable<ResMainTeacherLiving> observable = GuBbBasePresenter.Create().RequestMainTeacherLiving(context, TAG);
        observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        observable.subscribe(new Consumer<ResMainTeacherLiving>() {
            @Override
            public void accept(ResMainTeacherLiving res) throws Throwable {
                if (res.isSuccess()){
                    Log.d(TAG, new Gson().toJson(res));
                    GuBbMainTeacherLivingEvent event = new GuBbMainTeacherLivingEvent(res.getResultObj(), true);
                    EventBus.getDefault().post(event);
                }else {
                    GuBbMainTeacherLivingEvent event = new GuBbMainTeacherLivingEvent(null, false);
                    event.setError(res.getMessage());
                    EventBus.getDefault().post(event);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
            }
        });
    }

    /*
    * 请求北向资金
    * */
    public void ContactNorthBuy(Context context, BaseFragment baseFragment){
        Observable<ResMainNorthBuy> observable = GuBbBasePresenter.Create().RequestMainNorthBuy(context, TAG);
        observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        observable.subscribe(new Consumer<ResMainNorthBuy>() {
            @Override
            public void accept(ResMainNorthBuy res) throws Throwable {
                GuBbMainMovementEvent event;
                if (res.isSuccess()){
                    event = new GuBbMainMovementEvent(true, MainMovementAdapter.NorthItem);
                    event.setNorthBuy(res);
                }else {
                    event = new GuBbMainMovementEvent(false, MainMovementAdapter.NorthItem);
                    event.setError(res.getMessage());
                }
                EventBus.getDefault().post(event);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                GuBbMainMovementEvent event = new GuBbMainMovementEvent(false, MainMovementAdapter.NorthItem);
                event.setError(throwable.getMessage());
                EventBus.getDefault().post(event);
            }
        });
    }

    /*
     * 请求机构买入
     * */
    public void ContactInstitutionBuy(Context context, BaseFragment baseFragment){
        Observable<ResMainInstitutionBuy> observable = GuBbBasePresenter.Create().RequestMainInstitutionsBuy(context, TAG);
        observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        observable.subscribe(new Consumer<ResMainInstitutionBuy>() {
            @Override
            public void accept(ResMainInstitutionBuy res) throws Throwable {
                GuBbMainMovementEvent event;
                if (res.isSuccess()){
                    event = new GuBbMainMovementEvent(true, MainMovementAdapter.InstitutionItem);
                    event.setInstitutionBuy(res);
                }else {
                    event = new GuBbMainMovementEvent(false, MainMovementAdapter.InstitutionItem);
                    event.setError(res.getMessage());
                }
                EventBus.getDefault().post(event);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                GuBbMainMovementEvent event = new GuBbMainMovementEvent(false, MainMovementAdapter.InstitutionItem);
                event.setError(throwable.getMessage());
                EventBus.getDefault().post(event);
            }
        });
    }

    /*
     * 请求融资资金
     * */
    public void ContactFinancingBuy(Context context, BaseFragment baseFragment){
        Observable<ResMainFinancingBuy> observable = GuBbBasePresenter.Create().RequestMainFinancingBuy(context, TAG);
        observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        observable.subscribe(new Consumer<ResMainFinancingBuy>() {
            @Override
            public void accept(ResMainFinancingBuy res) throws Throwable {
                GuBbMainMovementEvent event;
                if (res.isSuccess()){
                    event = new GuBbMainMovementEvent(true, MainMovementAdapter.FinancingItem);
                    event.setFinancingBuy(res);
                }else {
                    event = new GuBbMainMovementEvent(false, MainMovementAdapter.FinancingItem);
                    event.setError(res.getMessage());
                }
                EventBus.getDefault().post(event);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                GuBbMainMovementEvent event = new GuBbMainMovementEvent(false, MainMovementAdapter.FinancingItem);
                event.setError(throwable.getMessage());
                EventBus.getDefault().post(event);
            }
        });
    }

    /*
     * 请求风险雷达
     * */
    public void ContactRiskRadar(Context context, BaseFragment baseFragment){
        Observable<ResMainRiskRadar> observable = GuBbBasePresenter.Create().RequestMainRiskRadar(context, TAG);
        observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        observable.subscribe(new Consumer<ResMainRiskRadar>() {
            @Override
            public void accept(ResMainRiskRadar res) throws Throwable {
                GuBbMainRiskRadarEvent event;
                if (res.isSuccess()){
                    event = new GuBbMainRiskRadarEvent(true, res.getResultObj());
                }else {
                    event = new GuBbMainRiskRadarEvent(false, null);
                    event.setError(res.getMessage());
                }
                EventBus.getDefault().post(event);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                GuBbMainRiskRadarEvent event = new GuBbMainRiskRadarEvent(false, null);
                event.setError(throwable.getMessage());
                EventBus.getDefault().post(event);
            }
        });
    }

}
