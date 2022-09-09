package com.gzyslczx.yslc.presenter;

import android.content.Context;
import android.util.Log;

import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.events.SuggestCommitEvent;
import com.gzyslczx.yslc.modes.response.ResJustStrObj;
import com.gzyslczx.yslc.tools.ConnTool;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

public class MineSuggestPresenter {

    private final String TAG = "MineSuggestPres";

    /*
    * 请求提交建议
    * */
    public void RequestSuggest(Context context, BaseActivity baseActivity, String content){
        Observable<ResJustStrObj> observable = GuBbBasePresenter.Create().RequestSuggest(context, content, TAG);
        observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
        observable.subscribe(new Consumer<ResJustStrObj>() {
            @Override
            public void accept(ResJustStrObj resJustStrObj) throws Throwable {
                SuggestCommitEvent event= new SuggestCommitEvent(resJustStrObj.isSuccess(), resJustStrObj.getMessage());
                EventBus.getDefault().post(event);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                SuggestCommitEvent event = new SuggestCommitEvent(false, null);
                EventBus.getDefault().post(event);
            }
        });
    }

}
