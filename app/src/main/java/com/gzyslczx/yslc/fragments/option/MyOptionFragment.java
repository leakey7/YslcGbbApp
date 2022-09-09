package com.gzyslczx.yslc.fragments.option;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.ManagerOptionActivity;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.SearchActivity;
import com.gzyslczx.yslc.WebActivity;
import com.gzyslczx.yslc.adapters.option.MyOptionNameListAdapter;
import com.gzyslczx.yslc.adapters.option.MyOptionValueListAdapter;
import com.gzyslczx.yslc.databinding.KonwDialogLayoutBinding;
import com.gzyslczx.yslc.databinding.MyOptionLayoutBinding;
import com.gzyslczx.yslc.events.GuBbChangeOptionStateEvent;
import com.gzyslczx.yslc.events.MyOptionEvent;
import com.gzyslczx.yslc.events.NoticeOptionPageChangeEvent;
import com.gzyslczx.yslc.events.OptionSetTopEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.presenter.OptionPresenter;
import com.gzyslczx.yslc.tools.ConnPath;
import com.gzyslczx.yslc.tools.NormalActionTool;
import com.gzyslczx.yslc.tools.NormalDialog;
import com.gzyslczx.yslc.tools.OnOptionClickedAction;
import com.gzyslczx.yslc.tools.OptionLongPopup;
import com.gzyslczx.yslc.tools.SpTool;
import com.gzyslczx.yslc.tools.interfaces.OnScrollListenerForHorizontal;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.Collections;

