package com.gzyslczx.yslc.presenter;

import android.content.Context;
import android.util.Log;

import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.events.GuBbScrollSmallVideoEvent;
import com.gzyslczx.yslc.modes.response.ResScrollSmallVideo;
import com.gzyslczx.yslc.tools.ConnTool;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

public class GuBbSmallVideoPresenter {

    private final String TAG = "SVideoPresenter";
    private int CurrentPage = 1;

    /*
     * 请求滑动视频列表
     * */
    public void RequestScrollVideoList(Context context, BaseActivity baseActivity,  String nid){
        Observable<ResScrollSmallVideo> observable = GuBbBasePresenter.Create().RequestScrollSmallVideoList(context, nid, CurrentPage, TAG);
        observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
        observable.subscribe(new Consumer<ResScrollSmallVideo>() {
            @Override
            public void accept(ResScrollSmallVideo res) throws Throwable {
                if (res.isSuccess()){
                    boolean isEnd;
                    if (CurrentPage >= res.getResultObj().getPageSize()){
                        isEnd = true;
                    }else {
                        isEnd = false;
                        CurrentPage++;
                    }
                    GuBbScrollSmallVideoEvent event = new GuBbScrollSmallVideoEvent(true, res.getResultObj().getPageInfo(), isEnd);
                    EventBus.getDefault().post(event);
                }else {
                    GuBbScrollSmallVideoEvent event = new GuBbScrollSmallVideoEvent(false, null, false);
                    event.setError(res.getMessage());
                    EventBus.getDefault().post(event);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                GuBbScrollSmallVideoEvent event = new GuBbScrollSmallVideoEvent(false,null, false);
                event.setError(context.getString(R.string.NetError));
                EventBus.getDefault().post(event);
            }
        });

    }

    public void setCurrentPage(int currentPage) {
        CurrentPage = currentPage;
    }
}
