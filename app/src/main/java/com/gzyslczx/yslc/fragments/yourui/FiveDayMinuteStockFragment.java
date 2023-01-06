package com.gzyslczx.yslc.fragments.yourui;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.databinding.FivedayMinuteStockFragmentBinding;
import com.gzyslczx.yslc.databinding.MinuteLongPressDialogBinding;
import com.gzyslczx.yslc.events.yourui.FiveDayMinuteEvent;
import com.gzyslczx.yslc.events.yourui.NoticeFiveDayMinuteEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.tools.DisplayTool;
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
    private boolean isLoop = false;

    private MinuteLongPressDialogBinding longPressDialogBinding;
    private PopupWindow longPressWindow;

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
        mViewBinding.FiveDayMinuteChart.setLongPressListener(this);
        mViewBinding.FiveDayMinuteChart.setMinuteVolumeLink(mViewBinding.FiveDayMinuteVolumeChartView);
        ymdFormat = new SimpleDateFormat("yyyyMMdd");
        decimalFormat = new DecimalFormat("#0.00");
        cal=Calendar.getInstance();
        cal.add(Calendar.DATE,0);
        date = Integer.valueOf(ymdFormat.format(cal.getTime()));
        if (longPressDialogBinding==null){
            longPressDialogBinding = MinuteLongPressDialogBinding.bind(LayoutInflater.from(getContext()).inflate(R.layout.minute_long_press_dialog, null));
        }
        EventBus.getDefault().post(new NoticeFiveDayMinuteEvent(date));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (longPressWindow!=null && longPressWindow.isShowing()){
            longPressWindow.dismiss();
        }
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
                if (mViewBinding.FiveDayMinuteChart.getHisData1()!=null) {
                    isLoop = true;
                    RequestOnLoop();
                }
                return;
            }
            cal.add(Calendar.DATE, -1);
            date = Integer.valueOf(ymdFormat.format(cal.getTime()));
            EventBus.getDefault().post(new NoticeFiveDayMinuteEvent(date));
        }else {
            //获取历史分时数据有效
            if (isLoop){
                mViewBinding.FiveDayMinuteChart.UpdateHisData1(event.getHisTrendExtEntity());
                UpdateNewValue(event.getHisTrendExtEntity());
                mViewBinding.FiveDayMinuteChart.invalidate();
                mViewBinding.FiveDayMinuteVolumeChartView.invalidate();
            }
            mViewBinding.FiveDayMinuteChart.AddFiveDayMinuteData(event.getHisTrendExtEntity());
            if (count<4){
                //不够五日，继续请求
                num=0;
                ++count;
                cal.add(Calendar.DATE, -1);
                date = Integer.valueOf(ymdFormat.format(cal.getTime()));
                EventBus.getDefault().post(new NoticeFiveDayMinuteEvent(date));
                if (count==1){
                    UpdateNewValue(event.getHisTrendExtEntity());
                }
            }else {
                //足够五日
                Log.d(TAG, "已满五日");
                mViewBinding.FiveDayMinuteChart.invalidate();
                mViewBinding.FiveDayMinuteVolumeChartView.invalidate();
                isLoop = true;
                RequestOnLoop();
            }
        }
    }

    @Override
    public void onMinuteLongPress(float yesterdayPrice, double realPrice, float avePrice, double gain, boolean needTime, String time, long volume, double oldPrice) {
        //长按滑动
        if (longPressDialogBinding==null){
            longPressDialogBinding = MinuteLongPressDialogBinding.bind(LayoutInflater.from(getContext()).inflate(R.layout.minute_long_press_dialog, null));
        }
        if (longPressWindow==null){
            longPressWindow = new PopupWindow();
            longPressWindow.setHeight(DisplayTool.dp2px(getContext(), 186));
            longPressWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            longPressWindow.setOutsideTouchable(false);
            longPressWindow.setContentView(longPressDialogBinding.getRoot());
        }
        UpDateRealValue(avePrice, realPrice, gain, needTime, time);
        UpdateVolumeValue(realPrice, volume, oldPrice);
        longPressDialogBinding.lastPri.setText(String.format("昨收价:%s", decimalFormat.format(yesterdayPrice)));
        longPressDialogBinding.diffPri.setText(String.format("涨跌值:%s", decimalFormat.format(realPrice-yesterdayPrice)));
        if (!longPressWindow.isShowing()){
            longPressWindow.showAtLocation(mViewBinding.FiveDayMinuteSignBg, Gravity.TOP, 0, 0);
        }
    }

    @Override
    public void onCancelMinuteLongPress() {
        //取消长按
        if (longPressWindow!=null){
            longPressWindow.dismiss();
        }
    }

    /*
     * 更新实时数值
     * */
    private void UpDateRealValue(float avePrice, double newPrice, double gainValue, boolean needTime, String time){
        mViewBinding.FiveDayAvePriceSign.setText(String.format("均价:%s", decimalFormat.format(avePrice)));
        mViewBinding.FiveDayRealPriceSign.setText(String.format("最新:%s", decimalFormat.format(newPrice)));
        longPressDialogBinding.realPri.setText(decimalFormat.format(newPrice));
        longPressDialogBinding.avePri.setText(String.format("均价:%s", decimalFormat.format(avePrice)));
        if (gainValue<0){
            mViewBinding.FiveDayGainSign.setTextColor(ContextCompat.getColor(getContext(), R.color.green_down));
            longPressDialogBinding.diffPri.setTextColor(ContextCompat.getColor(getContext(), R.color.green_down));
            longPressDialogBinding.diffGain.setTextColor(ContextCompat.getColor(getContext(), R.color.green_down));
        }else if (gainValue>0){
            mViewBinding.FiveDayGainSign.setTextColor(ContextCompat.getColor(getContext(), R.color.red_up));
            longPressDialogBinding.diffPri.setTextColor(ContextCompat.getColor(getContext(), R.color.red_up));
            longPressDialogBinding.diffGain.setTextColor(ContextCompat.getColor(getContext(), R.color.red_up));
        }else {
            mViewBinding.FiveDayGainSign.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_666));
            longPressDialogBinding.diffPri.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_666));
            longPressDialogBinding.diffGain.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_666));
        }
        mViewBinding.FiveDayGainSign.setText(String.format("涨幅:%s", decimalFormat.format(gainValue)));
        longPressDialogBinding.diffGain.setText(String.format("涨跌幅:%s%%", decimalFormat.format(gainValue)));
        if (needTime){
            mViewBinding.FiveDayTimeSign.setText(time);
            longPressDialogBinding.dealTime.setText(time);
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
            longPressDialogBinding.volNum.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_666));
        }else if (oldPrice>newPrice){
            mViewBinding.FiveDayVolumeSign.setTextColor(ContextCompat.getColor(getContext(), R.color.green_down));
            longPressDialogBinding.volNum.setTextColor(ContextCompat.getColor(getContext(), R.color.green_down));
        }else {
            mViewBinding.FiveDayVolumeSign.setTextColor(ContextCompat.getColor(getContext(), R.color.red_up));
            longPressDialogBinding.volNum.setTextColor(ContextCompat.getColor(getContext(), R.color.red_up));
        }
        mViewBinding.FiveDayVolumeSign.setText(String.format("成交量:%s手", volume));
        longPressDialogBinding.volNum.setText(String.format("成交量:%s手", volume));
        double amount = newPrice*volume*100f;
        mViewBinding.FiveDayVolumeCountSign.setText(String.format("成交额:%s", ShiftUnit(amount)));
        longPressDialogBinding.volCount.setText(String.format("成交额:%s", ShiftUnit(amount)));
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

    private void RequestOnLoop(){
        cal.clear();
        cal.add(Calendar.DATE, 0);
        date = Integer.valueOf(ymdFormat.format(cal.getTime()));
        EventBus.getDefault().post(new NoticeFiveDayMinuteEvent(date, true));
    }

    private void UpdateNewValue(HisTrendExtEntity hisTrendExtEntity){
        int index = hisTrendExtEntity.getTrendDataModelList().size()-1;
        double gain = (hisTrendExtEntity.getTrendDataModelList().get(index).getPrice() - hisTrendExtEntity.getPreClosePrice())
                / hisTrendExtEntity.getPreClosePrice() * 100f;
        UpDateRealValue(hisTrendExtEntity.getTrendDataModelList().get(index).getAvgPrice(),
                hisTrendExtEntity.getTrendDataModelList().get(index).getPrice(), gain, true,
                hisTrendExtEntity.getTrendDataModelList().get(index).getTime());
        UpdateVolumeValue(hisTrendExtEntity.getTrendDataModelList().get(index).getPrice(),
                hisTrendExtEntity.getTrendDataModelList().get(index).getTradeAmount(),
                hisTrendExtEntity.getTrendDataModelList().get(index-1).getPrice());
    }

}