public class MyOptionFragment extends BaseFragment<MyOptionLayoutBinding> implements OnItemLongClickListener, OnItemClickListener,
        View.OnClickListener {

    private final String TAG = "MyOptionFrag";
    private OptionPresenter mPresenter;
    private MyOptionNameListAdapter mLeftListAdapter;
    private MyOptionValueListAdapter mRightListAdapter;
    private OptionLongPopup popup;
    private NormalDialog mDialog;
    private KonwDialogLayoutBinding mDialogBinding;
    private boolean LeftVScroll=false, RightVScroll=false;

    @Override
    protected void InitParentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = MyOptionLayoutBinding.inflate(inflater, container, false);
        InitView();
    }

    @Override
    protected void InitView() {
        EventBus.getDefault().register(this);
        mViewBinding.MyOpNameList.setLayoutManager(new LinearLayoutManager(getContext()));
        mViewBinding.MyOpList.setLayoutManager(new LinearLayoutManager(getContext()));
        mRightListAdapter = new MyOptionValueListAdapter(R.layout.my_option_list_data_item);
        mRightListAdapter.setOnItemLongClickListener(this);
        mViewBinding.MyOpList.setAdapter(mRightListAdapter);
        mLeftListAdapter = new MyOptionNameListAdapter(R.layout.my_option_name_item);
        mLeftListAdapter.setOnItemLongClickListener(this::onItemLongClick);
        mLeftListAdapter.setOnItemClickListener(this);
        mViewBinding.MyOpNameList.setAdapter(mLeftListAdapter);
        SetHorScrollAction();
        SetVerScrollAction();
        mViewBinding.MyOpRightTop.MyOpNorthImg.setOnClickListener(this::onClick);
        mViewBinding.MyOpRightTop.MyOpFinanceImg.setOnClickListener(this::onClick);
        mViewBinding.MyOpName.setOnClickListener(this::onClick);
        mViewBinding.AddOptionFoot.setOnClickListener(this::onClick);
        popup = new OptionLongPopup(LayoutInflater.from(getContext()).inflate(R.layout.long_click_option, null),
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        SetupAfterOptionAction();
        mPresenter = new OptionPresenter();
        if (!SpTool.Instance(getContext()).IsGuBbLogin()) {
            NoticeOptionPageChangeEvent event = new NoticeOptionPageChangeEvent(1);
            EventBus.getDefault().post(event);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SpTool.Instance(getContext()).IsGuBbLogin()) {
            mViewBinding.MyOpLoading.setVisibility(View.VISIBLE);
            mPresenter.RequestMyOption(getContext(), this, false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /*
    * 列表条目点击
    * */
    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        Intent intent = new Intent(getContext(), WebActivity.class);
        intent.putExtra(WebActivity.WebKey, ConnPath.StockDetailUrl+mLeftListAdapter.getData().get(position).getStockCode());
        startActivity(intent);
    }

    /*
    * 列表条目长按
    * */
    @Override
    public boolean onItemLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        Log.d(TAG, String.format("长按Item=%d, 列表总个数%d", position, mLeftListAdapter.getData().size()));
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        View child = mViewBinding.MyOpList.getLayoutManager().findViewByPosition(position);
        int[] location = new int[2];
        child.getLocationInWindow(location);
        popup.setSelectPosition(position);
        popup.showAtLocation(child, Gravity.TOP, 0, location[1] - child.getHeight());
        mLeftListAdapter.getViewByPosition(position, R.id.MyOpNameBg).setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_F7));
        mRightListAdapter.getViewByPosition(position, R.id.MyOpBelongDataBg).setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_F7));
        mRightListAdapter.getViewByPosition(position, R.id.MyOpNorthData).setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_F7));
        mRightListAdapter.getViewByPosition(position, R.id.MyOpFinanceData).setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_F7));
        mRightListAdapter.getViewByPosition(position, R.id.MyOpNetProfitData).setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_F7));
        return true;
    }

    /*
     * 左右滑动动作
     * */
    private void SetHorScrollAction() {
        mViewBinding.MyOpRightDataScroll.setSubScroll(mViewBinding.MyOpRightTopScroll);
        mViewBinding.MyOpRightTopScroll.setSubScroll(mViewBinding.MyOpRightDataScroll);
    }

    /*
     * 上下滚动动作
     * */
    private void SetVerScrollAction() {
        mViewBinding.MyOpNameList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == SCROLL_STATE_TOUCH_SCROLL){
                    LeftVScroll = true;
                    RightVScroll = false;
                    mViewBinding.MyOpNameList.setBackground(null);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!RightVScroll) {
                    mViewBinding.MyOpList.scrollBy(dx, dy);
                }
            }
        });
        mViewBinding.MyOpList.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == SCROLL_STATE_TOUCH_SCROLL){
                    RightVScroll = true;
                    LeftVScroll = false;
                }
                if (newState == SCROLL_STATE_IDLE){
                    mViewBinding.MyOpNameList.setBackground(null);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dx==0 && !LeftVScroll){
                    mViewBinding.MyOpNameList.scrollBy(dx, dy);
                }
            }
        });
    }

    /*
    * 点击事件
    * */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.MyOpNorthImg:
                //北向资金问号
                SetupDialog(getString(R.string.NorthOption));
                break;
            case R.id.MyOpFinanceImg:
                SetupDialog(getString(R.string.FinanceOption));
                break;
            case R.id.AddOptionFoot:
                //添加自选
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.MyOpName:
                //点击编辑
                Intent managerOptionIntent = new Intent(getContext(), ManagerOptionActivity.class);
                managerOptionIntent.putExtra(ManagerOptionActivity.ListKey, (Serializable) mRightListAdapter.getData());
                startActivity(managerOptionIntent);
                break;
        }
    }

    /*
     * 初始化问号弹窗
     * */
    private void SetupDialog(String text){
        if (mDialog==null){
            mDialogBinding = KonwDialogLayoutBinding.bind(getLayoutInflater().inflate(R.layout.konw_dialog_layout, null));
            mDialog = new NormalDialog(getContext(), mDialogBinding.getRoot(), true);
            mDialogBinding.NormalDialogText.setText(text);
            mDialogBinding.NormalDialogKnow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialog.DismissDialog();
                }
            });
        }else {
            mDialogBinding.NormalDialogText.setText(text);
        }
        mDialog.ShowDialog();
    }

    /*
     * 设置长按后点击动作事件
     * */
    private void SetupAfterOptionAction() {
        popup.setClickedAction(new OnOptionClickedAction() {
            @Override
            public void OnSelectOptionAction(OptionLongPopup.Action action, int pos) {
                switch (action) {
                    case SETMANAGER:
                        //前往自选股管理界面
                        Intent intent = new Intent(getContext(), ManagerOptionActivity.class);
                        intent.putExtra(ManagerOptionActivity.ListKey, (Serializable) mRightListAdapter.getData());
                        startActivity(intent);
                        break;
                    case SETTOP:
                        //置顶
                        mPresenter.RequestSetTopMyOption(getContext(), mRightListAdapter.getData().get(pos).getStockCode(),
                                MyOptionFragment.this, (BaseActivity) getActivity());
                        break;
                    case DELETED:
                        //删除
                        NormalActionTool.DeleteOptionAction(getContext(), MyOptionFragment.this, (BaseActivity) getActivity(),
                                new String[]{mRightListAdapter.getData().get(pos).getStockCode()}, TAG);
                        break;
                }
            }
        });
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popup.SetNormalAction();
                int position = popup.getSelectPosition();
                mLeftListAdapter.getViewByPosition(position, R.id.MyOpNameBg).setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                mRightListAdapter.getViewByPosition(position, R.id.MyOpBelongDataBg).setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                mRightListAdapter.getViewByPosition(position, R.id.MyOpNorthData).setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                mRightListAdapter.getViewByPosition(position, R.id.MyOpFinanceData).setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                mRightListAdapter.getViewByPosition(position, R.id.MyOpNetProfitData).setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
            }
        });
    }

    /*
    * 接收我的自选股
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMyOptionEvent(MyOptionEvent event){
        if (event.isFlag()){
            Log.d(TAG, "接收到我的自选股");
            mLeftListAdapter.setList(event.getDataList());
            mRightListAdapter.setList(event.getDataList());
        }
        mViewBinding.MyOpLoading.setVisibility(View.GONE);
    }

    /*
     * 接收我的自选股置顶
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMyOptionSetTopEvent(OptionSetTopEvent event){
        if (event.isFlag()){
            int position = popup.getSelectPosition();
            mLeftListAdapter.getData().get(position).setTop(true);
            mLeftListAdapter.getData().get(0).setTop(false);
            mRightListAdapter.getData().get(position).setTop(true);
            mRightListAdapter.getData().get(0).setTop(false);
            Collections.sort(mLeftListAdapter.getData());
            Collections.sort(mRightListAdapter.getData());
            mLeftListAdapter.notifyDataSetChanged();
            mRightListAdapter.notifyDataSetChanged();
        }else {
            Toast.makeText(getContext(), event.getError(), Toast.LENGTH_SHORT).show();
        }
    }

    /*
     * 接收我的自选股增删
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMyOptionDeleteEvent(GuBbChangeOptionStateEvent event){
        if (event.isFlag() && !event.isAddOption()){
            Log.d(TAG, "接收到添删自选");
            int position = popup.getSelectPosition();
            for (int i=0; i<mLeftListAdapter.getData().size(); i++){
                for (String code : event.getStockCodes()){
                    if (code.equals(mLeftListAdapter.getData().get(i).getStockCode())){
                        mLeftListAdapter.removeAt(position);
                        mRightListAdapter.removeAt(position);
                    }
                }
            }
        }else if (event.isFlag() && event.isAddOption()){
            mViewBinding.MyOpLoading.setVisibility(View.VISIBLE);
            mPresenter.RequestMyOption(getContext(), this, false);
        }
    }

    /*
     * 接收通知刷新自选页面
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnNoticeOptionChangePageEvent(NoticeOptionPageChangeEvent event){
        if (event.getCurrentPage()==0) {
            Log.d(TAG, "接收到通知刷新自选页面");
            mViewBinding.MyOpLoading.setVisibility(View.VISIBLE);
            mPresenter.RequestMyOption(getContext(), this, false);
        }
    }

}
