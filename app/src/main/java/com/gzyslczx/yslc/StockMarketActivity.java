package com.gzyslczx.yslc;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.gzyslczx.yslc.adapters.stockmarket.MoreRealTimeGridAdapter;
import com.gzyslczx.yslc.adapters.stockmarket.StockMarketValueGridAdapter;
import com.gzyslczx.yslc.databinding.ActivityStockMarketBinding;
import com.gzyslczx.yslc.databinding.RealPriceExPopBinding;
import com.gzyslczx.yslc.events.yourui.FiveRangeEvent;
import com.gzyslczx.yslc.events.yourui.NoticeDailyKLineLoadMoreEvent;
import com.gzyslczx.yslc.events.yourui.NoticeDealEvent;
import com.gzyslczx.yslc.events.yourui.NoticeFiveDayMinuteEvent;
import com.gzyslczx.yslc.events.yourui.RealTimeEvent;
import com.gzyslczx.yslc.fragments.yourui.DailyStockFragment;
import com.gzyslczx.yslc.fragments.yourui.FiveDayMinuteStockFragment;
import com.gzyslczx.yslc.fragments.yourui.MinuteStockFragment;
import com.gzyslczx.yslc.fragments.yourui.MonthStockFragment;
import com.gzyslczx.yslc.fragments.yourui.WeekStockFragment;
import com.gzyslczx.yslc.presenter.StockMarketPresenter;
import com.gzyslczx.yslc.tools.DateTool;
import com.gzyslczx.yslc.tools.DisplayTool;
import com.gzyslczx.yslc.tools.TransStatusTool;
import com.gzyslczx.yslc.tools.yourui.CodeTypeTool;
import com.yourui.sdk.message.use.Realtime;
import com.yourui.sdk.message.use.Stock;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StockMarketActivity extends BaseActivity<ActivityStockMarketBinding> implements View.OnClickListener {

    private StockMarketValueGridAdapter mValueGridAdapter;
    private MoreRealTimeGridAdapter moreRealTimeAdapter;
    private StockMarketPresenter mPresenter;
    private DecimalFormat decimalFormat;
    private String StockCode = "688339";
    private MinuteStockFragment minuteStockFragment;
    private FiveDayMinuteStockFragment fiveDayMinuteStockFragment;
    private DailyStockFragment dailyStockFragment;
    private WeekStockFragment weekStockFragment;
    private MonthStockFragment monthStockFragment;
    private Stock stock;
    public static double PrePrice;
    private RealPriceExPopBinding realPriceExPopBinding;
    private PopupWindow realPriceExPop;

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
        moreRealTimeAdapter = new MoreRealTimeGridAdapter(MoreRealTimeGridAdapter.NormalStyle, this);
        mViewBinding.Back.setOnClickListener(this::onClick);
        mViewBinding.Search.setOnClickListener(this::onClick);
        mViewBinding.MoreValue.setOnClickListener(this::onClick);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mViewBinding.StockMarketAppBar.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
            @Override
            public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                if (realPriceExPop!=null && realPriceExPop.isShowing()){
                    return false;
                }
                return true;
            }
        });
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
        if (realPriceExPop!=null && realPriceExPop.isShowing()) {
            realPriceExPop.dismiss();
        }
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
        if (index==1){
            //五日分时Fragment
            FragmentTransaction transaction =  getSupportFragmentManager().beginTransaction();
            if (fiveDayMinuteStockFragment==null){
                fiveDayMinuteStockFragment = new FiveDayMinuteStockFragment();
                transaction.add(mViewBinding.StockMarketFrame.getId(), fiveDayMinuteStockFragment);
                transaction.commit();
                return;
            }
            if (fiveDayMinuteStockFragment.isHidden()){
                transaction.show(fiveDayMinuteStockFragment);
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
            case 1:
                FragmentTransaction fiveDayMinuteTransaction =  getSupportFragmentManager().beginTransaction();
                if (fiveDayMinuteStockFragment!=null) {
                    fiveDayMinuteTransaction.hide(fiveDayMinuteStockFragment);
                    fiveDayMinuteTransaction.commit();
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

    private void UpdateRealPriceEX(Realtime realtime){
        List<String> list = new ArrayList<String>();
        list.add(String.valueOf(realtime.getHighLimitPrice()));//涨停
        list.add(String.valueOf(realtime.getPreClosePrice()));//昨收
        list.add(realtime.getPriceChange());//涨跌

        list.add(String.valueOf(realtime.getLowLimitPrice()));//跌停
        list.add(String.valueOf(realtime.getHand()));//每手股
        list.add(String.valueOf(realtime.getFiveSpeedUp()));//五分涨速

        list.add(String.valueOf(realtime.getInside()));//内盘
        list.add(String.valueOf(realtime.getBuyCount()));//委买
        list.add(decimalFormat.format(realtime.getFinancialZEntity().getPERate()));//市盈

        list.add(String.valueOf(realtime.getOutside()));//外盘
        list.add(String.valueOf(realtime.getSellCount()));//委卖
        list.add(decimalFormat.format(realtime.getFinancialZEntity().getPBRate()));//市净
        moreRealTimeAdapter.setValueList(list);
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
        list.add(String.valueOf(realtime.getHighPrice())); //最高
        list.add(String.valueOf(realtime.getTotalVolume()));//总量
        list.add(ShiftUnit(realtime.getTotalMoney())); //总额

        list.add(String.valueOf(realtime.getLowPrice())); //最低
        list.add(String.valueOf(realtime.getCurrent()));//现手
        list.add(String.format("%s%%", decimalFormat.format(realtime.getTurnoverRatio()))); //换手

        list.add(String.valueOf(realtime.getOpenPrice())); //今开
        list.add(String.format("%s%%", decimalFormat.format(realtime.getAmplitude()))); //振幅
        list.add(String.valueOf(realtime.getVolumeRatio())); //量比
        mValueGridAdapter.setValues(list);
        UpdateRealPriceEX(realtime);
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
    * 行情拓展弹窗
    * */
    private void ShowRealEx(){
        if (realPriceExPopBinding==null){
            realPriceExPopBinding = RealPriceExPopBinding.bind(LayoutInflater.from(this).inflate(R.layout.real_price_ex_pop, null));
            realPriceExPopBinding.MoreValueGrid.setAdapter(moreRealTimeAdapter);
            ViewGroup.LayoutParams layoutParams = realPriceExPopBinding.MoreValueGrid.getLayoutParams();
            layoutParams.height = DisplayTool.dp2px(this,28)*4;
            realPriceExPopBinding.MoreValueGrid.setLayoutParams(layoutParams);
            realPriceExPopBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    realPriceExPop.dismiss();
                    Glide.with(StockMarketActivity.this).load(ActivityCompat.getDrawable(StockMarketActivity.this,R.drawable.rightdown)).into(mViewBinding.MoreValue);
                }
            });
            realPriceExPop = new PopupWindow(realPriceExPopBinding.getRoot(),
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT-mViewBinding.MoreValue.getTop());
            realPriceExPop.setOutsideTouchable(false);
        }
        realPriceExPop.showAsDropDown(mViewBinding.ValueSegLine);
        Glide.with(this).load(ActivityCompat.getDrawable(this,R.drawable.leftup)).into(mViewBinding.MoreValue);
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
                PrePrice = event.getRealtimeList().get(0).getPreClosePrice();
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

    /*
    * 接受到通知K线加载更多
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnNoticeDailyLoadMoreEvent(NoticeDailyKLineLoadMoreEvent event){
        Log.d(getClass().getSimpleName(), "请求更多K线");
        mPresenter.RequestDailyChart(stock, event.getPeriod(), event.getRemitMode(), event.getOffset());
    }

    /*
    * 接受到通知五日分时数据
    * */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void OnNoticeFiveDayMinuteEvent(NoticeFiveDayMinuteEvent event){
        Log.d(getClass().getSimpleName(), "请求五日分时数据");
        if (event.isLoop()){
            mPresenter.RequestHistoryTrendOnLoop(StockMarketActivity.this, stock, event.getDate());
        }else {
            mPresenter.RequestHistoryTrend(stock, event.getDate());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Back:
                finish();
                break;
            case R.id.MoreValue:
                if (realPriceExPop==null || !realPriceExPop.isShowing()){
                    ShowRealEx();
                }else {
                    realPriceExPop.dismiss();
                    Glide.with(this).load(ActivityCompat.getDrawable(this,R.drawable.rightdown)).into(mViewBinding.MoreValue);
                }
                break;
            case R.id.Search:
                //查询股票
                break;
        }
    }

    /*
     * 接受到通知请求交易明细
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
     public void OnNoticeMinuteDeal(NoticeDealEvent event){
        if (stock!=null) {
            Log.d(event.getTAG(), "请求交易明细数:"+event.getCount());
            mPresenter.RequestMinuteDeal(event.getTAG(), event.getBaseActivity(), event.getBaseFragment(),
                    String.format("%s-%s", stock.getStockName(), stock.getStockcode()),
                    event.getCount(), event.isLoop(), event.getSecond());
        }
    }

}