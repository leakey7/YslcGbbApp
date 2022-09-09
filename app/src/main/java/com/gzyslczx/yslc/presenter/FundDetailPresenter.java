package com.gzyslczx.yslc.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.events.AddFundOptionEvent;
import com.gzyslczx.yslc.events.DeleteFundOptionEvent;
import com.gzyslczx.yslc.events.FundTongDetailEvent;
import com.gzyslczx.yslc.events.FundTongDetailIconEvent;
import com.gzyslczx.yslc.events.FundTongLoginEvent;
import com.gzyslczx.yslc.events.FundTongTokenEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.modes.response.ResAdv;
import com.gzyslczx.yslc.modes.response.ResFundDetail;
import com.gzyslczx.yslc.modes.response.ResFundTongLogin;
import com.gzyslczx.yslc.modes.response.ResJustStrObj;
import com.gzyslczx.yslc.modes.response.ResLogin;
import com.gzyslczx.yslc.modes.response.ResToken;
import com.gzyslczx.yslc.tools.ConnTool;
import com.gzyslczx.yslc.tools.DateTool;
import com.gzyslczx.yslc.tools.SpTool;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

public class FundDetailPresenter {

    private final String TAG = "FundDetailPres";

    /*
     * 请求股扒扒Token
     * HasToken： true=存有Token false=新建Token
     * */
    public void RequestFundTongToken(Context context, BaseActivity baseActivity){
        boolean HasToken = !(SpTool.Instance(context).GetInfo(SpTool.FundTongToken).equals(SpTool.SpDefault))
                && !(SpTool.Instance(context).GetInfo(SpTool.FundTongTokenTime).equals(SpTool.SpDefault))
                && !DateTool.MoreThan23Hour(SpTool.Instance(context).GetInfo(SpTool.FundTongTokenTime));
        if (HasToken){
            //Token有效，通知接收者
            FundTongTokenEvent tokenEvent = new FundTongTokenEvent(true, SpTool.Instance(context).GetInfo(SpTool.GuBbToken));
            EventBus.getDefault().post(tokenEvent);
        }else {
            Observable<ResToken> observable = FundTongBasePresenter.Create().RequestFundTongToken(TAG);
            observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
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
    public void RequestFundTongLogin(Context context, BaseActivity baseActivity){
        Observable<ResFundTongLogin> observable = FundTongBasePresenter.Create().RequestFundTongVerifyCode(context, TAG);
        observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
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
     * 请求小图标
     * */
    public void RequestFundTongDetailIcons(Context context, BaseActivity baseActivity){
        Observable<ResAdv> observable = FundTongBasePresenter.Create().RequestFundTongAdv(context, 2, TAG);
        observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
        observable.subscribe(new Consumer<ResAdv>() {
            @Override
            public void accept(ResAdv resAdv) throws Throwable {
                Log.d(TAG, new Gson().toJson(resAdv));
                if (resAdv.isSuccess()){
                    FundTongDetailIconEvent event = new FundTongDetailIconEvent(true, resAdv.getResultObj());
                    EventBus.getDefault().post(event);
                }else {
                    FundTongDetailIconEvent event = new FundTongDetailIconEvent(false, null);
                    event.setError(resAdv.getMessage());
                    EventBus.getDefault().post(event);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                FundTongDetailIconEvent event = new FundTongDetailIconEvent(false, null);
                event.setError(context.getString(R.string.NetError));
                EventBus.getDefault().post(event);
            }
        });
    }

    /*
     * 请求基金详情
     * */
    public void RequestDetails(Context context, BaseActivity baseActivity, String FundCode){
        Observable<ResFundDetail> observable = FundTongBasePresenter.Create().RequestFundDetail(context, FundCode, TAG);
        observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
        observable.subscribe(new Consumer<ResFundDetail>() {
            @Override
            public void accept(ResFundDetail detail) throws Throwable {
                Log.d(TAG, new Gson().toJson(detail));
                if (detail.isSuccess()){
                    FundTongDetailEvent event = new FundTongDetailEvent(true, detail.getResultObj());
                    EventBus.getDefault().post(event);
                }else {
                    FundTongDetailEvent event = new FundTongDetailEvent(false, null);
                    event.setError(detail.getMessage());
                    EventBus.getDefault().post(event);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                FundTongDetailEvent event = new FundTongDetailEvent(false, null);
                event.setError(context.getString(R.string.NetError));
                EventBus.getDefault().post(event);
            }
        });
    }

    /*
    * 添加自选基金
    * */
    public void RequestAddFundOption(Context context, BaseActivity baseActivity, String fundCode){
        Observable<ResJustStrObj> observable = FundTongBasePresenter.Create().RequestAddFundOption(context, fundCode, TAG);
        if (observable !=null){
            observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
            observable.subscribe(new Consumer<ResJustStrObj>() {
                @Override
                public void accept(ResJustStrObj res) throws Throwable {
                    AddFundOptionEvent event = new AddFundOptionEvent(res.isSuccess());
                    EventBus.getDefault().post(event);
                    if (res.isSuccess()){
                        Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context, res.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Throwable {
                    Log.d(TAG, throwable.getMessage());
                    AddFundOptionEvent event = new AddFundOptionEvent(false);
                    EventBus.getDefault().post(event);
                    Toast.makeText(context, context.getString(R.string.NetError), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /*
     * 删除自选基金
     * */
    public void RequestDeleteFundOption(Context context, BaseActivity baseActivity, String fundCode){
        Observable<ResJustStrObj> observable = FundTongBasePresenter.Create().RequestDeleteFundOption(context, fundCode, TAG);
        if (observable !=null){
            observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
            observable.subscribe(new Consumer<ResJustStrObj>() {
                @Override
                public void accept(ResJustStrObj res) throws Throwable {
                    DeleteFundOptionEvent event = new DeleteFundOptionEvent(res.isSuccess());
                    EventBus.getDefault().post(event);
                    if (res.isSuccess()){
                        Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context, res.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Throwable {
                    Log.d(TAG, throwable.getMessage());
                    DeleteFundOptionEvent event = new DeleteFundOptionEvent(false);
                    EventBus.getDefault().post(event);
                    Toast.makeText(context, context.getString(R.string.NetError), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}
