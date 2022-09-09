package com.gzyslczx.yslc.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.adapters.main.bean.MainRecoData;
import com.gzyslczx.yslc.events.GuBbMainRecoAndVideoListEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.modes.response.ResMainReco;
import com.gzyslczx.yslc.modes.response.ResMainRecoInfo;
import com.gzyslczx.yslc.tools.ConnTool;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

public class MainRecoAndVideoPresenter {

    private final String TAG = "RecoVideoPres";
    private int CurrentPage = 1;

    /*
    * 请求推荐、视频、小视频列表
    * type：1为推荐 2为视频 3为小视频
    * */
    public void RequestRecommendList(Context context, BaseFragment baseFragment, BaseActivity baseActivity, int type){
        Observable<ResMainReco> observable = GuBbBasePresenter.Create().RequestMainRecoAndVideo(context, CurrentPage, type, TAG);
        if (baseFragment!=null){
            observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        }else {
            observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
        }
        observable.subscribe(new Consumer<ResMainReco>() {
            @Override
            public void accept(ResMainReco res) throws Throwable {
                GuBbMainRecoAndVideoListEvent event=null;
                if (res.isSuccess()){
                    boolean isEnd = false;
                    if (res.getResultObj().getCurrentPage() >= res.getResultObj().getPageCount()){
                        isEnd = true;
                        CurrentPage=res.getResultObj().getCurrentPage();
                    }else {
                        isEnd = false;
                        CurrentPage=res.getResultObj().getCurrentPage()+1;
                    }
                    List<MainRecoData> dataList = new ArrayList<MainRecoData>();
                    for (ResMainRecoInfo info : res.getResultObj().getPageInfo()){
                        dataList.add(new MainRecoData(info));
                    }
                    event = new GuBbMainRecoAndVideoListEvent(true, null, dataList, type, CurrentPage);
                    event.setListPageEnd(isEnd);
                }else {
                    event = new GuBbMainRecoAndVideoListEvent(false, res.getMessage(), null, type, CurrentPage);
                    event.setListPageEnd(false);
                }
                EventBus.getDefault().post(event);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                GuBbMainRecoAndVideoListEvent event = new GuBbMainRecoAndVideoListEvent(false, throwable.getMessage(), null, type, CurrentPage);
                event.setListPageEnd(false);
                EventBus.getDefault().post(event);
            }
        });
    }

    /*
    * 设置当前页
    * */
    public void setCurrentPage(int currentPage) {
        CurrentPage = currentPage;
    }
}
