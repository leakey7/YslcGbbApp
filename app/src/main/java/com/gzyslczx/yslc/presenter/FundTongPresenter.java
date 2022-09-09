package com.gzyslczx.yslc.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.events.FunTongHuShenEvent;
import com.gzyslczx.yslc.events.FundTongFindDefaultEvent;
import com.gzyslczx.yslc.events.FundTongHomeIconTabEvent;
import com.gzyslczx.yslc.events.FundTongHomeRankEvent;
import com.gzyslczx.yslc.events.FundTongHomeAdvEvent;
import com.gzyslczx.yslc.events.FundTongLoginEvent;
import com.gzyslczx.yslc.events.FundTongTokenEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.modes.response.ResAdv;
import com.gzyslczx.yslc.modes.response.ResFundTongDefault;
import com.gzyslczx.yslc.modes.response.ResFundTongHuShen;
import com.gzyslczx.yslc.modes.response.ResFundTongIcon;
import com.gzyslczx.yslc.modes.response.ResFundTongLogin;
import com.gzyslczx.yslc.modes.response.ResFundTongRank;
import com.gzyslczx.yslc.modes.response.ResLogin;
import com.gzyslczx.yslc.modes.response.ResToken;
import com.gzyslczx.yslc.tools.ConnTool;
import com.gzyslczx.yslc.tools.DateTool;
import com.gzyslczx.yslc.tools.SpTool;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

public class FundTongPresenter {

    private final String TAG = "FundTongPres";
    private int CurrentPage = 1;

