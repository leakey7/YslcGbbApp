package com.gzyslczx.yslc.presenter;

import android.content.Context;
import android.util.Log;

import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.events.SpecialSupByDateEvent;
import com.gzyslczx.yslc.events.SpecialSupDetailEvent;
import com.gzyslczx.yslc.events.SpecialSupPraiseEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.modes.response.ResJustIntObj;
import com.gzyslczx.yslc.modes.response.ResSpecialSupDetail;
import com.gzyslczx.yslc.modes.response.ResSpecialSupport;
import com.gzyslczx.yslc.tools.ConnTool;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

public class SpecialSupPresenter {

    private final String TAG = "SpecialSupPres";

    /*
    * 根据日期查询盘前特供
    * */
    public void RequestSpecialSupByDate(Context context, BaseActivity baseActivity, String date){
        Observable<ResSpecialSupport> observable = GuBbBasePresenter.Create().RequestCheckSupport(context, date, TAG);
        observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
        observable.subscribe(new Consumer<ResSpecialSupport>() {
            @Override
            public void accept(ResSpecialSupport res) throws Throwable {
                SpecialSupByDateEvent event = new SpecialSupByDateEvent(res.isSuccess(), res.getResultObj());
                if (!res.isSuccess()){
                    event.setError(res.getMessage());
                }
                EventBus.getDefault().post(event);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                SpecialSupByDateEvent event = new SpecialSupByDateEvent(false, null);
                event.setError(context.getString(R.string.NetError));
                EventBus.getDefault().post(event);
            }
        });
    }

    /*
     * 请求盘前特供详情
     * */
    public void RequestSpecialSupportDetail(Context context, BaseActivity baseActivity, String sid){
        Observable<ResSpecialSupDetail> observable = GuBbBasePresenter.Create().RequestResSpecialSupDetail(context, sid, TAG);
        observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
        observable.subscribe(new Consumer<ResSpecialSupDetail>() {
            @Override
            public void accept(ResSpecialSupDetail res) throws Throwable {
                SpecialSupDetailEvent event = new SpecialSupDetailEvent(res.isSuccess(), res.getResultObj());
                if (!res.isSuccess()){
                    event.setError(res.getMessage());
                }
                EventBus.getDefault().post(event);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                SpecialSupDetailEvent event = new SpecialSupDetailEvent(false, null);
                event.setError(context.getString(R.string.NetError));
                EventBus.getDefault().post(event);
            }
        });
    }

    /*
     * 请求点赞盘前特供
     * */
    public void RequestSpecialSupportPraise(Context context, BaseActivity baseActivity, BaseFragment baseFragment,  String sid, String code){
        Observable<ResJustIntObj> observable = GuBbBasePresenter.Create().RequestResSpecialSupPraise(context, sid, TAG);
        if (baseFragment == null) {
            observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
        }else {
            observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        }
        observable.subscribe(new Consumer<ResJustIntObj>() {
            @Override
            public void accept(ResJustIntObj res) throws Throwable {
                SpecialSupPraiseEvent event = new SpecialSupPraiseEvent(res.isSuccess(), res.getResultObj()==1, res.getCount());
                event.setCode(code);
                if (!res.isSuccess()){
                    event.setError(res.getMessage());
                }
                EventBus.getDefault().post(event);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                SpecialSupPraiseEvent event = new SpecialSupPraiseEvent(false, false, 0);
                event.setError(context.getString(R.string.NetError));
                EventBus.getDefault().post(event);
            }
        });
    }

}
