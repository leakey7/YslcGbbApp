package com.gzyslczx.yslc.fragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.LabelSelfActivity;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.TeacherSelfActivity;
import com.gzyslczx.yslc.adapters.main.MainMyFocusAdapter;
import com.gzyslczx.yslc.adapters.main.bean.MainMyFocusData;
import com.gzyslczx.yslc.databinding.MainMoreFocusFragmentBinding;
import com.gzyslczx.yslc.events.GuBbChangeFocusEvent;
import com.gzyslczx.yslc.events.GuBbChangeLoginEvent;
import com.gzyslczx.yslc.events.GuBbMainMoreFocusEvent;
import com.gzyslczx.yslc.events.MainFocusPageChangeEvent;
import com.gzyslczx.yslc.events.NoticeFocusPageRefreshEvent;
import com.gzyslczx.yslc.events.NoticeFocusUpdateEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.presenter.MainFocusPresenter;
import com.gzyslczx.yslc.tools.NormalActionTool;
import com.gzyslczx.yslc.tools.SpTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class MainMoreFocusFragment extends BaseFragment<MainMoreFocusFragmentBinding> implements View.OnClickListener, OnItemChildClickListener {

    private final String TAG = "MoreFocusFrag";
    private MainMyFocusAdapter mAdapter;
    private MainFocusPresenter mPresenter;

    @Override
    protected void InitParentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = MainMoreFocusFragmentBinding.inflate(inflater, container, false);
        InitView();
    }

    @Override
    protected void InitView() {
        EventBus.getDefault().register(this);
        mViewBinding.MoreFocusRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new MainMyFocusAdapter(new ArrayList<MainMyFocusData>());
        mAdapter.setOnItemChildClickListener(this::onItemChildClick);
        mViewBinding.MoreFocusRecycler.setAdapter(mAdapter);
        mViewBinding.MoreFocusLeft.setOnClickListener(this::onClick);
        mPresenter = new MainFocusPresenter();
        mViewBinding.MoreFocusRefresh.setEnabled(false);
        mViewBinding.MoreFocusRefresh.setRefreshing(true);
        mPresenter.RequestMoreFocus(getContext(), this);
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    /*
     * ??????????????????
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMoreFocusEvent(GuBbMainMoreFocusEvent event) {
        Log.d(TAG, "?????????????????????");
        if (event.isFlag()) {
            mAdapter.setList(event.getDataList());
            mViewBinding.MoreFocusRecycler.scrollToPosition(0);
        }
        mViewBinding.MoreFocusRefresh.setRefreshing(false);
    }

    /*
     * ????????????????????????
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnChangeLoginEvent(GuBbChangeLoginEvent event) {
        //??????????????????
        Log.d(TAG, "?????????????????????");
        if (event.isLogin()) {
            mPresenter.RequestMoreFocus(getContext(), this);
        }
    }

    /*
     * ????????????????????????
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void NoticeUpdateFocusEvent(NoticeFocusUpdateEvent event) {
        if (event.isFocus()) {
            Log.d(TAG, "???????????????????????????");
            //????????????????????????????????????????????????????????????
            if (event.isTeacher()) {
                //??????????????????
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    MainMyFocusData data = mAdapter.getData().get(i);
                    if (data.getItemType() == MainMyFocusData.TeacherItem && data.getData().getTid().equals(event.getID())) {
                        GuBbChangeFocusEvent focusEvent = new GuBbChangeFocusEvent(true, data.getData(), event.isFocus(), true);
                        EventBus.getDefault().post(focusEvent);
                        mAdapter.removeAt(i);
                        break;
                    }
                }
            } else {
                //??????????????????
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    MainMyFocusData data = mAdapter.getData().get(i);
                    if (data.getItemType() == MainMyFocusData.LabelItem && data.getData().getTid().equals(event.getID())) {
                        GuBbChangeFocusEvent focusEvent = new GuBbChangeFocusEvent(true, data.getData(), event.isFocus(), false);
                        EventBus.getDefault().post(focusEvent);
                        mAdapter.removeAt(i);
                        break;
                    }
                }
            }
        }
    }

    /*
    * ??????????????????
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnChangeFocusEvent(GuBbChangeFocusEvent event){
        Log.d(TAG, "?????????????????????");
        if (!event.isFocus()){
            mViewBinding.MoreFocusRefresh.setRefreshing(true);
            mPresenter.RequestMoreFocus(getContext(), this);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.MoreFocusLeft){
            MainFocusPageChangeEvent event = new MainFocusPageChangeEvent(0);
            EventBus.getDefault().post(event);
        }
    }

    @Override
    public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        switch (view.getId()){
            case R.id.FocusTHeadImg:
                //?????????????????????????????????????????????
                Intent teacherIntent = new Intent(getContext(), TeacherSelfActivity.class);
                teacherIntent.putExtra(TeacherSelfActivity.TIDKey, mAdapter.getData().get(position).getData().getTid());
                startActivity(teacherIntent);
                break;
            case R.id.FocusLHeadImg:
                //????????????LOGO????????????????????????
                Intent labelIntent = new Intent(getContext(), LabelSelfActivity.class);
                labelIntent.putExtra(LabelSelfActivity.LNameKey, mAdapter.getData().get(position).getData().getName());
                startActivity(labelIntent);
                break;
            case R.id.FocusTState:
            case R.id.FocusLState:
                //????????????
                if (SpTool.Instance(getContext()).IsGuBbLogin()){
                    //???????????????????????????????????????,??????+?????????????????????
                    NormalActionTool.FocusAction(getContext(), this, (BaseActivity) getActivity(),
                            mAdapter.getData().get(position).getData().getTid(),
                            mAdapter.getData().get(position).getItemType()==MainMyFocusData.TeacherItem, TAG);
                }else {
                    //???????????????????????????
                    NormalActionTool.LoginAction(getContext(), this, (BaseActivity) getActivity(), null, TAG);
                }
                break;
        }
    }

    /*
     * ????????????????????????
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnNoticeRefresh(NoticeFocusPageRefreshEvent event){
        if (event.getFocusPage()==1){
            Log.d(TAG, "??????????????????????????????");
            mViewBinding.MoreFocusRefresh.setRefreshing(true);
            mPresenter.RequestMoreFocus(getContext(), this);
        }
    }

}
