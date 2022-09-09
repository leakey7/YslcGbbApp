package com.gzyslczx.yslc.presenter;

import android.content.Context;
import android.util.Log;

import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.events.GuBbKLineDetailEvent;
import com.gzyslczx.yslc.events.GuBbKLineLearnEvent;
import com.gzyslczx.yslc.events.GuBbKLinePraiseEvent;
import com.gzyslczx.yslc.events.GuBbKLineTypeEvent;
import com.gzyslczx.yslc.modes.response.ResJustIntObj;
import com.gzyslczx.yslc.modes.response.ResKLineDetails;
import com.gzyslczx.yslc.modes.response.ResKLineType;
import com.gzyslczx.yslc.tools.ConnTool;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

public class KLinePresenter {

    private final String TAG = "KLPresenter";

    /*
    * 请求K线秘籍类型信息
    * */
    public void RequestKLineType(Context context, BaseActivity baseActivity){
        Observable<ResKLineType> observable = GuBbBasePresenter.Create().RequestKLineType(context, TAG);
        observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
        observable.subscribe(new Consumer<ResKLineType>() {
            @Override
            public void accept(ResKLineType res) throws Throwable {
                GuBbKLineTypeEvent event=null;
                if (res.isSuccess()){
                    event = new GuBbKLineTypeEvent(true, res.getResultObj());
                }else {
                    event = new GuBbKLineTypeEvent(false, null);
                    event.setError(res.getMessage());
                }
                EventBus.getDefault().post(event);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                GuBbKLineTypeEvent event = new GuBbKLineTypeEvent(false, null);
                event.setError(context.getString(R.string.NetError));
                EventBus.getDefault().post(event);
            }
        });
    }


    /*
    * 请求K线秘籍视频详情
    * */
    public void RequestKLineVideo(Context context, BaseActivity baseActivity, int id){
        Observable<ResKLineDetails> observable = GuBbBasePresenter.Create().RequestKLineVideoDetail(context, id, TAG);
        observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
        observable.subscribe(new Consumer<ResKLineDetails>() {
            @Override
            public void accept(ResKLineDetails res) throws Throwable {
                GuBbKLineDetailEvent event = new GuBbKLineDetailEvent(res.isSuccess(), res.getResultObj());
                event.setType(1);
                if (!res.isSuccess()){
                    event.setError(res.getMessage());
                }
                EventBus.getDefault().post(event);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                GuBbKLineDetailEvent event = new GuBbKLineDetailEvent(false, null);
                event.setError(context.getString(R.string.NetError));
                EventBus.getDefault().post(event);
            }
        });
    }

    /*
     * 请求K线秘籍文章详情
     * */
    public void RequestKLineArticle(Context context, BaseActivity baseActivity, int id){
        Observable<ResKLineDetails> observable = GuBbBasePresenter.Create().RequestKLineArticleDetail(context, id, TAG);
        observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
        observable.subscribe(new Consumer<ResKLineDetails>() {
            @Override
            public void accept(ResKLineDetails res) throws Throwable {
                GuBbKLineDetailEvent event = new GuBbKLineDetailEvent(res.isSuccess(), res.getResultObj());
                event.setType(2);
                if (!res.isSuccess()){
                    event.setError(res.getMessage());
                }
                EventBus.getDefault().post(event);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                GuBbKLineDetailEvent event = new GuBbKLineDetailEvent(false, null);
                event.setError(context.getString(R.string.NetError));
                EventBus.getDefault().post(event);
            }
        });
    }

    /*
     * K线秘籍请求学习或点赞
     * state 0=学习 1=点赞
     * */
    public void RequestKLineLearnOrPraise(Context context, BaseActivity baseActivity, int id, int state, int typeInfo, int position, int cid){
        Observable<ResJustIntObj> observable = GuBbBasePresenter.Create().RequestKLineLearnPraise(context, id, state, typeInfo, TAG);
        observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
        observable.subscribe(new Consumer<ResJustIntObj>() {
            @Override
            public void accept(ResJustIntObj res) throws Throwable {
                if (state==0){
                    //学习
                    GuBbKLineLearnEvent event = new GuBbKLineLearnEvent(res.isSuccess(), id);
                    if (!res.isSuccess()){
                        event.setError(res.getMessage());
                    }
                    EventBus.getDefault().post(event);
                    return;
                }
                if (state==1){
                    //点赞
                    boolean isPraise = true;
                    if (res.getMessage().equals("点赞成功")){
                        isPraise = true;
                    }else if (res.getMessage().equals("取消点赞成功")){
                        isPraise = false;
                    }
                    GuBbKLinePraiseEvent event = new GuBbKLinePraiseEvent(res.isSuccess(), isPraise, id, res.getCount());
                    if (!res.isSuccess()){
                        event.setError(res.getMessage());
                    }else {
                        event.setPosition(position);
                        event.setCid(cid);
                    }
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