    /*
     * 请求股扒扒Token
     * HasToken： true=存有Token false=新建Token
     * */
    public void RequestFundTongToken(Context context, BaseFragment baseFragment){
        boolean HasToken = !(SpTool.Instance(context).GetInfo(SpTool.FundTongToken).equals(SpTool.SpDefault))
                && !(SpTool.Instance(context).GetInfo(SpTool.FundTongTokenTime).equals(SpTool.SpDefault))
                && !DateTool.MoreThan23Hour(SpTool.Instance(context).GetInfo(SpTool.FundTongTokenTime));
        if (HasToken){
            //Token有效，通知接收者
            FundTongTokenEvent tokenEvent = new FundTongTokenEvent(true, SpTool.Instance(context).GetInfo(SpTool.GuBbToken));
            EventBus.getDefault().post(tokenEvent);
        }else {
            Observable<ResToken> observable = FundTongBasePresenter.Create().RequestFundTongToken(TAG);
            observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
            observable.subscribe(new Consumer<ResToken>() {
                @Override
                public void accept(ResToken resToken) throws Throwable {
                    if (resToken.isSuccess()) {
                        SpTool.Instance(context).SaveInfo(SpTool.FundTongToken, resToken.getResultObj().getAccess_token());
                        SpTool.Instance(context).SaveInfo(SpTool.FundTongTokenTime, DateTool.GetTodayDate());
                        //获取Token，通知接收者
                        FundTongTokenEvent tokenEvent = new FundTongTokenEvent(true, resToken.getResultObj().getAccess_token());
                        EventBus.getDefault().post(tokenEvent);
                    } else {
                        FundTongTokenEvent tokenEvent = new FundTongTokenEvent(false, null);
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
     * 请求隐形登录基金通
     * */
    public void RequestFundTongLogin(Context context, BaseFragment baseFragment){
        Observable<ResFundTongLogin> observable = FundTongBasePresenter.Create().RequestFundTongVerifyCode(context, TAG);
        observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        observable.subscribe(new Consumer<ResFundTongLogin>() {
            @Override
            public void accept(ResFundTongLogin resLogin) throws Throwable {
                FundTongLoginEvent event = new FundTongLoginEvent(resLogin.isSuccess());
                if (resLogin.isSuccess()){
                    SpTool.Instance(context).SaveInfo(SpTool.FundTongUID, resLogin.getResultObj().getUserId());
                }else {
                    event.setError(resLogin.getMessage());
                }
                EventBus.getDefault().post(event);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                FundTongLoginEvent event = new FundTongLoginEvent(false);
                event.setError(context.getString(R.string.NetError));
                EventBus.getDefault().post(event);
            }
        });
    }

    /*
     * 请求首页广告图
     * */
    public void RequestFundAdv(Context context, BaseFragment baseFragment){
        Observable<ResAdv> observable = FundTongBasePresenter.Create().RequestFundTongAdv(context, 1, TAG);
        observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        observable.subscribe(new Consumer<ResAdv>() {
            @Override
            public void accept(ResAdv adv) throws Throwable {
                FundTongHomeAdvEvent event = new FundTongHomeAdvEvent(adv.isSuccess(), adv);
                if (!adv.isSuccess()){
                    event.setError(adv.getMessage());
                }
                EventBus.getDefault().post(event);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                FundTongHomeAdvEvent event = new FundTongHomeAdvEvent(false, null);
                event.setError(context.getString(R.string.NetError));
                EventBus.getDefault().post(event);
            }
        });
    }


    /*
     * 请求首页图标
     * */
    public void RequestFundIconTab(Context context, BaseFragment baseFragment){
        Observable<ResFundTongIcon> observable = FundTongBasePresenter.Create().RequestFundTongIcon(context, TAG);
        observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        observable.subscribe(new Consumer<ResFundTongIcon>() {
            @Override
            public void accept(ResFundTongIcon res) throws Throwable {

                FundTongHomeIconTabEvent event = new FundTongHomeIconTabEvent(res.isSuccess(), res.getResultObj());
                if (!res.isSuccess()){
                    event.setError(res.getMessage());
                }
                EventBus.getDefault().post(event);

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                FundTongHomeIconTabEvent event = new FundTongHomeIconTabEvent(false, null);
                event.setError(context.getString(R.string.NetError));
                EventBus.getDefault().post(event);
            }
        });
    }

    /*
     * 请求首页排行榜
     * */
    public void RequestFundRankList(Context context, BaseFragment baseFragment,  boolean isInit,  int iconId, int typeId, int sort){
        if (isInit){
            CurrentPage = 1;
        }
        Observable<ResFundTongRank> observable = FundTongBasePresenter.Create().RequestFundTongSortRank(context, iconId,typeId, CurrentPage, sort, TAG);
        observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        observable.subscribe(new Consumer<ResFundTongRank>() {
            @Override
            public void accept(ResFundTongRank res) throws Throwable {
                boolean isEnd=false;
                if (res.isSuccess()){
                    if (CurrentPage >= res.getResultObj().getPageSize()){
                        isEnd = true;
                    }else {
                        isEnd = false;
                        CurrentPage ++;
                    }
                }
                FundTongHomeRankEvent event = new FundTongHomeRankEvent(res.isSuccess(), res.getResultObj().getPageInfo());
                event.setEnd(isEnd);
                event.setInit(isInit);
                if (!res.isSuccess()){
                    event.setError(res.getMessage());
                }
                EventBus.getDefault().post(event);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                FundTongHomeRankEvent event = new FundTongHomeRankEvent(false, null);
                event.setEnd(false);
                event.setInit(isInit);
                event.setError(context.getString(R.string.NetError));
                EventBus.getDefault().post(event);
            }
        });
    }

    /*
     * 请求默认行业和概念数据
     * */
    public void RequestDefaultFind(Context context, BaseFragment baseFragment){
        Observable<ResFundTongDefault> observable = FundTongBasePresenter.Create().RequestDefault(context, TAG);
        observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        observable.subscribe(new Consumer<ResFundTongDefault>() {
            @Override
            public void accept(ResFundTongDefault res) throws Throwable {
                FundTongFindDefaultEvent event = new FundTongFindDefaultEvent(res.isSuccess(), res.getResultObj());
                if (!res.isSuccess()){
                    event.setError(res.getMessage());
                }
                EventBus.getDefault().postSticky(event);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                FundTongFindDefaultEvent event = new FundTongFindDefaultEvent(false, null);
                event.setError(context.getString(R.string.NetError));
                EventBus.getDefault().postSticky(event);
            }
        });
    }

    /*
    * 请求沪深三百
    * */
    public void RequestHuShen(Context context, BaseFragment baseFragment ){
        Observable<ResFundTongHuShen> observable = FundTongBasePresenter.Create().RequestHuShen(context, TAG);
        observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        observable.subscribe(new Consumer<ResFundTongHuShen>() {
            @Override
            public void accept(ResFundTongHuShen res) throws Throwable {
                FunTongHuShenEvent event = new FunTongHuShenEvent(res.isSuccess(), res.getResultObj());
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
