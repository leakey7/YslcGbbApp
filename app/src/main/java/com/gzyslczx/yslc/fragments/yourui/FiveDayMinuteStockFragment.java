package com.gzyslczx.yslc.fragments.yourui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.gzyslczx.yslc.databinding.FivedayMinuteStockFragmentBinding;
import com.gzyslczx.yslc.events.yourui.FiveDayMinuteEvent;
import com.gzyslczx.yslc.events.yourui.NoticeFiveDayMinuteEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.tools.yourui.HisTrendExtEntity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FiveDayMinuteStockFragment extends BaseFragment<FivedayMinuteStockFragmentBinding> {

    private int date;

    private final String TAG = "FiveMinuteStock";

    private SimpleDateFormat ymdFormat;
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
        mViewBinding.chart.setFiveDay(true);
        ymdFormat = new SimpleDateFormat("yyyyMMdd");
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
                mViewBinding.chart.invalidate();
                return;
            }
            cal.add(Calendar.DATE, -1);
            date = Integer.valueOf(ymdFormat.format(cal.getTime()));
            EventBus.getDefault().post(new NoticeFiveDayMinuteEvent(date));
        }else {
            //获取历史分时数据有效
            mViewBinding.chart.AddFiveDayMinuteData(event.getHisTrendExtEntity());
            if (count<4){
                //不够五日，继续请求
                num=0;
                ++count;
                cal.add(Calendar.DATE, -1);
                date = Integer.valueOf(ymdFormat.format(cal.getTime()));
                EventBus.getDefault().post(new NoticeFiveDayMinuteEvent(date));
            }else {
                //足够五日
                Log.d(TAG, "已满五日");
                mViewBinding.chart.invalidate();
            }
        }
    }

}
