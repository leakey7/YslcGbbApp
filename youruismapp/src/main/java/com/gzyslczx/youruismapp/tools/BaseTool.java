package com.gzyslczx.youruismapp.tools;

import android.util.Log;

import com.gzyslczx.youruismapp.BaseActivity;
import com.gzyslczx.youruismapp.fragments.BaseFragment;
import com.trello.rxlifecycle4.android.ActivityEvent;
import com.trello.rxlifecycle4.android.FragmentEvent;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class BaseTool {

    public void PrintLogE(String tag, String msg){
        Log.e(String.format("错误:%s", tag), msg);
    }

    public void PrintLogD(String tag, String msg){
        Log.d(String.format("日志:%s", tag), msg);
    }

    /*
     * 附加重试请求
     * */
    public Observable AddRetryRequest(Observable observable, String TAG) {
        return observable.retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Throwable {
                return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Throwable throwable) throws Throwable {
                        if (throwable instanceof IOException) {
                            PrintLogE(TAG, String.format("请求出现IO异常，2S后尝试重试：%s", throwable.getMessage()));
                            return Observable.just(1).delay(2, TimeUnit.SECONDS);
                        }
                        PrintLogE(TAG, String.format("请求出现未知异常，停止请求：%s", throwable.getMessage()));
                        return Observable.error(new Throwable(String.format("未知异常:%s", throwable.getMessage())));
                    }
                });
            }
        });
    }

    /*
     * 添加常规项--Activity
     * */
    public Observable AddExtraReqOfAct(Observable observable, BaseActivity activity) {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY));
    }

    /*
     * 添加常规项--Fragment
     * */
    public Observable AddExtraReqOfFrag(Observable observable, BaseFragment fragment) {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY));
    }

    /*
    * 添加所有项
    * BaseActivity和BaseFragment不能同时空
    * */
    public Observable AddRetryExtraAll(Observable observable, BaseActivity baseActivity, BaseFragment baseFragment, String tag){
        Observable obs = AddRetryRequest(observable, tag);
        if (baseActivity!=null){
            obs = AddExtraReqOfAct(observable, baseActivity);
        }else if (baseFragment!=null){
            obs = AddExtraReqOfFrag(observable, baseFragment);
        }
        return obs;
    }

}
