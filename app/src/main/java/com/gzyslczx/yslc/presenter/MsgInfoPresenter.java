package com.gzyslczx.yslc.presenter;

import android.content.Context;
import android.util.Log;

import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.events.MsgInfoListEvent;
import com.gzyslczx.yslc.events.UpdateMsgListEvent;
import com.gzyslczx.yslc.modes.response.ResJustStrObj;
import com.gzyslczx.yslc.modes.response.ResMsgInfoList;
import com.gzyslczx.yslc.tools.ConnTool;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

public class MsgInfoPresenter {

    private final String TAG = "MsgInfoPres";
    private int CurrentPage = 1;

    /*
    * 请求消息列表
    * */
    public void RequestMsgInfo(Context context, BaseActivity baseActivity, int MsgType){
        Observable<ResMsgInfoList> observable = GuBbBasePresenter.Create().RequestMsgInfoList(context, MsgType, CurrentPage, TAG);
        observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
        observable.subscribe(new Consumer<ResMsgInfoList>() {
            @Override
            public void accept(ResMsgInfoList res) throws Throwable {
                boolean isEnd = false;
                if (res.isSuccess()) {
                    if (res.getResultObj().getCurrentPage()>=res.getResultObj().getPageCount()){
                        isEnd = true;
                    }else {
                        isEnd = false;
                    }
                    MsgInfoListEvent event = new MsgInfoListEvent(true, res.getResultObj().getPageInfo());
                    event.setEnd(isEnd);
                    EventBus.getDefault().post(event);
                }else {
                    MsgInfoListEvent event = new MsgInfoListEvent(false, null);
                    event.setError(res.getMessage());
                    event.setEnd(isEnd);
                    EventBus.getDefault().post(event);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                MsgInfoListEvent event = new MsgInfoListEvent(false, null);
                event.setError(context.getString(R.string.NetError));
                event.setEnd(false);
                EventBus.getDefault().post(event);
            }
        });
    }

    public void setCurrentPage(int currentPage) {
        CurrentPage = currentPage;
    }

    public void AddCurrentPage(){
        CurrentPage++;
    }

    public int getCurrentPage() {
        return CurrentPage;
    }

    /*
    * 更新消息列表
    * */
    public void RequestUpdateMsgInfo(Context context, BaseActivity baseActivity, int id, int position){
        Observable<ResJustStrObj> observable = GuBbBasePresenter.Create().RequestUpdateMsgList(context, id, TAG);
        observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
        observable.subscribe(new Consumer<ResJustStrObj>() {
            @Override
            public void accept(ResJustStrObj resJustStrObj) throws Throwable {
                UpdateMsgListEvent event = new UpdateMsgListEvent(resJustStrObj.isSuccess());
                event.setPos(position);
                if (!resJustStrObj.isSuccess()){
                    event.setError(resJustStrObj.getMessage());
                }
                EventBus.getDefault().post(event);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                UpdateMsgListEvent event = new UpdateMsgListEvent(false);
                event.setError(context.getString(R.string.NetError));
                event.setPos(position);
                EventBus.getDefault().post(event);
            }
        });
    }

}
