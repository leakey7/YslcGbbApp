package com.gzyslczx.yslc.fragments.yourui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gzyslczx.yslc.databinding.DailyStockFragmentBinding;
import com.gzyslczx.yslc.events.yourui.DailyKLineEvent;
import com.gzyslczx.yslc.events.yourui.NoticeDailyKLineLoadMoreEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.tools.yourui.DailyMAEntity;
import com.gzyslczx.yslc.tools.yourui.myviews.OnDailyLongPressListener;
import com.gzyslczx.yslc.tools.yourui.myviews.OnDailyStockLoadMoreListener;
import com.yourui.sdk.message.api.protocol.QuoteConstants;
import com.yourui.sdk.message.use.StockKLine;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;

public class MonthStockFragment extends BaseFragment<DailyStockFragmentBinding> implements OnDailyLongPressListener, OnDailyStockLoadMoreListener {

    private int OFFSET=-1;
    private int COUNT;
    private short PERIOD = QuoteConstants.PERIOD_TYPE_MONTH;
    private short REMIT = QuoteConstants.NO_REMIT_MODE;
    private DecimalFormat decimalFormat;

    @Override
    protected void InitParentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        mViewBinding = DailyStockFragmentBinding.inflate(inflater, container, false);
        InitView();
    }

    @Override
    protected void InitView() {
        mViewBinding.DailyKlineChartView.setEnableLongPress(true); //允许长按
        mViewBinding.DailyKlineChartView.setLongPressListener(this); //长按监听
        mViewBinding.TopSubChartView.setDailyLongPressListener(this); //长按显示数据
        mViewBinding.BtmSubChartView.setDailyLongPressListener(this); //长按显示数据
        mViewBinding.DailyKlineChartView.setLoadMoreListener(this::onLoadMoreDailyStock); //加载更多监听
        mViewBinding.DailyKlineChartView.setSubLink(mViewBinding.TopSubChartView); //添加副图关联
        mViewBinding.DailyKlineChartView.setSubLink(mViewBinding.BtmSubChartView); //添加副图关联
        decimalFormat = new DecimalFormat("#0.00");
        EventBus.getDefault().post(new NoticeDailyKLineLoadMoreEvent(OFFSET, PERIOD, REMIT));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnDailyKLineEvent(DailyKLineEvent event){
        if (PERIOD == event.getPeriod()) {
            if (!event.isEnd()) {
                mViewBinding.DailyKlineChartView.AddData(event.getKlineEntity().getStockKLineList());
            } else {
                mViewBinding.DailyKlineChartView.setLoadMoreEnd(event.isEnd());
            }
            if (mViewBinding.dailyStockLoadMore.getVisibility() == View.VISIBLE) {
                mViewBinding.dailyStockLoadMore.setVisibility(View.GONE);
            }
            COUNT = event.getCount();
            OFFSET = event.getOffset();
        }
    }

    @Override
    public void onDailyLongPress(boolean needTime, StockKLine stockKLine, DailyMAEntity dailyMAEntity) {
        if (dailyMAEntity.getMA5()!=-1) {
            mViewBinding.MD5Sign.setText(String.format("MA5:%s", dailyMAEntity.getMA5()));
        }else {
            mViewBinding.MD5Sign.setText(String.format("MA5:%s", "--"));
        }
        if (dailyMAEntity.getMA10()!=-1) {
            mViewBinding.MD10Sign.setText(String.format("MA10:%s", dailyMAEntity.getMA10()));
        }else {
            mViewBinding.MD10Sign.setText(String.format("MA10:%s", "--"));
        }
        if (dailyMAEntity.getMA20()!=-1) {
            mViewBinding.MD20Sign.setText(String.format("MA20:%s", dailyMAEntity.getMA20()));
        }else {
            mViewBinding.MD20Sign.setText(String.format("MA20:%s", "--"));
        }
        if (dailyMAEntity.getMA30()!=-1) {
            mViewBinding.MD30Sign.setText(String.format("MA30:%s", dailyMAEntity.getMA30()));
        }else {
            mViewBinding.MD30Sign.setText(String.format("MA30:%s", "--"));
        }
    }

    @Override
    public void onKDJLongPress(double K, double D, double J) {
        mViewBinding.KSign.setText(String.format("K:%s",decimalFormat.format(K)));
        mViewBinding.DSign.setText(String.format("D:%s",decimalFormat.format(D)));
        mViewBinding.JSign.setText(String.format("J:%s",decimalFormat.format(J)));
    }

    @Override
    public void onMACDLongPress(double MACD, double DIFF, double DEA) {
        if (MACD>0){
            mViewBinding.MACDSign.setTextColor(mViewBinding.BtmSubChartView.getUpColor());
        }else if (MACD<0){
            mViewBinding.MACDSign.setTextColor(mViewBinding.BtmSubChartView.getDownColor());
        }else {
            mViewBinding.MACDSign.setTextColor(mViewBinding.BtmSubChartView.getEqualColor());
        }
        mViewBinding.MACDSign.setText(String.format("MACD:%s", decimalFormat.format(MACD)));
        mViewBinding.DIFFSign.setText(String.format("DIFF:%s", decimalFormat.format(DIFF)));
        mViewBinding.DEASign.setText(String.format("DEA:%s", decimalFormat.format(DEA)));
    }

    @Override
    public void onLoadMoreDailyStock() {
        if (mViewBinding.dailyStockLoadMore.getVisibility()==View.GONE) {
            Log.d(getClass().getSimpleName(), "月K加载更多");
            mViewBinding.dailyStockLoadMore.setVisibility(View.VISIBLE);
            EventBus.getDefault().post(new NoticeDailyKLineLoadMoreEvent(OFFSET + COUNT, PERIOD, REMIT));
        }
    }
}
