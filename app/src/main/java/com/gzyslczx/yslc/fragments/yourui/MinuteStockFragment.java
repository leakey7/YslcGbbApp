package com.gzyslczx.yslc.fragments.yourui;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.databinding.MinuteLongPressDialogBinding;
import com.gzyslczx.yslc.databinding.MinuteStockFragmentBinding;
import com.gzyslczx.yslc.events.yourui.MinuteTrendEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.tools.DisplayTool;
import com.gzyslczx.yslc.tools.yourui.myviews.OnMinuteLongPressListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;

public class MinuteStockFragment extends BaseFragment<MinuteStockFragmentBinding> implements OnMinuteLongPressListener, View.OnClickListener {

    private DecimalFormat decimalFormat;
    private FiveDayStockFragment fiveDayStockFragment;
    private MinuteDealDetailFragment minuteDealDetailFragment;
    private MinuteLongPressDialogBinding longPressDialogBinding;
    private PopupWindow longPressWindow;


    @Override
    protected void InitParentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        mViewBinding = MinuteStockFragmentBinding.inflate(inflater, container, false);
        InitView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (longPressWindow!=null && longPressWindow.isShowing()){
            longPressWindow.dismiss();
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void InitView() {
        mViewBinding.MinuteChartView.setMinuteVolumeLink(mViewBinding.VolumeChartView);
        mViewBinding.MinuteChartView.setEnableLongPress(true);
        mViewBinding.MinuteChartView.setLongPressListener(this);
        ChangeSubFragment(0);
        mViewBinding.FiveRange.setOnClickListener(this::onClick);
        mViewBinding.TheDetail.setOnClickListener(this::onClick);
        decimalFormat = new DecimalFormat("#0.00");
        if (longPressDialogBinding==null){
            longPressDialogBinding = MinuteLongPressDialogBinding.bind(LayoutInflater.from(getContext()).inflate(R.layout.minute_long_press_dialog, null));
        }
    }

    /*
     * 分时图数据
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMinuteChartEvent(MinuteTrendEvent event){
        Log.d(getClass().getSimpleName(), "接收到分时图Event");
        if (event.getTrendExtEntity()!=null){
            if (event.getTrendExtEntity().getTrendDataModelList()!=null) {
                mViewBinding.MinuteChartView.SetDataList(event.getTrendExtEntity().getTrendDataModelList(),
                        event.getTrendExtEntity().getPreClosePrice(), //昨收价
                        event.getTrendExtEntity().getMaxPrice(), event.getTrendExtEntity().getMinPrice()); //最高价，最低价
                int size = event.getTrendExtEntity().getTrendDataModelList().size();
                float ave = event.getTrendExtEntity().getTrendDataModelList().get(size-1).getAvgPrice();
                double gain = (event.getTrendExtEntity().getNewPrice()-event.getTrendExtEntity().getPreClosePrice())
                        /event.getTrendExtEntity().getPreClosePrice()*100f;
                if (!mViewBinding.MinuteChartView.isLongPress()){
                    UpDateRealValue(ave, event.getTrendExtEntity().getmNewPrice(), gain, false, null); //更新分时图实时数值
                    //更新成交量实时数值
                    Log.d(getClass().getSimpleName(), "当前数据量:"+event.getTrendExtEntity().getTrendDataModelList().size());
                    if (event.getTrendExtEntity().getTrendDataModelList().size()==1){
                        UpdateVolumeValue(event.getTrendExtEntity().getmNewPrice(),
                                event.getTrendExtEntity().getTrendDataModelList().get(0).getTradeAmount(), event.getTrendExtEntity().getPreClosePrice());
                    }else {
                        UpdateVolumeValue(event.getTrendExtEntity().getTrendDataModelList().get(size-1).getPrice(),
                                event.getTrendExtEntity().getTrendDataModelList().get(size-1).getTradeAmount(),
                                event.getTrendExtEntity().getTrendDataModelList().get(size-2).getPrice());
                    }
                }
            }else {
                //分时图数据集为空
                Log.d(getClass().getSimpleName(), "分时图数据集空");
            }
        }
    }



    /*
     * 更新实时数值
     * */
    private void UpDateRealValue(float avePrice, double newPrice, double gainValue, boolean needTime, String time){
        mViewBinding.AvePriceSign.setText(String.format("均价:%s", decimalFormat.format(avePrice)));
        mViewBinding.RealPriceSign.setText(String.format("最新:%s", decimalFormat.format(newPrice)));
        longPressDialogBinding.realPri.setText(decimalFormat.format(newPrice));
        longPressDialogBinding.avePri.setText(String.format("均价:%s", decimalFormat.format(avePrice)));
        if (gainValue<0){
            mViewBinding.GainSign.setTextColor(ContextCompat.getColor(getContext(), R.color.green_down));
            longPressDialogBinding.diffPri.setTextColor(ContextCompat.getColor(getContext(), R.color.green_down));
            longPressDialogBinding.diffGain.setTextColor(ContextCompat.getColor(getContext(), R.color.green_down));
        }else if (gainValue>0){
            mViewBinding.GainSign.setTextColor(ContextCompat.getColor(getContext(), R.color.red_up));
            longPressDialogBinding.diffPri.setTextColor(ContextCompat.getColor(getContext(), R.color.red_up));
            longPressDialogBinding.diffGain.setTextColor(ContextCompat.getColor(getContext(), R.color.red_up));
        }else {
            mViewBinding.GainSign.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_666));
            longPressDialogBinding.diffPri.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_666));
            longPressDialogBinding.diffGain.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_666));
        }
        mViewBinding.GainSign.setText(String.format("涨幅:%s%%", decimalFormat.format(gainValue)));
        longPressDialogBinding.diffGain.setText(String.format("涨跌幅:%s%%", decimalFormat.format(gainValue)));
        if (needTime){
            mViewBinding.TimeSign.setText(time);
            longPressDialogBinding.dealTime.setText(time);
        }else {
            mViewBinding.TimeSign.setText("");
        }
    }

    /*
     * 更新成交量数据
     * */
    private void UpdateVolumeValue(double newPrice, long volume, double oldPrice){
        if (oldPrice==newPrice){
            mViewBinding.VolumeSign.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_666));
            longPressDialogBinding.volNum.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_666));
        }else if (oldPrice>newPrice){
            mViewBinding.VolumeSign.setTextColor(ContextCompat.getColor(getContext(), R.color.green_down));
            longPressDialogBinding.volNum.setTextColor(ContextCompat.getColor(getContext(), R.color.green_down));
        }else {
            mViewBinding.VolumeSign.setTextColor(ContextCompat.getColor(getContext(), R.color.red_up));
            longPressDialogBinding.volNum.setTextColor(ContextCompat.getColor(getContext(), R.color.red_up));
        }
        mViewBinding.VolumeSign.setText(String.format("成交量:%s手", volume));
        longPressDialogBinding.volNum.setText(String.format("成交量:%s手", volume));
        double amount = newPrice*volume*100f;
        mViewBinding.VolumeCountSign.setText(String.format("成交额:%s", ShiftUnit(amount)));
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

    /*
    * 更换副Fragment
    * */
    private void ChangeSubFragment(int index){
        if (index==0){
            Log.d(getClass().getSimpleName(), "显示五档Fragment");
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            if (fiveDayStockFragment==null){
                fiveDayStockFragment = new FiveDayStockFragment();
                fragmentTransaction.add(mViewBinding.MinuteStockSub.getId(), fiveDayStockFragment);
            }else {
                fragmentTransaction.show(fiveDayStockFragment);
            }
            fragmentTransaction.commit();
            return;
        }else if (index==1){
            Log.d(getClass().getSimpleName(), "显示明细Fragment");
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            if (minuteDealDetailFragment==null){
                minuteDealDetailFragment = new MinuteDealDetailFragment();
                fragmentTransaction.add(mViewBinding.MinuteStockSub.getId(), minuteDealDetailFragment);
            }else {
                fragmentTransaction.show(minuteDealDetailFragment);
            }
            fragmentTransaction.commit();
            return;
        }
    }

    /*
    * 隐藏副Fragment
    * */
    private void HiddenSubFragment(int index){
        if (index==0){
            if (fiveDayStockFragment!=null){
                Log.d(getClass().getSimpleName(), "隐藏五档Fragment");
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(fiveDayStockFragment);
                fragmentTransaction.commit();
            }
            return;
        }else if (index==1){
            if (minuteDealDetailFragment!=null){
                Log.d(getClass().getSimpleName(), "隐藏明细Fragment");
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(minuteDealDetailFragment);
                fragmentTransaction.commit();
            }
            return;
        }
    }

    /*
    * 长按监听
    * */
    @Override
    public void onMinuteLongPress(float yesterdayPrice, double realPrice, float avePrice, double gain, boolean needTime, String time, long volume, double oldPrice) {
        //长按滑动
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
            longPressWindow.showAtLocation(mViewBinding.MinuteSignBg, Gravity.TOP, 0, 0);
        }
    }

    @Override
    public void onCancelMinuteLongPress() {
        //取消长按
        if (longPressWindow!=null){
            longPressWindow.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.FiveRange:
                //点击了五档
                Log.d(getClass().getSimpleName(), "点击了五档");
                HiddenSubFragment(1);
                ChangeSubFragment(0);
                mViewBinding.TheDetail.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                mViewBinding.TheDetail.setTextSize(12);
                mViewBinding.TheDetail.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_666));
                mViewBinding.FiveRange.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.pink_corner_4_shape));
                mViewBinding.FiveRange.setTextSize(14);
                mViewBinding.FiveRange.setTextColor(ContextCompat.getColor(getContext(), R.color.main_red));
                break;
            case R.id.TheDetail:
                //点击了明细
                Log.d(getClass().getSimpleName(), "点击了明细");
                HiddenSubFragment(0);
                ChangeSubFragment(1);
                mViewBinding.FiveRange.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                mViewBinding.FiveRange.setTextSize(12);
                mViewBinding.FiveRange.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_666));
                mViewBinding.TheDetail.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.pink_corner_4_shape));
                mViewBinding.TheDetail.setTextSize(14);
                mViewBinding.TheDetail.setTextColor(ContextCompat.getColor(getContext(), R.color.main_red));
                break;
        }
    }
}
