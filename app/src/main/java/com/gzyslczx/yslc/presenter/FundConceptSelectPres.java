package com.gzyslczx.yslc.presenter;

import android.content.Context;
import android.util.Log;

import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.adapters.fundtong.bean.ConceptSelectData;
import com.gzyslczx.yslc.events.FundConceptSelectListEvent;
import com.gzyslczx.yslc.modes.response.ResConceptSelect;
import com.gzyslczx.yslc.modes.response.ResConceptSelectInfo;
import com.gzyslczx.yslc.modes.response.ResConceptSelectObj;
import com.gzyslczx.yslc.tools.ConnTool;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

public class FundConceptSelectPres {

    private final String TAG = "FundSelectPres";

    /*
     * 请求概念筛选列表
     * */
    public void RequestConceptSelectList(Context context, BaseActivity baseActivity){
        Observable<ResConceptSelect> observable = FundTongBasePresenter.Create().RequestConceptSelectList(context, TAG);
        observable = ConnTool.AddExtraReqOfAct(observable, TAG, baseActivity);
        observable.subscribe(new Consumer<ResConceptSelect>() {
            @Override
            public void accept(ResConceptSelect resConceptSelect) throws Throwable {
                if (resConceptSelect.isSuccess()){
                    List<String> WordList = new ArrayList<String>();
                    List<ConceptSelectData> DataList = new ArrayList<ConceptSelectData>();
                    for (ResConceptSelectObj obj : resConceptSelect.getResultObj()){
                        WordList.add(obj.getInitial());
                        DataList.add(new ConceptSelectData(true, false, obj.getInitial(), null));
                        for (ResConceptSelectInfo info : obj.getListInfo()){
                            ConceptSelectData data = new ConceptSelectData(false, false, obj.getInitial(), info.getName());
                            data.setCode(info.getCode());
                            DataList.add(data);
                        }
                    }
                    FundConceptSelectListEvent event = new FundConceptSelectListEvent(resConceptSelect.isSuccess(),WordList,  DataList);
                    EventBus.getDefault().post(event);
                }else {
                    FundConceptSelectListEvent event = new FundConceptSelectListEvent(resConceptSelect.isSuccess(), null, null);
                    event.setError(resConceptSelect.getMessage());
                    EventBus.getDefault().post(event);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.d(TAG, throwable.getMessage());
                FundConceptSelectListEvent event = new FundConceptSelectListEvent(false, null, null);
                event.setError(context.getString(R.string.NetError));
                EventBus.getDefault().post(event);
            }
        });
    }

}
