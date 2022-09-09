package com.gzyslczx.yslc.presenter;

import android.content.Context;
import android.util.Log;

import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.adapters.label.bean.LabelSelfData;
import com.gzyslczx.yslc.events.LabelSelfInfoEvent;
import com.gzyslczx.yslc.events.LabelSelfListEvent;
import com.gzyslczx.yslc.modes.response.ResLabelSelf;
import com.gzyslczx.yslc.modes.response.ResMainRecoInfo;
import com.gzyslczx.yslc.tools.ConnTool;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

public class LabelSelfActPresenter {

    private final String TAG = "LabelSelfPres";

    private int CurrentPage=1;

    /*
    * 设置当前页数
    * */
    public void setCurrentPage(int currentPage) {
        CurrentPage = currentPage;
    }

    /*
    * 请求栏目专业信息
    * */
    public void RequestLabelSelf(Context context, BaseActivity baseActivity, String LabelName){
        Observable<ResLabelSelf> observable = GuBbBasePresenter.Create().RequestLabelSelf(context, CurrentPage, LabelName, TAG);
        observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
        observable.subscribe(new Consumer<ResLabelSelf>() {
            @Override
            public void accept(ResLabelSelf res) throws Throwable {
                if (res.isSuccess()){
                    //请求成功
                    boolean isEnd = false;
                    if (CurrentPage >= res.getResultObj().getPageCount()){
                        isEnd = true;
                    }else {
                        isEnd = false;
                        CurrentPage++;
                    }
                    if (res.getResultObj().getCurrentPage()==1){
                        //初始或刷新时，更新栏目数据
                        LabelSelfInfoEvent infoEvent = new LabelSelfInfoEvent(true, res.getResultObj().getPageInfo());
                        EventBus.getDefault().post(infoEvent);
                    }
                    List<LabelSelfData> data = new ArrayList<LabelSelfData>();
                    for (ResMainRecoInfo info : res.getResultObj().getPageInfo().getnList()){
                        data.add(new LabelSelfData(info));
                    }
                    LabelSelfListEvent listEvent = new LabelSelfListEvent(true, data);
                    listEvent.setEnd(isEnd);
                    EventBus.getDefault().post(listEvent);
                }else {
                    //请求失败
                    LabelSelfListEvent listEvent = new LabelSelfListEvent(false, null);
                    listEvent.setEnd(false);
                    listEvent.setError(res.getMessage());
                    EventBus.getDefault().post(listEvent);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                //请求异常
                Log.d(TAG, throwable.getMessage());
                LabelSelfListEvent listEvent = new LabelSelfListEvent(false, null);
                listEvent.setEnd(false);
                listEvent.setError(throwable.getMessage());
                EventBus.getDefault().post(listEvent);
            }
        });
    }

}
