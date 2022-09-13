package com.gzyslczx.yslc.fragments.yourui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.adapters.stockmarket.MinuteDetailRecyclerAdapter;
import com.gzyslczx.yslc.databinding.DealDetailFragmentBinding;
import com.gzyslczx.yslc.events.yourui.MinuteDealDetailEvent;
import com.gzyslczx.yslc.events.yourui.NoticeDealEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collections;

public class MinuteDealDetailFragment extends BaseFragment<DealDetailFragmentBinding> {

    private MinuteDetailRecyclerAdapter mAdapter;

    @Override
    protected void InitParentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        mViewBinding = DealDetailFragmentBinding.inflate(inflater, container, false);
        InitView();
    }

    @Override
    protected void InitView() {
        mViewBinding.dealRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new MinuteDetailRecyclerAdapter(R.layout.minute_detail_item);
        mViewBinding.dealRecycler.setAdapter(mAdapter);
        NoticeDealEvent event = new NoticeDealEvent();
        event.setTAG(getClass().getSimpleName());
        event.setBaseFragment(this);
        event.setCount(-1);
        event.setLoop(true);
        event.setSecond(2);
        EventBus.getDefault().post(event);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /*
    * 接受部分明细
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMinuteDealDetailEvent(MinuteDealDetailEvent event){
        if (event.getStockTickDetail()!=null){
            Log.d(getClass().getSimpleName(), "更新部分交易明细");
            Collections.reverse(event.getStockTickDetail().getStockTickInfoList());
            mAdapter.setList(event.getStockTickDetail().getStockTickInfoList());
        }
    }

}
