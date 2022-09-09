package com.gzyslczx.yslc.presenter;

import android.content.Context;
import android.util.Log;

import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.events.LoginCodeEvent;
import com.gzyslczx.yslc.events.LogoutCodeEvent;
import com.gzyslczx.yslc.events.LogoutEvent;
import com.gzyslczx.yslc.modes.response.ResJustStrObj;
import com.gzyslczx.yslc.tools.ConnTool;
import com.gzyslczx.yslc.tools.interfaces.CountDownTimeIfs;
import com.trello.rxlifecycle4.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LogoutPresenter {


    private final String TAG = "LogoutPresenter";

    /*
     * 请求注销账户
     * */
    public void RequestLogout(Context context, BaseActivity baseActivity, String code){
        Observable<ResJustStrObj> observable = GuBbBasePresenter.Create().RequestLogoutUser(context, code, TAG);
        observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
        observable.subscribe(new Consumer<ResJustStrObj>() {
            @Override
            public void accept(ResJustStrObj res) throws Throwable {
                LogoutEvent event = new LogoutEvent(res.isSuccess());
                event.setError(res.getMessage());
                EventBus.getDefault().post(event);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                LogoutEvent event = new LogoutEvent(false);
                event.setError(throwable.getMessage());
                EventBus.getDefault().post(event);
            }
        });
    }

    /*
     * 请求登录验证码
     * */
    public void RequestLogoutCode(Context context, BaseActivity baseActivity, String phone) {
        Observable<ResJustStrObj> observable = GuBbBasePresenter.Create().RequestLoginCode(context, phone, TAG);
        observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
        observable.subscribe(new Consumer<ResJustStrObj>() {
            @Override
            public void accept(ResJustStrObj res) throws Throwable {
                LogoutCodeEvent event = new LogoutCodeEvent(res.isSuccess(), res.getResultObj());
                if (!res.isSuccess()){
                    event.setError(res.getMessage());
                }
                EventBus.getDefault().post(event);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                LogoutCodeEvent event = new LogoutCodeEvent(false, null);
                event.setError(throwable.getMessage());
                EventBus.getDefault().post(event);
            }
        });

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

}
