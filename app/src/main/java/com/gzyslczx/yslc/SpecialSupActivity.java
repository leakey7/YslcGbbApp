package com.gzyslczx.yslc;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.gson.Gson;
import com.gzyslczx.yslc.adapters.ViewPagerAdapter;
import com.gzyslczx.yslc.databinding.ActivitySpecialSupBinding;
import com.gzyslczx.yslc.databinding.MyCalenderViewBinding;
import com.gzyslczx.yslc.events.GuBbChangeFocusEvent;
import com.gzyslczx.yslc.events.GuBbChangeLoginEvent;
import com.gzyslczx.yslc.events.NoticeChangeSpecialSupCardEvent;
import com.gzyslczx.yslc.events.SpecialSupByDateEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.fragments.specialsup.SpecialSupCardFragment;
import com.gzyslczx.yslc.modes.response.ResSpecialSupportObj;
import com.gzyslczx.yslc.presenter.SpecialSupPresenter;
import com.gzyslczx.yslc.tools.ConnPath;
import com.gzyslczx.yslc.tools.DateTool;
import com.gzyslczx.yslc.tools.DisplayTool;
import com.gzyslczx.yslc.tools.NormalActionTool;
import com.gzyslczx.yslc.tools.SpTool;
import com.gzyslczx.yslc.tools.TransStatusTool;
import com.gzyslczx.yslc.tools.customviews.SharePopup;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.youth.banner.transformer.ScaleInTransformer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class SpecialSupActivity extends BaseActivity<ActivitySpecialSupBinding> implements View.OnClickListener, MenuItem.OnMenuItemClickListener {

    private final String TAG = "SpecialSupAct";
    public final static String SupportDate = "SupDate";
    private SpecialSupPresenter mPresenter;
    private ResSpecialSupportObj specialSupportObj;
    private StringBuilder date = new StringBuilder();
    private String today;
    private SharePopup sharePopup;
    private int MaxYear = 0, MaxMonth = 0, MaxDay = 0;
    private StringBuilder MinHisDate = new StringBuilder();
    private MyCalenderViewBinding calendarView;
    private PopupWindow popupWindow;
    private String[] HistoryDate;

    @Override
    void InitParentLayout() {
        mViewBinding = ActivitySpecialSupBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        TransStatusTool.translucent(this);
        TransStatusTool.setStatusBarDarkMode(this);
    }

    @Override
    void InitView() {
        EventBus.getDefault().register(this);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mViewBinding.SSupToolBar.getLayoutParams();
        layoutParams.setMargins(0, TransStatusTool.getStatusbarHeight(this), 0, 0);
        mViewBinding.SSupToolBar.setLayoutParams(layoutParams);
        mViewBinding.SSupPager.setOffscreenPageLimit(2);
        date.replace(0, date.length(), getIntent().getStringExtra(SupportDate));
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) mViewBinding.SSupPager.getLayoutParams();
        marginLayoutParams.leftMargin = DisplayTool.dp2px(this, 44);
        marginLayoutParams.rightMargin = marginLayoutParams.leftMargin;
        mViewBinding.SSupPager.setLayoutParams(marginLayoutParams);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(DisplayTool.dp2px(this, 10)));
        compositePageTransformer.addTransformer(new ScaleInTransformer());
        InitFragments();
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPagerAdapter.setmFragments(InitFragments());
        mViewBinding.SSupPager.setAdapter(viewPagerAdapter);
        mViewBinding.SSupPager.setPageTransformer(compositePageTransformer);
        mViewBinding.SSupPager.setCurrentItem(1, false);
        mViewBinding.SSupDate.setText(date);
        mPresenter = new SpecialSupPresenter();
        today = DateTool.GetTodayDate();
        if (today.equals(date.toString())) {
            mViewBinding.SSupDateTip.setVisibility(View.VISIBLE);
        } else {
            mViewBinding.SSupDateTip.setVisibility(View.INVISIBLE);
        }
        //点击回退
        SetupBackClicked();
        //点击选择时间
        mViewBinding.SSupDate.setOnClickListener(this::onClick);
        //点击关注
        mViewBinding.SSupFocus.setOnClickListener(this::onClick);
        //设置Pager监听
        mViewBinding.SSupPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0) {
                    mViewBinding.SSupLeftTag.setImageDrawable(ActivityCompat.getDrawable(SpecialSupActivity.this, R.drawable.special_sup_selected));
                    mViewBinding.SSupCenterTag.setImageDrawable(ActivityCompat.getDrawable(SpecialSupActivity.this, R.drawable.special_sup_unselect));
                    mViewBinding.SSupRightTag.setImageDrawable(ActivityCompat.getDrawable(SpecialSupActivity.this, R.drawable.special_sup_unselect));
                } else if (position == 1) {
                    mViewBinding.SSupCenterTag.setImageDrawable(ActivityCompat.getDrawable(SpecialSupActivity.this, R.drawable.special_sup_selected));
                    mViewBinding.SSupLeftTag.setImageDrawable(ActivityCompat.getDrawable(SpecialSupActivity.this, R.drawable.special_sup_unselect));
                    mViewBinding.SSupRightTag.setImageDrawable(ActivityCompat.getDrawable(SpecialSupActivity.this, R.drawable.special_sup_unselect));
                } else if (position == 2) {
                    mViewBinding.SSupRightTag.setImageDrawable(ActivityCompat.getDrawable(SpecialSupActivity.this, R.drawable.special_sup_selected));
                    mViewBinding.SSupLeftTag.setImageDrawable(ActivityCompat.getDrawable(SpecialSupActivity.this, R.drawable.special_sup_unselect));
                    mViewBinding.SSupCenterTag.setImageDrawable(ActivityCompat.getDrawable(SpecialSupActivity.this, R.drawable.special_sup_unselect));
                }
            }
        });
        mViewBinding.SSupLoading.setVisibility(View.VISIBLE);
        mPresenter.RequestSpecialSupByDate(this, this, date.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sharePopup!=null){
            sharePopup.Clear();
        }
        EventBus.getDefault().unregister(this);
    }

    /*
     * 初始化Fragments
     * */
    private List<BaseFragment> InitFragments() {
        List<BaseFragment> fragmentList = new ArrayList<BaseFragment>();
        //左卡片
        SpecialSupCardFragment frag1 = new SpecialSupCardFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putInt(SpecialSupCardFragment.SpecialSupKey, 1);
        frag1.setArguments(bundle1);
        fragmentList.add(frag1);
        //中卡片
        SpecialSupCardFragment frag2 = new SpecialSupCardFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putInt(SpecialSupCardFragment.SpecialSupKey, 2);
        frag2.setArguments(bundle2);
        fragmentList.add(frag2);
        //右卡片
        SpecialSupCardFragment frag3 = new SpecialSupCardFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putInt(SpecialSupCardFragment.SpecialSupKey, 3);
        frag3.setArguments(bundle3);
        fragmentList.add(frag3);
        return fragmentList;
    }

    /*
     * 回退点击事件
     * */
    private void SetupBackClicked() {
        mViewBinding.SSupToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /*
     * 点击事件
     * */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.SSupDate:
                //点击选择日期
                if (mViewBinding.SSupLoading.getVisibility() == View.GONE) {
                    AddSchemeDate(HistoryDate, DateTool.GetYear(mViewBinding.SSupDate.getText().toString().trim()),
                            DateTool.GetMonth(mViewBinding.SSupDate.getText().toString().trim()), DateTool.GetDay(mViewBinding.SSupDate.getText().toString().trim()));
                }
                break;
            case R.id.SSupFocus:
                if (mViewBinding.SSupLoading.getVisibility() == View.GONE) {
                    //点击关注
                    if (SpTool.Instance(this).IsGuBbLogin()) {
                        //已登录
                        if (specialSupportObj != null) {
                            NormalActionTool.FocusAction(this, null, this, specialSupportObj.getTgList().get(0).getColumnId(), false, TAG);
                        }
                    } else {
                        //未登录
                        NormalActionTool.LoginAction(this, null, this, null, TAG);
                    }
                }
                break;
            case R.id.CalendarCommit:
                //提交选择日期
                Calendar calendar = calendarView.CalendarView.getSelectedCalendar();
                if (calendar.getYear() != 0 && calendar.getMonth() != 0 && calendar.getDay() != 0) {
                    popupWindow.dismiss();
                    mViewBinding.SSupLoading.setVisibility(View.VISIBLE);
                    mPresenter.RequestSpecialSupByDate(this, this, String.format("%d-%d-%d", calendar.getYear(), calendar.getMonth(), calendar.getDay()));
                } else {
                    Toast.makeText(this, "请选择查询的日期", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.CalendarClose:
                //关闭日期选择
                popupWindow.dismiss();
                break;
            case R.id.CalendarLeft:
                calendarView.CalendarView.scrollToPre();
                break;
            case R.id.CalendarRight:
                calendarView.CalendarView.scrollToNext();
                break;
        }
    }

    /*
     * 接收登录状态更新
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnChangeLoginEvent(GuBbChangeLoginEvent event) {
        //更新登录显示
        Log.d(TAG, "接收到登录更新");
        mViewBinding.SSupLoading.setVisibility(View.VISIBLE);
        mPresenter.RequestSpecialSupByDate(this, this, date.toString());
    }

    /*
     * 接收更新关注
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnFocusChangeEvent(GuBbChangeFocusEvent event) {
        Log.d(TAG, "接收到关注更新");
        if (event.isFlag() && !event.isTeacher() && !TextUtils.isEmpty(event.getFocusObj().getName()) && event.getFocusObj().getName().equals(getString(R.string.special_sup))) {
            ChangeFocusState(event.isFocus());
        }
    }

    /*
     * 接收盘前特供
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnSpecialSupByDateEvent(SpecialSupByDateEvent event) {
        Log.d(TAG, "接收到盘前特供");
        if (event.isFlag()) {
            specialSupportObj = event.getDataObj();
            mViewBinding.SSupDate.setText(event.getDataObj().getTgList().get(0).getAddDate());
            NoticeChangeSpecialSupCardEvent leftCard = new NoticeChangeSpecialSupCardEvent();
            //左边卡片
            leftCard.setCardCode(1);
            leftCard.setDateCard(event.getDataObj().getTgList().get(0));
            EventBus.getDefault().post(leftCard);
            //中间卡片
            NoticeChangeSpecialSupCardEvent centerCard = new NoticeChangeSpecialSupCardEvent();
            centerCard.setCardCode(2);
            centerCard.setDateCard(event.getDataObj().getTgList().get(1));
            EventBus.getDefault().post(centerCard);
            //右边卡片
            NoticeChangeSpecialSupCardEvent rightCard = new NoticeChangeSpecialSupCardEvent();
            rightCard.setCardCode(3);
            rightCard.setDateCard(event.getDataObj().getTgList().get(2));
            EventBus.getDefault().post(rightCard);
            ChangeFocusState(event.getDataObj().getTgList().get(0).isFocus());
            if (today.equals(event.getDataObj().getTgList().get(0).getAddDate())) {
                mViewBinding.SSupDateTip.setVisibility(View.VISIBLE);
            } else {
                mViewBinding.SSupDateTip.setVisibility(View.INVISIBLE);
            }
            mViewBinding.SSupToolBar.setOnMenuItemClickListener(this::onMenuItemClick);
            if (MaxYear == 0 || MaxMonth == 0 || MaxDay == 0) {
                MaxYear = DateTool.GetYear(event.getDataObj().getTgList().get(0).getAddDate());
                MaxMonth = DateTool.GetMonth(event.getDataObj().getTgList().get(0).getAddDate());
                MaxDay = DateTool.GetDay(event.getDataObj().getTgList().get(0).getAddDate());
            }
            HistoryDate = event.getDataObj().getDateList();
            int last = event.getDataObj().getDateList().length - 1;
            MinHisDate.replace(0, MinHisDate.length(), event.getDataObj().getDateList()[last]);
        }
        mViewBinding.SSupLoading.setVisibility(View.GONE);
    }

    /*
     * 显示关注状态
     * */
    private void ChangeFocusState(boolean isFocus) {
        if (isFocus) {
            mViewBinding.SSupFocus.setBackground(ActivityCompat.getDrawable(this, R.drawable.gray_focus_radius_10_shape));
            mViewBinding.SSupFocus.setTextColor(ActivityCompat.getColor(this, R.color.gray_666));
            mViewBinding.SSupFocus.setText(getString(R.string.IsFocus));
        } else {
            mViewBinding.SSupFocus.setBackground(ActivityCompat.getDrawable(this, R.drawable.white_border_focus_radius_10_shape));
            mViewBinding.SSupFocus.setTextColor(ActivityCompat.getColor(this, R.color.white));
            mViewBinding.SSupFocus.setText(getString(R.string.AddFocus));
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.share) {
            if (sharePopup == null) {
                sharePopup = new SharePopup(this, mViewBinding.getRoot(), true,
                        ConnPath.SpecialSupShare, "盘前特供");
            }
            sharePopup.Show(mViewBinding.getRoot(), Gravity.BOTTOM, 0, 0);
            return true;
        }
        return false;
    }


    /*
     * 添加标记日历
     * */
    private void AddSchemeDate(String[] HistoryDate, int CurrentYear, int CurrentMonth, int CurrentDay) {
        if (calendarView == null) {
            calendarView = MyCalenderViewBinding.bind(getLayoutInflater().inflate(R.layout.my_calender_view, null));
            calendarView.CalendarCommit.setOnClickListener(this::onClick);
            calendarView.CalendarClose.setOnClickListener(this::onClick);
            calendarView.CalendarLeft.setOnClickListener(this::onClick);
            calendarView.CalendarRight.setOnClickListener(this::onClick);
            /*
             * 添加拦截监听
             * */
            calendarView.CalendarView.setOnCalendarInterceptListener(new CalendarView.OnCalendarInterceptListener() {
                @Override
                public boolean onCalendarIntercept(Calendar calendar) {
                    Calendar nowDate = new Calendar();
                    nowDate.setYear(DateTool.GetYear(mViewBinding.SSupDate.getText().toString().trim()));
                    nowDate.setMonth(DateTool.GetMonth(mViewBinding.SSupDate.getText().toString().trim()));
                    nowDate.setDay(DateTool.GetDay(mViewBinding.SSupDate.getText().toString().trim()));
                    if (calendar.differ(nowDate)==0){
                        return false;
                    }else {
                        Calendar current = new Calendar();
                        current.setYear(calendarView.CalendarView.getCurYear());
                        current.setMonth(calendarView.CalendarView.getCurMonth());
                        current.setDay(calendarView.CalendarView.getCurDay());
                        return calendar.isWeekend() || !calendar.hasScheme() || calendar.compareTo(current) > 0;
                    }
                }

                @Override
                public void onCalendarInterceptClick(Calendar calendar, boolean isClick) {

                }
            });
            /*
             * 日历切换监听
             * */
            calendarView.CalendarView.setOnMonthChangeListener(new CalendarView.OnMonthChangeListener() {
                @Override
                public void onMonthChange(int year, int month) {
                    calendarView.CalendarMonth.setText(String.format("%d年%d月", year, month));
                }
            });
            popupWindow = new PopupWindow(calendarView.getRoot(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setBackgroundDrawable(new ColorDrawable(ActivityCompat.getColor(this, R.color.trans_50_black)));
            popupWindow.setFocusable(true);
        }
        calendarView.CalendarMonth.setText(String.format("%d年%d月", CurrentYear, CurrentMonth));
        Log.d(TAG, "历史数据:" + new Gson().toJson(HistoryDate));
        for (String date : HistoryDate) {
            Calendar calendar = new Calendar();
            calendar.setYear(DateTool.GetYear(date));
            calendar.setMonth(DateTool.GetMonth(date));
            calendar.setDay(DateTool.GetDay(date));
            calendarView.CalendarView.addSchemeDate(calendar);
        }
        calendarView.CalendarView.scrollToCalendar(CurrentYear, CurrentMonth, CurrentDay);
        popupWindow.showAtLocation(mViewBinding.getRoot(), Gravity.BOTTOM, 0, 0);
    }

}