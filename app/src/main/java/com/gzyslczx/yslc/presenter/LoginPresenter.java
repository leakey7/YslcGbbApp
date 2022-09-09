package com.gzyslczx.yslc.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.events.GuBbChangeLoginEvent;
import com.gzyslczx.yslc.events.LoginCodeEvent;
import com.gzyslczx.yslc.modes.response.ResJustStrObj;
import com.gzyslczx.yslc.modes.response.ResLogin;
import com.gzyslczx.yslc.tools.ConnTool;
import com.gzyslczx.yslc.tools.SpTool;
import com.gzyslczx.yslc.tools.interfaces.CountDownTimeIfs;
import com.trello.rxlifecycle4.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginPresenter {

    private final String TAG = "LoginPresenter";

    public boolean CheckPhone(Context context, String phone) {
        if (phone == null || phone.isEmpty()) {
            Toast.makeText(context, "请输入手机号", Toast.LENGTH_SHORT).show();
            return false;
        } else if (phone.length() < 11) {
            Toast.makeText(context, "请输入有效手机号", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean CheckCode(Context context, String inCode, boolean isGetCode) {
        if (isGetCode) {
            if (TextUtils.isEmpty(inCode)) {
                Toast.makeText(context, "请输入验证码", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                return true;
            }
        }
        Toast.makeText(context, "请获取验证码", Toast.LENGTH_SHORT).show();
        return false;
    }

    //    倒计时
    public Disposable CountDownTime(int start, int sumTime, int intervalTime, CountDownTimeIfs ifs, BaseActivity mActivity) {
        Disposable disposable = Flowable
                .intervalRange(start, sumTime, 0, intervalTime, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Throwable {
                        int t = (int) (sumTime - aLong);
                        ifs.OnCountDown(t);
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Throwable {
                        ifs.OnComplete();
                    }
                })
                .subscribe();
        return disposable;
    }

    /*
     * 请求登录验证码
     * */
    public void RequestLoginCode(Context context, BaseActivity baseActivity, String phone) {
        Observable<ResJustStrObj> observable = GuBbBasePresenter.Create().RequestLoginCode(context, phone, TAG);
        observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
        observable.subscribe(new Consumer<ResJustStrObj>() {
            @Override
            public void accept(ResJustStrObj res) throws Throwable {
                LoginCodeEvent event = new LoginCodeEvent(res.isSuccess(), res.getResultObj());
                if (!res.isSuccess()){
                    event.setError(res.getMessage());
                }
                EventBus.getDefault().post(event);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                LoginCodeEvent event = new LoginCodeEvent(false, null);
                event.setError(throwable.getMessage());
                EventBus.getDefault().post(event);
            }
        });

    }

    /*
     * 请求登录
     * */
    public void RequestLogin(Context context, BaseActivity baseActivity, String phone, String code, String StockCode) {
        Observable<ResLogin> observable = GuBbBasePresenter.Create().RequestLoginByCode(context, phone, code, TAG);
        observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
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
                }else{
                    GuBbChangeLoginEvent event = new GuBbChangeLoginEvent(false);
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

}
