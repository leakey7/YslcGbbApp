package com.gzyslczx.yslc.tools;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.LoginActivity;
import com.gzyslczx.yslc.events.GuBbChangeLoginEvent;
import com.gzyslczx.yslc.events.GuBbChangeOptionStateEvent;
import com.gzyslczx.yslc.events.GuBbChangePraiseEvent;
import com.gzyslczx.yslc.events.NoticeFocusUpdateEvent;
import com.gzyslczx.yslc.events.NoticePraiseUpdateEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.modes.response.ResJustIntObj;
import com.gzyslczx.yslc.modes.response.ResJustStrObj;
import com.gzyslczx.yslc.modes.response.ResLogin;
import com.gzyslczx.yslc.presenter.GuBbBasePresenter;

import org.greenrobot.eventbus.EventBus;

import cn.jiguang.verifysdk.api.AuthPageEventListener;
import cn.jiguang.verifysdk.api.JVerificationInterface;
import cn.jiguang.verifysdk.api.LoginSettings;
import cn.jiguang.verifysdk.api.VerifyListener;
import cn.jpush.android.api.JPushInterface;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

public class NormalActionTool {

    /*
    * 拉起登录
    * */
    public static void LoginAction(Context context, BaseFragment baseFragment, BaseActivity baseActivity, String StockCode, String TAG){
        if (JVerificationInterface.checkVerifyEnable(context)){
            //支持一键登录，拉起一键登录页
            LoginSettings settings = new LoginSettings();
            settings.setAutoFinish(false);
            settings.setTimeout(15 * 1000);
            settings.setAuthPageEventListener(new AuthPageEventListener() {
                @Override
                public void onEvent(int i, String s) {
                    Log.d(TAG, String.format("页面动作%d,%s", i, s));
                }
            });
            JGVerifyLogin.InitLoginUI(context);
            JVerificationInterface.loginAuth(context, settings, new VerifyListener() {
                @Override
                public void onResult(int i, String s, String s1) {
                    if (i == 6000) {
                        //获取登录Token成功
                        Observable<ResLogin> observable = GuBbBasePresenter.Create().RequestOneKeyLogin(context, s, TAG);
                        if (observable!=null){
                            if (baseFragment!=null) {
                                observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
                            }else {
                                observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
                            }
                            observable.subscribe(new Consumer<ResLogin>() {
                                @Override
                                public void accept(ResLogin res) throws Throwable {
                                    if (res.isSuccess()){
                                        boolean isSave = SpTool.Instance(context).SaveGuBbUserInfo(res.getResultObj().getId(), res.getResultObj().getPhone());
                                        JPushInterface.setAlias(context, 123, res.getResultObj().getPhone());
                                        GuBbChangeLoginEvent event = new GuBbChangeLoginEvent(isSave);
                                        event.setData(res.getResultObj());
                                        if (!TextUtils.isEmpty(StockCode)) {
                                            event.setStockCode(StockCode);
                                        }
                                        EventBus.getDefault().post(event);
                                        JVerificationInterface.dismissLoginAuthActivity();
                                        Log.d(TAG,"一键登录成功,关闭登录界面");
                                    }else {
                                        Toast.makeText(context, res.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Throwable {
                                    Log.d(TAG, throwable.getMessage());
                                    Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        //获取登录失败
                        if (i!=6002 && i!=6004){
                            Log.d(TAG, String.format("ActionCode=%d, Token=%s,Operator=%s", i, s, s1));
                            Intent intent = new Intent(context, LoginActivity.class);
                            if (!TextUtils.isEmpty(StockCode)) {
                                intent.putExtra(LoginActivity.WebStockCodeKey, StockCode);
                            }
                            context.startActivity(intent);
                        }
                    }
                }
            });
        }else {
            //不支持一键登录，拉起手机验证码登录页
            Intent intent = new Intent(context, LoginActivity.class);
            if (!TextUtils.isEmpty(StockCode)) {
                intent.putExtra(LoginActivity.WebStockCodeKey, StockCode);
            }
            context.startActivity(intent);
        }
    }

    /*
    * 请求关注
    * */
    public static void FocusAction(Context context, BaseFragment baseFragment, BaseActivity baseActivity, String id, boolean isTeacher, String TAG){
        Observable<ResJustIntObj> observable = GuBbBasePresenter.Create().RequestFocusAction(context, id, TAG);
        if (baseFragment!=null) {
            observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        }else {
            observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
        }
        observable.subscribe(new Consumer<ResJustIntObj>() {
            @Override
            public void accept(ResJustIntObj res) throws Throwable {
                //res.getResultObj(): 0=取消关注； 1=关注成功
                NoticeFocusUpdateEvent event = new NoticeFocusUpdateEvent(id, res.getResultObj()==1, isTeacher);
                if (!res.isSuccess()){
                    event.setERROR(res.getMessage());
                }
                EventBus.getDefault().post(event);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                NoticeFocusUpdateEvent event = new NoticeFocusUpdateEvent(id, false, isTeacher);
                event.setERROR(throwable.getMessage());
                EventBus.getDefault().post(event);
            }
        });
    };

    /*
    * 请求点赞
    * */
    public static void PraiseAction(Context context, BaseFragment baseFragment, BaseActivity baseActivity, int pos, String nid, boolean isTeacher, String TAG){
        Observable<ResJustIntObj> observable = GuBbBasePresenter.Create().RequestNormalPraise(context, nid, TAG);
        if (baseFragment!=null) {
            observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        }else {
            observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
        }
        observable.subscribe(new Consumer<ResJustIntObj>() {
            @Override
            public void accept(ResJustIntObj res) throws Throwable {
                Log.d(TAG, new Gson().toJson(res));
                if (res.isSuccess()) {
                    //res.getResultObj(): 0=取消点赞； 1=点赞成功
                    GuBbChangePraiseEvent event = new GuBbChangePraiseEvent(nid, true, isTeacher, pos);
                    event.setPraise(res.getResultObj()==1);
                    event.setPraiseNum(res.getCount());
                    EventBus.getDefault().post(event);
                    //通知隐藏页面更新点赞
                    NoticePraiseUpdateEvent notice = new NoticePraiseUpdateEvent(true, isTeacher, res.getResultObj()==1, nid, res.getCount());
                    EventBus.getDefault().post(notice);
                }else {
                    GuBbChangePraiseEvent event = new GuBbChangePraiseEvent(nid, false, isTeacher, pos);
                    EventBus.getDefault().post(event);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                GuBbChangePraiseEvent event = new GuBbChangePraiseEvent(nid, false, isTeacher, pos);
                EventBus.getDefault().post(event);
            }
        });

    }

    /*
    * 添加自选
    * */
    public static void AddOptionAction(Context context, BaseFragment baseFragment, BaseActivity baseActivity, String[] StockCodes, String TAG){
        Observable<ResJustStrObj> observable = GuBbBasePresenter.Create().RequestAddOption(context, StockCodes, TAG);
        if (baseFragment!=null) {
            observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        }else {
            observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
        }
        observable.subscribe(new Consumer<ResJustStrObj>() {
            @Override
            public void accept(ResJustStrObj res) throws Throwable {
                if (res.isSuccess()) {
                    GuBbChangeOptionStateEvent optionStateEvent = new GuBbChangeOptionStateEvent(true, StockCodes, true);
                    EventBus.getDefault().post(optionStateEvent);
                    Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
                }else {
                    GuBbChangeOptionStateEvent optionStateEvent = new GuBbChangeOptionStateEvent(false, StockCodes, true);
                    optionStateEvent.setError(res.getMessage());
                    EventBus.getDefault().post(optionStateEvent);
                    Toast.makeText(context, "添加失败", Toast.LENGTH_SHORT).show();;
                    Log.d(TAG, String.format("添加失败：%s", res.getMessage()));
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                GuBbChangeOptionStateEvent optionStateEvent = new GuBbChangeOptionStateEvent(false, StockCodes, true);
                EventBus.getDefault().post(optionStateEvent);
            }
        });
    }

    /*
     * 删除自选
     * */
    public static void DeleteOptionAction(Context context, BaseFragment baseFragment, BaseActivity baseActivity, String[] StockCodes, String TAG){
        Observable<ResJustStrObj> observable = GuBbBasePresenter.Create().RequestDeleteOption(context, StockCodes, TAG);
        if (baseFragment!=null) {
            observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        }else {
            observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
        }
        observable.subscribe(new Consumer<ResJustStrObj>() {
            @Override
            public void accept(ResJustStrObj res) throws Throwable {
                if (res.isSuccess()) {
                    GuBbChangeOptionStateEvent optionStateEvent = new GuBbChangeOptionStateEvent(true, StockCodes, false);
                    EventBus.getDefault().post(optionStateEvent);
                    Toast.makeText(context, "已删除", Toast.LENGTH_SHORT).show();
                }else {
                    GuBbChangeOptionStateEvent optionStateEvent = new GuBbChangeOptionStateEvent(false, StockCodes, false);
                    optionStateEvent.setError(res.getMessage());
                    EventBus.getDefault().post(optionStateEvent);
                    Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, String.format("删除失败：%s", res.getMessage()));
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                GuBbChangeOptionStateEvent optionStateEvent = new GuBbChangeOptionStateEvent(false, StockCodes, false);
                EventBus.getDefault().post(optionStateEvent);
            }
        });
    }

    /**
     * 设置整个字符串中的指定字符串的为指定颜色
     * @param origin 原字符串
     * @param colorStr 需要设置高亮的字符串
     * @param color 高亮的颜色
     * @return
     */
    public static SpannableString getColorSpannableString(String origin, String colorStr, int color){
        SpannableString spannableString = new SpannableString(origin);
        if(!TextUtils.isEmpty(colorStr)){
            int len = colorStr.length();
            for(int start = 0, end = 0; ; ){
                start = origin.indexOf(colorStr, end);
                if(start != -1){
                    end = start + len;
                    spannableString.setSpan(new ForegroundColorSpan(color),
                            start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                }else{
                    break;
                }
            }
        }
        return spannableString;
    }

}
