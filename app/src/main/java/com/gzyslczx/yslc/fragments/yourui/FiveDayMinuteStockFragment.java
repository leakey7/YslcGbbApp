package com.gzyslczx.yslc.fragments.yourui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.databinding.FivedayMinuteStockFragmentBinding;
import com.gzyslczx.yslc.events.yourui.FiveDayMinuteEvent;
import com.gzyslczx.yslc.events.yourui.NoticeFiveDayMinuteEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.tools.yourui.HisTrendExtEntity;
import com.gzyslczx.yslc.tools.yourui.myviews.OnMinuteLongPressListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FiveDayMinuteStockFragment extends BaseFragment<FivedayMinuteStockFragmentBinding> implements OnMinuteLongPressListener {

    private int date;

    private final String TAG = "FiveMinuteStock";

    private SimpleDateFormat ymdFormat;
    private DecimalFormat decimalFormat;
    private Calendar cal;
    private int count=0, num=0;

    @Override
    protected void InitParentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        mViewBinding = FivedayMinuteStockFragmentBinding.inflate(inflater, container, false);
        InitView();
    }

    @Override
    protected void InitView() {
        mViewBinding.FiveDayMinuteChart.setFiveDay(true);
        mViewBinding.FiveDayMinuteChart.setEnableLongPress(true);
        mViewBinding.FiveDayMinuteChart.setLongPressListener(this::onMinuteLongPress);
        mViewBinding.FiveDayMinuteChart.setMinuteVolumeLink(mViewBinding.FiveDayMinuteVolumeChartView);
        ymdFormat = new SimpleDateFormat("yyyyMMdd");
        decimalFormat = new DecimalFormat("#0.00");
        cal=Calendar.getInstance();
        cal.add(Calendar.DATE,0);
        date = Integer.valueOf(ymdFormat.format(cal.getTime()));
        EventBus.getDefault().post(new NoticeFiveDayMinuteEvent(date));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFiveDayMinuteEvent(FiveDayMinuteEvent event){
        if (event.getDate() == 0){
            //获取历史分时数据无效
            num++;
            if (num==10){
                Log.d(TAG, "连续十次访问数据无效");
                num=0;
                mViewBinding.FiveDayMinuteChart.invalidate();
                mViewBinding.FiveDayMinuteVolumeChartView.invalidate();
                return;
            }
            cal.add(Calendar.DATE, -1);
            date = Integer.valueOf(ymdFormat.format(cal.getTime()));
            EventBus.getDefault().post(new NoticeFiveDayMinuteEvent(date));
        }else {
            //获取历史分时数据有效
            mViewBinding.FiveDayMinuteChart.AddFiveDayMinuteData(event.getHisTrendExtEntity());
            if (count<4){
                //不够五日，继续请求
                num=0;
                ++count;
                cal.add(Calendar.DATE, -1);
                date = Integer.valueOf(ymdFormat.format(cal.getTime()));
                EventBus.getDefault().post(new NoticeFiveDayMinuteEvent(date));
                if (count==1){
                    int index = event.getHisTrendExtEntity().getTrendDataModelList().size()-1;
                    double gain = (event.getHisTrendExtEntity().getTrendDataModelList().get(index).getPrice() - event.getHisTrendExtEntity().getPreClosePrice())
                            / event.getHisTrendExtEntity().getPreClosePrice() * 100f;
                    UpDateRealValue(event.getHisTrendExtEntity().getTrendDataModelList().get(index).getAvgPrice(),
                            event.getHisTrendExtEntity().getTrendDataModelList().get(index).getPrice(), gain, true,
                            event.getHisTrendExtEntity().getTrendDataModelList().get(index).getTime());
                    UpdateVolumeValue(event.getHisTrendExtEntity().getTrendDataModelList().get(index).getPrice(),
                            event.getHisTrendExtEntity().getTrendDataModelList().get(index).getTradeAmount(),
                            event.getHisTrendExtEntity().getTrendDataModelList().get(index-1).getPrice());
                }
            }else {
                //足够五日
                Log.d(TAG, "已满五日");
                mViewBinding.FiveDayMinuteChart.invalidate();
                mViewBinding.FiveDayMinuteVolumeChartView.invalidate();
            }
        }
    }

    @Override
    public void onMinuteLongPress(double realPrice, float avePrice, double gain, boolean needTime, String time, long volume, double oldPrice) {
        UpDateRealValue(avePrice, realPrice, gain, needTime, time);
        UpdateVolumeValue(realPrice, volume, oldPrice);
    }

    /*
     * 更新实时数值
     * */
    private void UpDateRealValue(float avePrice, double newPrice, double gainValue, boolean needTime, String time){
        mViewBinding.FiveDayAvePriceSign.setText(String.format("均价:%s", decimalFormat.format(avePrice)));
        mViewBinding.FiveDayRealPriceSign.setText(String.format("最新:%s", decimalFormat.format(newPrice)));
        if (gainValue<0){
            mViewBinding.FiveDayGainSign.setTextColor(ContextCompat.getColor(getContext(), R.color.green_down));
        }else if (gainValue>0){
            mViewBinding.FiveDayGainSign.setTextColor(ContextCompat.getColor(getContext(), R.color.red_up));
        }else {
            mViewBinding.FiveDayGainSign.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_666));
        }
        mViewBinding.FiveDayGainSign.setText(String.format("涨幅:%s", decimalFormat.format(gainValue)));
        if (needTime){
            mViewBinding.FiveDayTimeSign.setText(time);
        }else {
            mViewBinding.FiveDayTimeSign.setText("");
        }
    }

    /*
     * 更新成交量数据
     * */
    private void UpdateVolumeValue(double newPrice, long volume, double oldPrice){
        if (oldPrice==newPrice){
            mViewBinding.FiveDayVolumeSign.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_666));
        }else if (oldPrice>newPrice){
            mViewBinding.FiveDayVolumeSign.setTextColor(ContextCompat.getColor(getContext(), R.color.green_down));
        }else {
            mViewBinding.FiveDayVolumeSign.setTextColor(ContextCompat.getColor(getContext(), R.color.red_up));
        }
        mViewBinding.FiveDayVolumeSign.setText(String.format("成交量:%s手", volume));
        double amount = newPrice*volume*100f;
        mViewBinding.FiveDayVolumeCountSign.setText(String.format("成交额:%s", ShiftUnit(amount)));
    }
    /*
     * 单位转换
     * */
    @NonNull
    private String ShiftUnit(double number){
        double millionsUnit = Math.round(number)/10000f; //万单位-最大百万
        if (millionsUnit>=1000){
            double hundredMillion = Math.round(number)/100000000f; //亿单位
            return decimalFormat.format(hundredMillion)+"亿"; //金额 - 单位亿
        }else {
            return decimalFormat.format(millionsUnit)+"万"; //金额 - 单位万
        }
    }

}
