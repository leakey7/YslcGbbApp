package com.gzyslczx.yslc;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.gzyslczx.yslc.adapters.stockmarket.StockMarketValueGridAdapter;
import com.gzyslczx.yslc.databinding.ActivityStockMarketBinding;
import com.gzyslczx.yslc.events.yourui.FiveRangeEvent;
import com.gzyslczx.yslc.events.yourui.NoticeDailyKLineLoadMoreEvent;
import com.gzyslczx.yslc.events.yourui.RealTimeEvent;
import com.gzyslczx.yslc.fragments.yourui.DailyStockFragment;
import com.gzyslczx.yslc.fragments.yourui.MinuteStockFragment;
import com.gzyslczx.yslc.fragments.yourui.MonthStockFragment;
import com.gzyslczx.yslc.fragments.yourui.WeekStockFragment;
import com.gzyslczx.yslc.presenter.StockMarketPresenter;
import com.gzyslczx.yslc.tools.TransStatusTool;
import com.gzyslczx.yslc.tools.yourui.CodeTypeTool;
import com.gzyslczx.yslc.tools.yourui.RequestApi;
import com.yourui.sdk.message.use.Realtime;
import com.yourui.sdk.message.use.Stock;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class StockMarketActivity extends BaseActivity<ActivityStockMarketBinding> implements View.OnClickListener {

    private StockMarketValueGridAdapter mValueGridAdapter;
    private StockMarketPresenter mPresenter;
    private DecimalFormat decimalFormat;
    private String StockCode = "688339";
    private MinuteStockFragment minuteStockFragment;
    private DailyStockFragment dailyStockFragment;
    private WeekStockFragment weekStockFragment;
    private MonthStockFragment monthStockFragment;
    private Stock stock;

    @Override
    void InitParentLayout() {
        EventBus.getDefault().register(this);
        mViewBinding = ActivityStockMarketBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        TransStatusTool.setStatusBarLightMode(this);
        getWindow().setStatusBarColor(ActivityCompat.getColor(this, R.color.white));
    }

    @Override
    void InitView() {
        mValueGridAdapter = new StockMarketValueGridAdapter(this);
        mViewBinding.ValueGrid.setAdapter(mValueGridAdapter);
        mViewBinding.Back.setOnClickListener(this::onClick);
        decimalFormat = new DecimalFormat("#0.00");
        mViewBinding.StyleTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ChangeFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                HiddenFragment(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        ChangeFragment(mViewBinding.StyleTab.getSelectedTabPosition());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter==null){
            mPresenter = new StockMarketPresenter();
            int codeType = CodeTypeTool.MatchingCodeType(StockCode);
            stock = new Stock(StockCode, codeType);
            mPresenter.RequestMinuteChart(this, stock);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /*
    * Fragment更新
    * */
    private void ChangeFragment(int index){
        Log.d(getClass().getSimpleName(), String.format("切换Fragment%d", index));
        if (index==0){
            //分时Fragment
            FragmentTransaction transaction =  getSupportFragmentManager().beginTransaction();
            if (minuteStockFragment==null){
                minuteStockFragment = new MinuteStockFragment();
                transaction.add(mViewBinding.StockMarketFrame.getId(), minuteStockFragment);
                transaction.commit();
                return;
            }
            if (minuteStockFragment.isHidden()){
                transaction.show(minuteStockFragment);
                transaction.commit();
                return;
            }
        }

        if (index==2){
            //日KFragment
            FragmentTransaction transaction =  getSupportFragmentManager().beginTransaction();
            if (dailyStockFragment==null){
                dailyStockFragment = new DailyStockFragment();
                transaction.add(mViewBinding.StockMarketFrame.getId(), dailyStockFragment);
                transaction.commit();
                return;
            }
            if (dailyStockFragment.isHidden()){
                transaction.show(dailyStockFragment);
                transaction.commit();
                return;
            }
        }
        if (index==3){
            //周KFragment
            FragmentTransaction transaction =  getSupportFragmentManager().beginTransaction();
            if (weekStockFragment==null){
                weekStockFragment = new WeekStockFragment();
                transaction.add(mViewBinding.StockMarketFrame.getId(), weekStockFragment);
                transaction.commit();
                return;
            }
            if (weekStockFragment.isHidden()){
                transaction.show(weekStockFragment);
                transaction.commit();
                return;
            }
        }
        if (index==4){
            //周KFragment
            FragmentTransaction transaction =  getSupportFragmentManager().beginTransaction();
            if (monthStockFragment==null){
                monthStockFragment = new MonthStockFragment();
                transaction.add(mViewBinding.StockMarketFrame.getId(), monthStockFragment);
                transaction.commit();
                return;
            }
            if (monthStockFragment.isHidden()){
                transaction.show(monthStockFragment);
                transaction.commit();
                return;
            }
        }
    }
    /*
    * 隐藏Fragment
    * */
    private void HiddenFragment(int index){
        Log.d(getClass().getSimpleName(), String.format("隐藏Fragment%d", index));
        switch (index){
            case 0:
                FragmentTransaction minuteTransaction =  getSupportFragmentManager().beginTransaction();
                if (minuteStockFragment!=null) {
                    minuteTransaction.hide(minuteStockFragment);
                    minuteTransaction.commit();
                }
                break;
            case 2:
                FragmentTransaction dailyTransaction =  getSupportFragmentManager().beginTransaction();
                if (dailyStockFragment!=null) {
                    dailyTransaction.hide(dailyStockFragment);
                    dailyTransaction.commit();
                }
            case 3:
                FragmentTransaction weekTransaction =  getSupportFragmentManager().beginTransaction();
                if (weekStockFragment!=null) {
                    weekTransaction.hide(weekStockFragment);
                    weekTransaction.commit();
                }
                break;
            case 4:
                FragmentTransaction monthTransaction =  getSupportFragmentManager().beginTransaction();
                if (monthStockFragment!=null) {
                    monthTransaction.hide(monthStockFragment);
                    monthTransaction.commit();
                }
                break;
        }
    }


    /*
    * 更新基础报价
    * */
    private void UpdateRealPriceView(Realtime realtime){
        mViewBinding.StockName.setText(realtime.getStock().getStockName()); //股票名称
        mViewBinding.StockCode.setText(realtime.getStock().getStockcode()); //股票代码
        if (realtime.getNewPrice() >=realtime.getPreClosePrice()){
            mViewBinding.NowPrice.setTextColor(ActivityCompat.getColor(this, R.color.red_up));
            mViewBinding.DiffGain.setTextColor(ActivityCompat.getColor(this, R.color.red_up));
            mViewBinding.DiffPrice.setTextColor(ActivityCompat.getColor(this, R.color.red_up));
        }else {
            mViewBinding.NowPrice.setTextColor(ActivityCompat.getColor(this, R.color.green_down));
            mViewBinding.DiffGain.setTextColor(ActivityCompat.getColor(this, R.color.green_down));
            mViewBinding.DiffPrice.setTextColor(ActivityCompat.getColor(this, R.color.green_down));
        }
        double difGain = (realtime.getNewPrice()-realtime.getPreClosePrice())/realtime.getPreClosePrice()*100;
        mViewBinding.NowPrice.setText(String.valueOf(realtime.getNewPrice())); //最新价
        mViewBinding.DiffPrice.setText(realtime.getPriceChange()); //差价
        mViewBinding.DiffGain.setText(decimalFormat.format(difGain)+"%"); //幅度
        List<String> list = new ArrayList<String>();
        list.add(String.valueOf(realtime.getHighPrice())); //高价
        list.add(String.valueOf(realtime.getVolumeRatio())); //量比
        list.add(ShiftUnit(realtime.getTotalMoney())); //金额
        list.add(String.valueOf(realtime.getLowPrice())); //低价
        list.add(realtime.getTurnoverRatio()+"%"); //换手
        list.add("--"); //流通
        list.add(String.valueOf(realtime.getOpenPrice())); //开盘
        list.add(decimalFormat.format(realtime.getAmplitude())); //振幅
        if (realtime.getFinancialZEntity()==null){
            list.add("亏损");
        }else {
            list.add(decimalFormat.format(realtime.getFinancialZEntity().getPERate())); //市盈
        }
        mValueGridAdapter.setValues(list);
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
    * 行情报价数据
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnRealTimeEvent(RealTimeEvent event){
        Log.d(getClass().getSimpleName(), "接收到行情报价Event");
        if (event.getRealtimeList()!=null){
            if (event.getRealtimeList().size()>0){
                Realtime realTime = event.getRealtimeList().get(0);
                UpdateRealPriceView(realTime); //更新基础报价
                if (realTime.getFiveRangeData()!=null){
                    EventBus.getDefault().post(new FiveRangeEvent(realTime.getFiveRangeData(),
                            realTime.getPreClosePrice()));
                }else {
                    Log.d(getClass().getSimpleName(), "行情五档数据为空");
                }
            }else {
                Log.d(getClass().getSimpleName(), "行情基础数据为空");
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnNoticeDailyLoadMoreEvent(NoticeDailyKLineLoadMoreEvent event){
        mPresenter.RequestDailyChart(stock, event.getPeriod(), event.getRemitMode(), event.getOffset());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Back:
                finish();
                break;
        }
    }


}