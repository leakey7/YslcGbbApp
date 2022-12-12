package com.gzyslczx.yslc.fragments.yourui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.adapters.stockmarket.StockSubTypeListAdapter;
import com.gzyslczx.yslc.databinding.DailyStockFragmentBinding;
import com.gzyslczx.yslc.databinding.StockSubTypeListBinding;
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

public class DailyStockFragment extends BaseFragment<DailyStockFragmentBinding> implements OnDailyLongPressListener, OnDailyStockLoadMoreListener,
        View.OnClickListener, OnItemClickListener {

    private final String TAG = "DStockFrag";

    private int OFFSET = -1;
    private int COUNT;
    private short PERIOD = QuoteConstants.PERIOD_TYPE_DAY;
    private short REMIT = QuoteConstants.NO_REMIT_MODE;
    private DecimalFormat decimalFormat;
    private StockSubTypeListAdapter subTypeListAdapter;
    private PopupWindow subTypePop;
    private StockSubTypeListBinding subTypeListBinding;
    private int SelectSubSign = -1;

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
        //上附图左标志设置点击监听
        mViewBinding.TopSubSetSign.setTag("0");
        mViewBinding.TopSubSetBg.setOnClickListener(this::onClick);
        mViewBinding.TopSubSetSign.setOnClickListener(this::onClick);
        mViewBinding.TopSubSetImg.setOnClickListener(this::onClick);
        //下附图左标志设置点击监听
        mViewBinding.BtmSubSetSign.setTag("0");
        mViewBinding.BtmSubSetBg.setOnClickListener(this::onClick);
        mViewBinding.BtmSubSetSign.setOnClickListener(this::onClick);
        mViewBinding.BtmSubSetImg.setOnClickListener(this::onClick);
        decimalFormat = new DecimalFormat("#0.00");
        EventBus.getDefault().post(new NoticeDailyKLineLoadMoreEvent(OFFSET, PERIOD, REMIT));
    }

    @Override
    public void onDestroy() {
        if (subTypePop != null && subTypePop.isShowing()) {
            subTypePop.dismiss();
        }
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    /*
     * 更新日K数据
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnDailyKLineEvent(DailyKLineEvent event) {
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
        if (dailyMAEntity.getMA5() != -1) {
            mViewBinding.MD5Sign.setText(String.format("MA5:%s", dailyMAEntity.getMA5()));
        } else {
            mViewBinding.MD5Sign.setText(String.format("MA5:%s", "--"));
        }
        if (dailyMAEntity.getMA10() != -1) {
            mViewBinding.MD10Sign.setText(String.format("MA10:%s", dailyMAEntity.getMA10()));
        } else {
            mViewBinding.MD10Sign.setText(String.format("MA10:%s", "--"));
        }
        if (dailyMAEntity.getMA20() != -1) {
            mViewBinding.MD20Sign.setText(String.format("MA20:%s", dailyMAEntity.getMA20()));
        } else {
            mViewBinding.MD20Sign.setText(String.format("MA20:%s", "--"));
        }
        if (dailyMAEntity.getMA30() != -1) {
            mViewBinding.MD30Sign.setText(String.format("MA30:%s", dailyMAEntity.getMA30()));
        } else {
            mViewBinding.MD30Sign.setText(String.format("MA30:%s", "--"));
        }
    }

    @Override
    public void onKDJLongPress(double K, double D, double J) {
        if (mViewBinding.TopSubSetSign.getText().toString().equals("KDJ")) {
            mViewBinding.KSign.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            mViewBinding.KSign.setText(String.format("K:%s", decimalFormat.format(K)));
            mViewBinding.DSign.setTextColor(ContextCompat.getColor(getContext(), R.color.orange_FF8C00));
            mViewBinding.DSign.setText(String.format("D:%s", decimalFormat.format(D)));
            mViewBinding.JSign.setTextColor(ContextCompat.getColor(getContext(), R.color.pink_FF69B4));
            mViewBinding.JSign.setText(String.format("J:%s", decimalFormat.format(J)));
        }
        if (mViewBinding.BtmSubSetSign.getText().toString().equals("KDJ")){
            mViewBinding.MACDSign.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            mViewBinding.MACDSign.setText(String.format("K:%s", decimalFormat.format(K)));
            mViewBinding.DIFFSign.setTextColor(ContextCompat.getColor(getContext(), R.color.orange_FF8C00));
            mViewBinding.DIFFSign.setText(String.format("D:%s", decimalFormat.format(D)));
            mViewBinding.DEASign.setTextColor(ContextCompat.getColor(getContext(), R.color.pink_FF69B4));
            mViewBinding.DEASign.setText(String.format("J:%s", decimalFormat.format(J)));
        }
    }

    @Override
    public void onMACDLongPress(double MACD, double DIFF, double DEA) {
        if (mViewBinding.BtmSubSetSign.getText().toString().equals("MACD")) {
            if (MACD > 0) {
                mViewBinding.MACDSign.setTextColor(mViewBinding.BtmSubChartView.getUpColor());
            } else if (MACD < 0) {
                mViewBinding.MACDSign.setTextColor(mViewBinding.BtmSubChartView.getDownColor());
            } else {
                mViewBinding.MACDSign.setTextColor(mViewBinding.BtmSubChartView.getEqualColor());
            }
            mViewBinding.MACDSign.setText(String.format("MACD:%s", decimalFormat.format(MACD)));
            mViewBinding.DIFFSign.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            mViewBinding.DIFFSign.setText(String.format("DIFF:%s", decimalFormat.format(DIFF)));
            mViewBinding.DEASign.setTextColor(ContextCompat.getColor(getContext(), R.color.orange_FF8C00));
            mViewBinding.DEASign.setText(String.format("DEA:%s", decimalFormat.format(DEA)));
        }
        if (mViewBinding.TopSubSetSign.getText().toString().equals("MACD")){
            if (MACD > 0) {
                mViewBinding.KSign.setTextColor(mViewBinding.BtmSubChartView.getUpColor());
            } else if (MACD < 0) {
                mViewBinding.KSign.setTextColor(mViewBinding.BtmSubChartView.getDownColor());
            } else {
                mViewBinding.KSign.setTextColor(mViewBinding.BtmSubChartView.getEqualColor());
            }
            mViewBinding.KSign.setText(String.format("MACD:%s", decimalFormat.format(MACD)));
            mViewBinding.DSign.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            mViewBinding.DSign.setText(String.format("DIFF:%s", decimalFormat.format(DIFF)));
            mViewBinding.JSign.setTextColor(ContextCompat.getColor(getContext(), R.color.orange_FF8C00));
            mViewBinding.JSign.setText(String.format("DEA:%s", decimalFormat.format(DEA)));
        }
    }

    @Override
    public void onVOLLongPress(long volume, long money, int color) {
        if (mViewBinding.TopSubSetSign.getText().toString().equals("成交量")){
            if (color==0){
                mViewBinding.KSign.setTextColor(mViewBinding.BtmSubChartView.getEqualColor());
            }else if (color==1) {
                mViewBinding.KSign.setTextColor(mViewBinding.BtmSubChartView.getUpColor());
            }else {
                mViewBinding.KSign.setTextColor(mViewBinding.BtmSubChartView.getDownColor());
            }
            mViewBinding.KSign.setText(String.format("成交量:%s手", CountUnit(volume)));
            mViewBinding.DSign.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            mViewBinding.DSign.setText(String.format("成交额:%s", CountUnit(money)));
            mViewBinding.JSign.setText("");
        }
        if (mViewBinding.BtmSubSetSign.getText().toString().equals("成交量")){
            if (color==0){
                mViewBinding.MACDSign.setTextColor(mViewBinding.BtmSubChartView.getEqualColor());
            }else if (color==1) {
                mViewBinding.MACDSign.setTextColor(mViewBinding.BtmSubChartView.getUpColor());
            }else {
                mViewBinding.MACDSign.setTextColor(mViewBinding.BtmSubChartView.getDownColor());
            }
            mViewBinding.MACDSign.setText(String.format("成交量:%s手", CountUnit(volume)));
            mViewBinding.DIFFSign.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            mViewBinding.DIFFSign.setText(String.format("成交额:%s", CountUnit(money)));
            mViewBinding.DEASign.setText("");
        }
    }

    @Override
    public void onBOLLLongPress(double M, double U, double D) {
        if (mViewBinding.TopSubSetSign.getText().toString().equals("BOLL")) {
            mViewBinding.KSign.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            mViewBinding.KSign.setText(String.format("MB:%s", decimalFormat.format(M)));
            mViewBinding.DSign.setTextColor(ContextCompat.getColor(getContext(), R.color.orange_FF8C00));
            mViewBinding.DSign.setText(String.format("UP:%s", decimalFormat.format(U)));
            mViewBinding.JSign.setTextColor(ContextCompat.getColor(getContext(), R.color.pink_FF69B4));
            mViewBinding.JSign.setText(String.format("DN:%s", decimalFormat.format(D)));
        }
        if (mViewBinding.BtmSubSetSign.getText().toString().equals("BOLL")){
            mViewBinding.MACDSign.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            mViewBinding.MACDSign.setText(String.format("MB:%s", decimalFormat.format(M)));
            mViewBinding.DIFFSign.setTextColor(ContextCompat.getColor(getContext(), R.color.orange_FF8C00));
            mViewBinding.DIFFSign.setText(String.format("UP:%s", decimalFormat.format(U)));
            mViewBinding.DEASign.setTextColor(ContextCompat.getColor(getContext(), R.color.pink_FF69B4));
            mViewBinding.DEASign.setText(String.format("DN:%s", decimalFormat.format(D)));
        }
    }

    @Override
    public void onASILongPress(double asi, double asit) {
        if (mViewBinding.TopSubSetSign.getText().toString().equals("ASI")) {
            mViewBinding.KSign.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            mViewBinding.KSign.setText(String.format("ASI:%s", decimalFormat.format(asi)));
            mViewBinding.DSign.setTextColor(ContextCompat.getColor(getContext(), R.color.orange_FF8C00));
            mViewBinding.DSign.setText(String.format("ASIMA:%s", decimalFormat.format(asit)));
            mViewBinding.JSign.setText("");
        }
        if (mViewBinding.BtmSubSetSign.getText().toString().equals("ASI")){
            mViewBinding.MACDSign.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            mViewBinding.MACDSign.setText(String.format("ASI:%s", decimalFormat.format(asi)));
            mViewBinding.DIFFSign.setTextColor(ContextCompat.getColor(getContext(), R.color.orange_FF8C00));
            mViewBinding.DIFFSign.setText(String.format("ASIMA:%s", decimalFormat.format(asit)));
            mViewBinding.DEASign.setText("");
        }
    }

    @Override
    public void onWRLongPress(double wr14, double wr28) {
        if (mViewBinding.TopSubSetSign.getText().toString().equals("WR")) {
            mViewBinding.KSign.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            mViewBinding.KSign.setText(String.format("WR14:%s", decimalFormat.format(wr14)));
            mViewBinding.DSign.setTextColor(ContextCompat.getColor(getContext(), R.color.orange_FF8C00));
            mViewBinding.DSign.setText(String.format("WR28:%s", decimalFormat.format(wr28)));
            mViewBinding.JSign.setText("");
        }
        if (mViewBinding.BtmSubSetSign.getText().toString().equals("WR")){
            mViewBinding.MACDSign.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            mViewBinding.MACDSign.setText(String.format("WR14:%s", decimalFormat.format(wr14)));
            mViewBinding.DIFFSign.setTextColor(ContextCompat.getColor(getContext(), R.color.orange_FF8C00));
            mViewBinding.DIFFSign.setText(String.format("WR28:%s", decimalFormat.format(wr28)));
            mViewBinding.DEASign.setText("");
        }
    }

    @Override
    public void onBIASLongPress(double bias6, double bias12, double bias24) {
        if (mViewBinding.TopSubSetSign.getText().toString().equals("BIAS")) {
            mViewBinding.KSign.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            mViewBinding.KSign.setText(String.format("BIAS6:%s", decimalFormat.format(bias6)));
            mViewBinding.DSign.setTextColor(ContextCompat.getColor(getContext(), R.color.orange_FF8C00));
            mViewBinding.DSign.setText(String.format("BIAS12:%s", decimalFormat.format(bias12)));
            mViewBinding.JSign.setTextColor(ContextCompat.getColor(getContext(), R.color.pink_FF69B4));
            mViewBinding.JSign.setText(String.format("BIAS24:%s", decimalFormat.format(bias24)));
        }
        if (mViewBinding.BtmSubSetSign.getText().toString().equals("BIAS")){
            mViewBinding.MACDSign.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            mViewBinding.MACDSign.setText(String.format("BIAS6:%s", decimalFormat.format(bias6)));
            mViewBinding.DIFFSign.setTextColor(ContextCompat.getColor(getContext(), R.color.orange_FF8C00));
            mViewBinding.DIFFSign.setText(String.format("BIAS12:%s", decimalFormat.format(bias12)));
            mViewBinding.DEASign.setTextColor(ContextCompat.getColor(getContext(), R.color.pink_FF69B4));
            mViewBinding.DEASign.setText(String.format("BIAS24:%s", decimalFormat.format(bias24)));
        }
    }

    @Override
    public void onRSILongPress(double rsi6, double rsi12, double rsi24) {
        if (mViewBinding.TopSubSetSign.getText().toString().equals("RSI")) {
            mViewBinding.KSign.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            mViewBinding.KSign.setText(String.format("RSI6:%s", decimalFormat.format(rsi6)));
            mViewBinding.DSign.setTextColor(ContextCompat.getColor(getContext(), R.color.orange_FF8C00));
            mViewBinding.DSign.setText(String.format("RSI12:%s", decimalFormat.format(rsi12)));
            mViewBinding.JSign.setTextColor(ContextCompat.getColor(getContext(), R.color.pink_FF69B4));
            mViewBinding.JSign.setText(String.format("RSI24:%s", decimalFormat.format(rsi24)));
        }
        if (mViewBinding.BtmSubSetSign.getText().toString().equals("RSI")){
            mViewBinding.MACDSign.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            mViewBinding.MACDSign.setText(String.format("RSI6:%s", decimalFormat.format(rsi6)));
            mViewBinding.DIFFSign.setTextColor(ContextCompat.getColor(getContext(), R.color.orange_FF8C00));
            mViewBinding.DIFFSign.setText(String.format("RSI12:%s", decimalFormat.format(rsi12)));
            mViewBinding.DEASign.setTextColor(ContextCompat.getColor(getContext(), R.color.pink_FF69B4));
            mViewBinding.DEASign.setText(String.format("RSI24:%s", decimalFormat.format(rsi24)));
        }
    }

    @Override
    public void onVRLongPress(double vr) {
        if (mViewBinding.TopSubSetSign.getText().toString().equals("VR")) {
            mViewBinding.KSign.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            mViewBinding.KSign.setText(String.format("VR:%s", decimalFormat.format(vr)));
            mViewBinding.DSign.setText("");
            mViewBinding.JSign.setText("");
        }
        if (mViewBinding.BtmSubSetSign.getText().toString().equals("VR")){
            mViewBinding.MACDSign.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            mViewBinding.MACDSign.setText(String.format("VR:%s", decimalFormat.format(vr)));
            mViewBinding.DIFFSign.setText("");
            mViewBinding.DEASign.setText("");
        }
    }

    private String CountUnit(long value){
        if (value>=10000){
            double W = value/10000d;
            if (W>=10000){
                double Y = W/10000;
                return String.format("%s亿", decimalFormat.format(Y));
            }
            return String.format("%s万", decimalFormat.format(W));
        }else {
            return String.valueOf(value);
        }
    }

    @Override
    public void onLoadMoreDailyStock() {
        if (mViewBinding.dailyStockLoadMore.getVisibility() == View.GONE) {
            Log.d(getClass().getSimpleName(), "日K加载更多");
            mViewBinding.dailyStockLoadMore.setVisibility(View.VISIBLE);
            EventBus.getDefault().post(new NoticeDailyKLineLoadMoreEvent(OFFSET + COUNT, PERIOD, REMIT));
        }
    }

    /*
     * 点击事件
     * */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.TopSubSetBg:
            case R.id.TopSubSetSign:
            case R.id.TopSubSetImg:
                SelectSubSign=1;
                ShowSubTypePop();
                if (subTypeListAdapter != null) {
                    subTypeListAdapter.setSelect(subTypeListAdapter.getItemPosition(mViewBinding.TopSubSetSign.getText().toString()));
                    subTypeListAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.BtmSubSetBg:
            case R.id.BtmSubSetSign:
            case R.id.BtmSubSetImg:
                SelectSubSign=2;
                ShowSubTypePop();
                if (subTypeListAdapter != null) {
                    subTypeListAdapter.setSelect(subTypeListAdapter.getItemPosition(mViewBinding.BtmSubSetSign.getText().toString()));
                    subTypeListAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    /*
     * 附图类型弹窗
     * */
    private void ShowSubTypePop() {
        if (subTypePop==null){
            subTypePop = new PopupWindow();
            subTypePop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            subTypePop.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            subTypePop.setOutsideTouchable(false);
        }
        if (subTypeListBinding==null) {
            subTypeListBinding = StockSubTypeListBinding.bind(LayoutInflater.from(getContext()).inflate(R.layout.stock_sub_type_list, null));
            subTypeListBinding.SubTypeList.setLayoutManager(new LinearLayoutManager(getContext()));
            subTypeListBinding.SubTypeBg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    subTypePop.dismiss();
                }
            });
            subTypePop.setContentView(subTypeListBinding.getRoot());
        }
        if (subTypeListAdapter==null) {
            subTypeListAdapter = new StockSubTypeListAdapter(R.layout.stock_sub_type_list_item);
            subTypeListAdapter.setOnItemClickListener(this::onItemClick);
            subTypeListBinding.SubTypeList.setAdapter(subTypeListAdapter);
        }
        Log.d(TAG, "显示附图类型表弹窗");
        subTypePop.showAsDropDown(mViewBinding.getRoot());
    }

    /*
    * 选择副图类型
    * */
    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        if (SelectSubSign==1){
            mViewBinding.TopSubSetSign.setText(subTypeListAdapter.getData().get(position));
            mViewBinding.TopSubChartView.setType(subTypeListAdapter.getType(position));
        }else if (SelectSubSign==2){
            mViewBinding.BtmSubSetSign.setText(subTypeListAdapter.getData().get(position));
            mViewBinding.BtmSubChartView.setType(subTypeListAdapter.getType(position));
        }
        subTypePop.dismiss();
    }
}
