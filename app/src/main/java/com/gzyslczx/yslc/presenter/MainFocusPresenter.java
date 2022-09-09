package com.gzyslczx.yslc.presenter;

import android.content.Context;
import android.util.Log;

import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.adapters.main.bean.MainMyFocusData;
import com.gzyslczx.yslc.adapters.main.bean.MainRecoData;
import com.gzyslczx.yslc.events.GuBbMainMoreFocusEvent;
import com.gzyslczx.yslc.events.GuBbMainMyFocusEvent;
import com.gzyslczx.yslc.events.GuBbMainRecoAndVideoListEvent;
import com.gzyslczx.yslc.events.MyFocusInfoListEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.modes.response.ResMainRecoInfo;
import com.gzyslczx.yslc.modes.response.ResMyFocus;
import com.gzyslczx.yslc.modes.response.ResMyFocusInfoList;
import com.gzyslczx.yslc.modes.response.ResMyFocusObj;
import com.gzyslczx.yslc.tools.ConnTool;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

public class MainFocusPresenter {

    private final String TAG = "FocusPresenter";
    private int CurrentPage=1;

    /*
    * 请求我的关注
    * */
    public void RequestMyFocus(Context context, BaseFragment baseFragment){
        Observable<ResMyFocus> observable = GuBbBasePresenter.Create().RequestMainMyFocus(context, TAG);
        observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        observable.subscribe(new Consumer<ResMyFocus>() {
            @Override
            public void accept(ResMyFocus res) throws Throwable {
                if (res.isSuccess()){
                    List<MainMyFocusData> dataList = new ArrayList<MainMyFocusData>();
                    for (ResMyFocusObj obj : res.getResultObj()){
                        dataList.add(new MainMyFocusData(obj));
                    }
                    GuBbMainMyFocusEvent event = new GuBbMainMyFocusEvent(true, dataList);
                    EventBus.getDefault().post(event);
                }else {
                    GuBbMainMyFocusEvent event = new GuBbMainMyFocusEvent(false, null);
                    event.setError(res.getMessage());
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

    /*
     * 请求我的关注
     * */
    public void RequestMoreFocus(Context context, BaseFragment baseFragment){
        Observable<ResMyFocus> observable = GuBbBasePresenter.Create().RequestMainMoreFocus(context, TAG);
        observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        observable.subscribe(new Consumer<ResMyFocus>() {
            @Override
            public void accept(ResMyFocus res) throws Throwable {
                if (res.isSuccess()){
                    List<MainMyFocusData> dataList = new ArrayList<MainMyFocusData>();
                    for (ResMyFocusObj obj : res.getResultObj()){
                        dataList.add(new MainMyFocusData(obj));
                    }
                    GuBbMainMoreFocusEvent event = new GuBbMainMoreFocusEvent(true, dataList);
                    EventBus.getDefault().post(event);
                }else {
                    GuBbMainMoreFocusEvent event = new GuBbMainMoreFocusEvent(false, null);
                    event.setError(res.getMessage());
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

    /*
    * 请求关注的越声栏目
    * */
    public void RequestMyFocusInfoList(Context context, BaseFragment baseFragment){
        Observable<ResMyFocusInfoList> observable = GuBbBasePresenter.Create().RequestMyFocusInfoList(context, CurrentPage, TAG);
        observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        observable.subscribe(new Consumer<ResMyFocusInfoList>() {
            @Override
            public void accept(ResMyFocusInfoList res) throws Throwable {
                MyFocusInfoListEvent event = null;
                if (res.isSuccess()){
                    boolean isEnd = false;
                    if (res.getResultObj()!=null) {
                        if (res.getResultObj().getCurrentPage() >= res.getResultObj().getPageCount()) {
                            isEnd = true;
                            CurrentPage = res.getResultObj().getCurrentPage();
                        } else {
                            isEnd = false;
                        }
                        List<MainRecoData> dataList = new ArrayList<MainRecoData>();
                        for (ResMainRecoInfo info : res.getResultObj().getPageInfo()){
                            dataList.add(new MainRecoData(info));
                        }
                        event = new MyFocusInfoListEvent(true, isEnd, dataList);
                    }else {
                        event = new MyFocusInfoListEvent(true, false, null);
                        event.setNoData(true);
                    }
                }else {
                    event = new MyFocusInfoListEvent(false, false, null);
                    event.setError(res.getMessage());
                }
                EventBus.getDefault().post(event);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                MyFocusInfoListEvent event = new MyFocusInfoListEvent(false, false, null);
                event.setError(context.getString(R.string.NetError));
                EventBus.getDefault().post(event);
            }
        });

    }

    public void AddCurrentPage() {
        CurrentPage++;
    }

    public void setCurrentPage(int currentPage) {
        CurrentPage = currentPage;
    }

    public int getCurrentPage() {
        return CurrentPage;
    }
}
