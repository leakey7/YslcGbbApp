package com.gzyslczx.yslc.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.adapters.main.bean.FlashData;
import com.gzyslczx.yslc.events.GuBbYsFlashEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.modes.response.ResFlash;
import com.gzyslczx.yslc.modes.response.ResFlashObj;
import com.gzyslczx.yslc.tools.ConnTool;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

public class YsFlashPresenter {

    private final String TAG = "YsFlashPresenter";
    private StringBuilder stringBuilder = new StringBuilder();

    /*
    * 请求越声快讯列表
    * */
    public void RequestYsFlashList(Context context, BaseFragment baseFragment, int page, int type){
        Observable<ResFlash> observable = GuBbBasePresenter.Create().RequestFlashList(context, page, type, TAG);
        observable = ConnTool.AddExtraReqOfFrag(observable, TAG, baseFragment);
        observable.subscribe(new Consumer<ResFlash>() {
            @Override
            public void accept(ResFlash res) throws Throwable {
                if (res.isSuccess() && res.getResultObj()!=null){
                    List<FlashData> dataList = new ArrayList<FlashData>();
                    if (TextUtils.isEmpty(stringBuilder.toString())){
                        stringBuilder.append(res.getResultObj().get(0).getDate());
                        for (int i=0; i<res.getResultObj().size(); i++){
                            if (i==0){
                                dataList.add(new FlashData(FlashData.DateType, res.getResultObj().get(i)));
                                dataList.add(new FlashData(FlashData.InfoType, res.getResultObj().get(i)));
                            }else {
                                if (stringBuilder.toString().equals(res.getResultObj().get(i).getDate())) {
                                    dataList.add(new FlashData(FlashData.InfoType, res.getResultObj().get(i)));
                                }else {
                                    stringBuilder.replace(0, stringBuilder.length(), res.getResultObj().get(i).getDate());
                                    dataList.add(new FlashData(FlashData.DateType, res.getResultObj().get(i)));
                                    dataList.add(new FlashData(FlashData.InfoType, res.getResultObj().get(i)));
                                }
                            }
                        }
                    }else {
                        for (ResFlashObj obj : res.getResultObj()){
                            if (stringBuilder.toString().equals(obj.getDate())){
                                dataList.add(new FlashData(FlashData.InfoType, obj));
                            }else {
                                stringBuilder.replace(0, stringBuilder.length(), obj.getDate());
                                dataList.add(new FlashData(FlashData.DateType, obj));
                                dataList.add(new FlashData(FlashData.InfoType, obj));
                            }
                        }
                    }
                    GuBbYsFlashEvent event = new GuBbYsFlashEvent(res.isSuccess(), type, dataList);
                    EventBus.getDefault().post(event);
                }else {
                    Log.d(TAG, res.getMessage());
                    GuBbYsFlashEvent event = new GuBbYsFlashEvent(false, type, null);
                    event.setError(res.getMessage());
                    EventBus.getDefault().post(event);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                GuBbYsFlashEvent event = new GuBbYsFlashEvent(false, type, null);
                event.setError(context.getString(R.string.NetError));
                EventBus.getDefault().post(event);
            }
        });
    }

    public void ClearStringBuilder(){
        stringBuilder.delete(0, stringBuilder.length());
    }

}
