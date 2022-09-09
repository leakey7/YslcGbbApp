package com.gzyslczx.yslc.tools;

import android.util.Log;

import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.trello.rxlifecycle4.android.ActivityEvent;
import com.trello.rxlifecycle4.android.FragmentEvent;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ConnTool {

    /*
     * 附加重试请求
     * */
    public static Observable AddRetryReq(Observable observable, String TAG) {
        return observable.retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Throwable {
                return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Throwable throwable) throws Throwable {
                        if (throwable instanceof IOException) {
                            Log.d(TAG, String.format("发生异常:%s,2s后重试请求", throwable.getMessage()));
                            return Observable.just(1).delay(2, TimeUnit.SECONDS);
                        }
                        Log.d(TAG, String.format("发生未知异常:%s", throwable.getMessage()));
                        return Observable.error(new Throwable(
                                String.format("发生未知异常:%s", throwable.getMessage())));
                    }
                });
            }
        });
    }

    /*
     * 添加常规项--Activity
     * */
    public static Observable AddExtraReqOfAct(Observable observable, String TAG, BaseActivity activity) {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY));
    }

    /*
    * 添加常规项--Fragment
    * */
    public static Observable AddExtraReqOfFrag(Observable observable, String TAG, BaseFragment fragment) {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY));
    }

}
