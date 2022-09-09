package com.gzyslczx.yslc.presenter;

import android.content.Context;
import android.util.Log;

import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.events.MineFundOptionEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.modes.response.ResMyFundOption;
import com.gzyslczx.yslc.tools.ConnTool;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

public class FundOptionPresenter {

    private final String TAG = "FOptionPresenter";

    /*
    * 请求自选基金列表
    * */
    public void RequestMineFundOption(Context context, BaseFragment baseFragment){
        Observable<ResMyFundOption> observable = FundTongBasePresenter.Create().RequestMineOptionFund(context, TAG);
        observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        observable.subscribe(new Consumer<ResMyFundOption>() {
            @Override
            public void accept(ResMyFundOption res) throws Throwable {
                MineFundOptionEvent event = new MineFundOptionEvent(true, res.getResultObj());
                if (!res.isSuccess()){
                    event.setError(res.getMessage());
                }
                EventBus.getDefault().post(event);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                MineFundOptionEvent event = new MineFundOptionEvent(false, null);
                event.setError(context.getString(R.string.NetError));
                EventBus.getDefault().post(event);
            }
        });
    }

}
