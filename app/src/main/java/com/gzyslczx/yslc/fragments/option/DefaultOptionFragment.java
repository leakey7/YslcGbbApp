package com.gzyslczx.yslc.fragments.option;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.SearchActivity;
import com.gzyslczx.yslc.adapters.option.DefaultOptionGridAdapter;
import com.gzyslczx.yslc.databinding.DefaultOptionFragmentBinding;
import com.gzyslczx.yslc.events.DefaultOptionEvent;
import com.gzyslczx.yslc.events.GuBbChangeOptionStateEvent;
import com.gzyslczx.yslc.events.NoticeOptionPageChangeEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.modes.response.ResDefaultOptionObj;
import com.gzyslczx.yslc.presenter.OptionPresenter;
import com.gzyslczx.yslc.tools.NormalActionTool;
import com.gzyslczx.yslc.tools.SpTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashSet;

public class DefaultOptionFragment extends BaseFragment<DefaultOptionFragmentBinding> implements View.OnClickListener {

    private final String TAG = "DefOptionFrag";
    private OptionPresenter mPresenter;
    private DefaultOptionGridAdapter mGridAdapter;
    private HashSet<String> isCheckSet = new HashSet<String>();
    private boolean isClickedAllWithoutSelect = false;

    @Override
    protected void InitParentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = DefaultOptionFragmentBinding.inflate(inflater, container, false);
        InitView();
    }

    @Override
    protected void InitView() {
        EventBus.getDefault().register(this);
        mViewBinding.DefOptionGrid.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mGridAdapter = new DefaultOptionGridAdapter(getContext());
        mViewBinding.DefOptionGrid.setAdapter(mGridAdapter);
        SetupGridItemClick();
        mViewBinding.DefOptionAddTag.setOnClickListener(this::onClick);
        mViewBinding.DefOptionAddAll.setOnClickListener(this::onClick);
        mPresenter = new OptionPresenter();
        mViewBinding.DefOptionLoading.setVisibility(View.VISIBLE);
        mPresenter.RequestDefaultOption(getContext(),this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isCheckSet.size()>0 && SpTool.Instance(getContext()).IsGuBbLogin()){
            mViewBinding.DefOptionLoading.setVisibility(View.GONE);
            NormalActionTool.AddOptionAction(getContext(), this, (BaseActivity) getActivity(), isCheckSet.toArray(new String[isCheckSet.size()]), TAG);
        }else if (!SpTool.Instance(getContext()).IsGuBbLogin() && isCheckSet.size()>0 && isClickedAllWithoutSelect){
            isClickedAllWithoutSelect = false;
            isCheckSet.clear();
        }else if (SpTool.Instance(getContext()).IsGuBbLogin()){
            mViewBinding.DefOptionLoading.setVisibility(View.VISIBLE);
            mPresenter.RequestMyOption(getContext(), this, true);
        }
    }

    /*
     * 设置推荐列点击
     * */
    private void SetupGridItemClick(){
        mViewBinding.DefOptionGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mGridAdapter.getData().get(i).changeSelect();
                mGridAdapter.notifyDataSetChanged();
                for (ResDefaultOptionObj obj : mGridAdapter.getData()){
                    if (obj.isSelect()){
                        isCheckSet.add(obj.getStockCode());
                    }else {
                        isCheckSet.remove(obj.getStockCode());
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.DefOptionAddTag:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.DefOptionAddAll:
                if (!SpTool.Instance(getContext()).IsGuBbLogin()){
                    if (isCheckSet.size()<=0) {
                        for (ResDefaultOptionObj obj : mGridAdapter.getData()) {
                            isCheckSet.add(obj.getStockCode());
                        }
                        isClickedAllWithoutSelect = true;
                    }
                    NormalActionTool.LoginAction(getContext(), this, (BaseActivity) getActivity(), null, TAG);
                }else {
                    if (isCheckSet.size()>0){
                        mViewBinding.DefOptionLoading.setVisibility(View.VISIBLE);
                        NormalActionTool.AddOptionAction(getContext(), this, (BaseActivity) getActivity(), isCheckSet.toArray(new String[isCheckSet.size()]), TAG);
                    }else {
                        for (ResDefaultOptionObj obj : mGridAdapter.getData()){
                            isCheckSet.add(obj.getStockCode());
                        }
                        mViewBinding.DefOptionLoading.setVisibility(View.VISIBLE);
                        NormalActionTool.AddOptionAction(getContext(), this, (BaseActivity) getActivity(), isCheckSet.toArray(new String[isCheckSet.size()]), TAG);
                    }
                }
                break;
        }
    }


    /*
     * 接收我的自选股增加
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMyOptionDeleteEvent(GuBbChangeOptionStateEvent event){
        if (event.isFlag() && event.isAddOption()){
            Log.d(TAG, "接收到添删自选");
            mViewBinding.DefOptionLoading.setVisibility(View.GONE);
            NoticeOptionPageChangeEvent changeEvent = new NoticeOptionPageChangeEvent(0);
            EventBus.getDefault().post(changeEvent);
            isCheckSet.clear();
        }else {
            Toast.makeText(getContext(), event.getError(), Toast.LENGTH_SHORT).show();
        }
    }

    /*
     * 接收默认自选
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnDefOptionEvent(DefaultOptionEvent event){
        Log.d(TAG, "接收到默认自选");
        if (event.isFlag()){
            mGridAdapter.setData(event.getDatList());
            mGridAdapter.notifyDataSetChanged();
        }else {
            Toast.makeText(getContext(), event.getError(), Toast.LENGTH_SHORT).show();
        }
        mViewBinding.DefOptionLoading.setVisibility(View.GONE);
    }


    /*
     * 接收通知刷新自选页面
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnNoticeOptionChangePageEvent(NoticeOptionPageChangeEvent event){
        if (event.getCurrentPage()==1) {
            Log.d(TAG, "接收到通知刷新自选页面");
            mViewBinding.DefOptionLoading.setVisibility(View.VISIBLE);
            mPresenter.RequestDefaultOption(getContext(), this);
        }else {
            mViewBinding.DefOptionLoading.setVisibility(View.GONE);
        }
    }

}
