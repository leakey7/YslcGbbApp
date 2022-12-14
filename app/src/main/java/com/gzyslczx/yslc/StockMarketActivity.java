package com.gzyslczx.yslc;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import com.gzyslczx.yslc.databinding.StockMoreTypeListBinding;
import com.gzyslczx.yslc.events.yourui.FiveRangeEvent;
import com.gzyslczx.yslc.events.yourui.MoreTypeStockEvent;
import com.gzyslczx.yslc.events.yourui.NoticeDailyKLineLoadMoreEvent;
import com.gzyslczx.yslc.events.yourui.NoticeDealEvent;
import com.gzyslczx.yslc.events.yourui.NoticeFiveDayMinuteEvent;
import com.gzyslczx.yslc.events.yourui.RealTimeEvent;
import com.gzyslczx.yslc.fragments.yourui.DailyStockFragment;
import com.gzyslczx.yslc.fragments.yourui.FiveDayMinuteStockFragment;
import com.gzyslczx.yslc.fragments.yourui.MinuteStockFragment;
import com.gzyslczx.yslc.fragments.yourui.MonthStockFragment;
import com.gzyslczx.yslc.fragments.yourui.MoreFragment;
import com.gzyslczx.yslc.fragments.yourui.WeekStockFragment;
import com.gzyslczx.yslc.presenter.StockMarketPresenter;
import com.gzyslczx.yslc.tools.DisplayTool;
import com.gzyslczx.yslc.tools.TransStatusTool;
import com.gzyslczx.yslc.tools.yourui.CodeTypeTool;
import com.yourui.sdk.message.api.protocol.QuoteConstants;
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
    private MoreRealTimeGridAdapter moreRealTimeAdapter;
    private StockMarketPresenter mPresenter;
    private DecimalFormat decimalFormat;
    private String StockCode = "688339";
    private MinuteStockFragment minuteStockFragment;
    private FiveDayMinuteStockFragment fiveDayMinuteStockFragment;
    private DailyStockFragment dailyStockFragment;
    private WeekStockFragment weekStockFragment;
    private MonthStockFragment monthStockFragment;
    private MoreFragment moreStockFragment;
    private Stock stock;
    public static double PrePrice;
    private RealPriceExPopBinding realPriceExPopBinding;
    private PopupWindow realPriceExPop;
    private StockMoreTypeListBinding moreTypeListBinding;
    private PopupWindow moreTypeWindow;
    private Short SelectMoreType=-2;

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
//        mViewBinding.MoreTab.setOnClickListener(this::onClick);
//        mViewBinding.MoreTabImg.setOnClickListener(this::onClick);
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
        if (moreTypeWindow!=null && moreTypeWindow.isShowing()){
            moreTypeWindow.dismiss();
        }
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /*
    * Fragment??????
    * */
    private void ChangeFragment(int index){
        Log.d(getClass().getSimpleName(), String.format("??????Fragment%d", index));
        if (index==0){
            //??????Fragment
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
            //????????????Fragment
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
            //???KFragment
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
            //???KFragment
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
            //???KFragment
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
        if (index==5){
            //??????Fragment
            if (SelectMoreType==-2) {
                mViewBinding.StyleTab.getTabAt(5).setText("5min");
            }
            FragmentTransaction transaction =  getSupportFragmentManager().beginTransaction();
            if (moreStockFragment==null){
                moreStockFragment = new MoreFragment();
                transaction.add(mViewBinding.StockMarketFrame.getId(), moreStockFragment);
                transaction.commit();
            }
            if (moreStockFragment.isHidden()){
                transaction.show(moreStockFragment);
                transaction.commit();
            }
            //??????Fragment
            //??????????????????
            if (moreTypeListBinding==null){
                moreTypeListBinding = StockMoreTypeListBinding.bind(LayoutInflater.from(this).inflate(R.layout.stock_more_type_list, null));
                moreTypeListBinding.minute5.setOnClickListener(this::onClick);
                moreTypeListBinding.minute15.setOnClickListener(this::onClick);
                moreTypeListBinding.minute30.setOnClickListener(this::onClick);
                moreTypeListBinding.minute60.setOnClickListener(this::onClick);
                moreTypeListBinding.minute120.setOnClickListener(this::onClick);
            }
            if (moreTypeWindow==null) {
                moreTypeWindow = new PopupWindow();
                moreTypeWindow.setWidth(DisplayTool.dp2px(this, 40));
                moreTypeWindow.setHeight(DisplayTool.dp2px(this, 120));
                moreTypeWindow.setOutsideTouchable(true);
                moreTypeWindow.setContentView(moreTypeListBinding.getRoot());
            }
            if (moreTypeWindow.isShowing()){
                moreTypeWindow.dismiss();
            }else {
                moreTypeWindow.showAsDropDown(mViewBinding.StyleTab, Gravity.RIGHT, 0, 0);
            }
        }
    }
    /*
    * ??????Fragment
    * */
    private void HiddenFragment(int index){
        Log.d(getClass().getSimpleName(), String.format("??????Fragment%d", index));
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
                break;
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
            case 5:
                FragmentTransaction moreTransaction =  getSupportFragmentManager().beginTransaction();
                if (moreStockFragment!=null) {
                    moreTransaction.hide(moreStockFragment);
                    moreTransaction.commit();
                }
                break;
        }
    }

    private void UpdateRealPriceEX(Realtime realtime){
        List<String> list = new ArrayList<String>();
        list.add(String.valueOf(realtime.getHighLimitPrice()));//??????
        list.add(String.valueOf(realtime.getPreClosePrice()));//??????
        list.add(realtime.getPriceChange());//??????

        list.add(String.valueOf(realtime.getLowLimitPrice()));//??????
        list.add(String.valueOf(realtime.getHand()));//?????????
        list.add(String.valueOf(realtime.getFiveSpeedUp()));//????????????

        list.add(String.valueOf(realtime.getInside()));//??????
        list.add(String.valueOf(realtime.getBuyCount()));//??????
        list.add(decimalFormat.format(realtime.getFinancialZEntity().getPERate()));//??????

        list.add(String.valueOf(realtime.getOutside()));//??????
        list.add(String.valueOf(realtime.getSellCount()));//??????
        list.add(decimalFormat.format(realtime.getFinancialZEntity().getPBRate()));//??????
        moreRealTimeAdapter.setValueList(list);
    }

    /*
    * ??????????????????
    * */
    private void UpdateRealPriceView(Realtime realtime){
        mViewBinding.StockName.setText(realtime.getStock().getStockName()); //????????????
        mViewBinding.StockCode.setText(realtime.getStock().getStockcode()); //????????????
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
        mViewBinding.NowPrice.setText(String.valueOf(realtime.getNewPrice())); //?????????
        mViewBinding.DiffPrice.setText(realtime.getPriceChange()); //??????
        mViewBinding.DiffGain.setText(decimalFormat.format(difGain)+"%"); //??????
        List<String> list = new ArrayList<String>();
        list.add(String.valueOf(realtime.getHighPrice())); //??????
        list.add(String.valueOf(realtime.getTotalVolume()));//??????
        list.add(ShiftUnit(realtime.getTotalMoney())); //??????

        list.add(String.valueOf(realtime.getLowPrice())); //??????
        list.add(String.valueOf(realtime.getCurrent()));//??????
        list.add(String.format("%s%%", decimalFormat.format(realtime.getTurnoverRatio()))); //??????

        list.add(String.valueOf(realtime.getOpenPrice())); //??????
        list.add(String.format("%s%%", decimalFormat.format(realtime.getAmplitude()))); //??????
        list.add(String.valueOf(realtime.getVolumeRatio())); //??????
        mValueGridAdapter.setValues(list);
        UpdateRealPriceEX(realtime);
    }

    /*
    * ????????????
    * */
    @NonNull
    private String ShiftUnit(double number){
        double millionsUnit = Math.round(number)/10000f; //?????????-????????????
        if (millionsUnit>=1000){
            double hundredMillion = Math.round(number)/100000000f; //?????????
            return decimalFormat.format(hundredMillion)+"???"; //?????? - ?????????
        }else {
            return decimalFormat.format(millionsUnit)+"???"; //?????? - ?????????
        }
    }

    /*
    * ??????????????????
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
    * ??????????????????
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnRealTimeEvent(RealTimeEvent event){
        Log.d(getClass().getSimpleName(), "?????????????????????Event");
        if (event.getRealtimeList()!=null){
            if (event.getRealtimeList().size()>0){
                Realtime realTime = event.getRealtimeList().get(0);
                UpdateRealPriceView(realTime); //??????????????????
                PrePrice = event.getRealtimeList().get(0).getPreClosePrice();
                if (realTime.getFiveRangeData()!=null){
                    EventBus.getDefault().post(new FiveRangeEvent(realTime.getFiveRangeData(),
                            realTime.getPreClosePrice()));
                }else {
                    Log.d(getClass().getSimpleName(), "????????????????????????");
                }
            }else {
                Log.d(getClass().getSimpleName(), "????????????????????????");
            }
        }
    }

    /*
    * ???????????????K???????????????
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnNoticeDailyLoadMoreEvent(NoticeDailyKLineLoadMoreEvent event){
        Log.d(getClass().getSimpleName(), "????????????K???");
        mPresenter.RequestDailyChart(stock, event.getPeriod(), event.getRemitMode(), event.getOffset());
    }

    /*
    * ?????????????????????????????????
    * */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void OnNoticeFiveDayMinuteEvent(NoticeFiveDayMinuteEvent event){
        if (event.isLoop()){
            Log.d(getClass().getSimpleName(), String.format("????????????????????????:%d", event.getDate()));
            mPresenter.RequestHistoryTrendOnLoop(StockMarketActivity.this, stock, event.getDate());
        }else {
            Log.d(getClass().getSimpleName(), String.format("????????????????????????:%d", event.getDate()));
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
                //????????????
                break;
            case R.id.minute5:
                mViewBinding.StyleTab.getTabAt(5).setText("5min");
                SelectMoreType = QuoteConstants.PERIOD_TYPE_MINUTE5;
                EventBus.getDefault().post(new MoreTypeStockEvent(SelectMoreType));
                moreTypeWindow.dismiss();
                break;
            case R.id.minute15:
                mViewBinding.StyleTab.getTabAt(5).setText("15min");
                SelectMoreType = QuoteConstants.PERIOD_TYPE_MINUTE15;
                EventBus.getDefault().post(new MoreTypeStockEvent(SelectMoreType));
                moreTypeWindow.dismiss();
                break;
            case R.id.minute30:
                mViewBinding.StyleTab.getTabAt(5).setText("30min");
                SelectMoreType = QuoteConstants.PERIOD_TYPE_MINUTE30;
                EventBus.getDefault().post(new MoreTypeStockEvent(SelectMoreType));
                moreTypeWindow.dismiss();
                break;
            case R.id.minute60:
                mViewBinding.StyleTab.getTabAt(5).setText("60min");
                SelectMoreType = QuoteConstants.PERIOD_TYPE_MINUTE60;
                EventBus.getDefault().post(new MoreTypeStockEvent(SelectMoreType));
                moreTypeWindow.dismiss();
                break;
            case R.id.minute120:
                mViewBinding.StyleTab.getTabAt(5).setText("120min");
                SelectMoreType = QuoteConstants.PERIOD_TYPE_MINUTE120;
                EventBus.getDefault().post(new MoreTypeStockEvent(SelectMoreType));
                moreTypeWindow.dismiss();
                break;
        }
    }

    /*
    * ????????????Fragment
    * */


    /*
     * ?????????????????????????????????
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
     public void OnNoticeMinuteDeal(NoticeDealEvent event){
        if (stock!=null) {
            Log.d(event.getTAG(), "?????????????????????:"+event.getCount());
            mPresenter.RequestMinuteDeal(event.getTAG(), event.getBaseActivity(), event.getBaseFragment(),
                    String.format("%s-%s", stock.getStockName(), stock.getStockcode()),
                    event.getCount(), event.isLoop(), event.getSecond());
        }
    }

}