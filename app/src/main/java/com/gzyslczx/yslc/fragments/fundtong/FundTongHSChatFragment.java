package com.gzyslczx.yslc.fragments.fundtong;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.gzyslczx.yslc.databinding.FundTongHschatFragmentBinding;
import com.gzyslczx.yslc.events.FunTongHuShenEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.modes.response.ResFundTongHuShenModelList;
import com.gzyslczx.yslc.presenter.FundTongPresenter;
import com.gzyslczx.yslc.tools.customviews.HuShenCharView;
import com.gzyslczx.yslc.tools.customviews.LineChartView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class FundTongHSChatFragment extends BaseFragment<FundTongHschatFragmentBinding> {

    private final String TAG = "FTongChartFrag";
    public static final String ChartKey = "FTongCharType";
    private int ChartType;
    private FundTongPresenter mPresenter;
    private HuShenCharView mChartView;

    @Override
    protected void InitParentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = FundTongHschatFragmentBinding.inflate(inflater, container, false);
        InitView();
    }

    @Override
    protected void InitView() {
        EventBus.getDefault().register(this);
        ChartType = getArguments().getInt(ChartKey);
        mPresenter = new FundTongPresenter();
        mChartView = new HuShenCharView(getContext());
        mViewBinding.FundTongChartFrame.addView(mChartView);
        mPresenter.RequestHuShen(getContext(), this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    /*
     * 接收沪深三百对比数据
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnHuShenChatEvent(FunTongHuShenEvent event){
        if (event.isFlag()){
            Log.d(TAG, "接收到沪深三百对比数据");
            switch (ChartType){
                case 1:
                    //一个月
                    InitChartView(event.getData().getmList());
                    break;
                case 2:
                    //3个月
                    InitChartView(event.getData().getM3List());
                    break;
                case 3:
                    //6个月
                    InitChartView(event.getData().getM6List());
                    break;
                case 4:
                    //一年
                    InitChartView(event.getData().getyList());
                    break;
                case 5:
                    //两年
                    InitChartView(event.getData().getY2List());
                    break;
                case 6:
                    //三年
                    InitChartView(event.getData().getY3List());
                    break;
                case 7:
                    //五年
                    InitChartView(event.getData().getY5List());
                    break;
            }
        }
    }

    /*
    * 初始化图标
    * */
    private void InitChartView(List<ResFundTongHuShenModelList> data){
        float max = Float.MIN_VALUE;
        float min = Float.MAX_VALUE;
        mChartView.getChartStockDataList().clear();
        mChartView.getChartHuShenDataList().clear();
        mChartView.getChartMixedDataList().clear();
        for (ResFundTongHuShenModelList obj : data) {
            mChartView.AddStockData(Float.valueOf(obj.getGpxRate()), obj.getPublishDate());
            mChartView.AddMixedData(Float.valueOf(obj.getHhxRate()), obj.getPublishDate());
            mChartView.AddHuShenData(Float.valueOf(obj.getHsRate()), obj.getPublishDate());
            //取最大值
            max = Math.max(max, Float.valueOf(obj.getGpxRate()));
            max = Math.max(max, Float.valueOf(obj.getHhxRate()));
            max = Math.max(max, Float.valueOf(obj.getHsRate()));
            //取最小值
            min = Math.min(min, Float.valueOf(obj.getGpxRate()));
            min = Math.min(min, Float.valueOf(obj.getHhxRate()));
            min = Math.min(min, Float.valueOf(obj.getHsRate()));
        }
        mChartView.setMaxVMinV_ItemSize(max, min, data.size());
        mChartView.isInitData();
    }


}
