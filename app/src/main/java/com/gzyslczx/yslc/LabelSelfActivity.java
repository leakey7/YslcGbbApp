package com.gzyslczx.yslc;

import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.google.android.material.appbar.AppBarLayout;
import com.gzyslczx.yslc.adapters.label.LabelSelfAdapter;
import com.gzyslczx.yslc.adapters.label.bean.LabelSelfData;
import com.gzyslczx.yslc.databinding.ActivityLabelSelfBinding;
import com.gzyslczx.yslc.events.GuBbChangeFocusEvent;
import com.gzyslczx.yslc.events.GuBbChangePraiseEvent;
import com.gzyslczx.yslc.events.LabelSelfInfoEvent;
import com.gzyslczx.yslc.events.LabelSelfListEvent;
import com.gzyslczx.yslc.presenter.LabelSelfActPresenter;
import com.gzyslczx.yslc.tools.AppBarScrollChangeListener;
import com.gzyslczx.yslc.tools.ConnPath;
import com.gzyslczx.yslc.tools.DisplayTool;
import com.gzyslczx.yslc.tools.NormalActionTool;
import com.gzyslczx.yslc.tools.SpTool;
import com.gzyslczx.yslc.tools.TransStatusTool;
import com.gzyslczx.yslc.tools.customviews.SharePopup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class LabelSelfActivity extends BaseActivity<ActivityLabelSelfBinding> implements OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener, OnItemChildClickListener, OnItemClickListener {

    private final String TAG = "LabelSelfAct";
    private LabelSelfActPresenter mPresenter;
    public static final String LNameKey = "LSelfName";
    private String LabelName, LabelId;
    private LabelSelfAdapter mAdapter;
    private SharePopup sharePopup;

    @Override
    void InitParentLayout() {
        mViewBinding = ActivityLabelSelfBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        TransStatusTool.translucent(this);
        TransStatusTool.setStatusBarDarkMode(this);
    }

    @Override
    void InitView() {
        EventBus.getDefault().register(this);
        //回退点击
        LabelName = getIntent().getStringExtra(LNameKey);
        //添加状态栏高度
        int height = TransStatusTool.getStatusbarHeight(this);
        FrameLayout.LayoutParams toolBarParams = (FrameLayout.LayoutParams) mViewBinding.LabelSelfToolBar.getLayoutParams();
        toolBarParams.setMargins(0, height, 0, 0);
        mViewBinding.LabelSelfToolBar.setLayoutParams(toolBarParams);
        ConstraintLayout.LayoutParams logoParams = (ConstraintLayout.LayoutParams) mViewBinding.LabelSelfLogo.getLayoutParams();
        logoParams.setMargins(DisplayTool.dp2px(this, 14),
                DisplayTool.dp2px(this, 64)+height, 0, 0);
        mViewBinding.LabelSelfLogo.setLayoutParams(logoParams);
        //设置列表
        mViewBinding.LabelSelfList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new LabelSelfAdapter(new ArrayList<LabelSelfData>());
        mAdapter.setOnItemChildClickListener(this::onItemChildClick);
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(this::onLoadMore);
        mAdapter.setOnItemClickListener(this::onItemClick);
        mViewBinding.LabelSelfList.setAdapter(mAdapter);
        //设置刷新
        mViewBinding.LabelSelfRefresh.setOnRefreshListener(this::onRefresh);
        //点击回退
        SetupBackOnClicked();
        //滑动监听
        SetupAppBarScrollListener();
        //关注点击
        mViewBinding.LabelSelfFocus.setOnClickListener(this::onClick);
        mViewBinding.LabelSelfToolBarFocus.setOnClickListener(this::onClick);
        //初始化Presenter
        mPresenter = new LabelSelfActPresenter();
        mViewBinding.LabelSelfRefresh.setColorSchemeColors(ActivityCompat.getColor(this, R.color.main_red));
        mViewBinding.LabelSelfRefresh.setRefreshing(true);
        //请求栏目专页信息
        mPresenter.RequestLabelSelf(this, this, LabelName);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (sharePopup != null){
            sharePopup.Clear();
        }
    }

    /*
    * 设置回退点击
    * */
    private void SetupBackOnClicked(){
        mViewBinding.LabelSelfToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /*
     * AppBar滑动监听适应配置
     * */
    private void SetupAppBarScrollListener(){
        mViewBinding.LabelSelfAppBar.addOnOffsetChangedListener(new AppBarScrollChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    //展开状态
                    TransStatusTool.setStatusBarDarkMode(LabelSelfActivity.this);
                    mViewBinding.LabelSelfToolBar.setNavigationIcon(ActivityCompat.getDrawable(LabelSelfActivity.this, R.drawable.white_left));
                    mViewBinding.LabelSelfToolBarLogo.setVisibility(View.GONE);
                    mViewBinding.LabelSelfToolBarTitle.setVisibility(View.GONE);
                    mViewBinding.LabelSelfToolBarFocus.setVisibility(View.GONE);
                } else if (state == State.COLLAPSED) {
                    //折叠状态
                    TransStatusTool.setStatusBarLightMode(LabelSelfActivity.this);
                    mViewBinding.LabelSelfToolBar.setNavigationIcon(ActivityCompat.getDrawable(LabelSelfActivity.this, R.drawable.black_left));
                    mViewBinding.LabelSelfToolBarLogo.setVisibility(View.VISIBLE);
                    mViewBinding.LabelSelfToolBarTitle.setVisibility(View.VISIBLE);
                    mViewBinding.LabelSelfToolBarFocus.setVisibility(View.VISIBLE);
                } else {
                    //中间状态

                }
            }
        });
    }

    /*
    * 接收栏目头部信息
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnLabelSelfInfoEvent(LabelSelfInfoEvent event){
        if (event.isFlag()){
            Log.d(TAG, "接收到栏目头部信息");
            Glide.with(this).load(event.getLabelSelfInfo().getImg())
                    .placeholder(ActivityCompat.getDrawable(this, R.drawable.thump_shape))
                    .fitCenter().into(mViewBinding.LabelSelfLogo);
            Glide.with(this).load(event.getLabelSelfInfo().getImg())
                    .placeholder(ActivityCompat.getDrawable(this, R.drawable.thump_shape))
                    .fitCenter().into(mViewBinding.LabelSelfToolBarLogo);
            mViewBinding.LabelSelfName.setText(event.getLabelSelfInfo().getName());
            mViewBinding.LabelSelfToolBarTitle.setText(event.getLabelSelfInfo().getName());
            mViewBinding.LabelSelfDes.setText(event.getLabelSelfInfo().getDesc());
            LabelId = event.getLabelSelfInfo().getId();
            ChangeFocusState(event.getLabelSelfInfo().isFocus());
            mViewBinding.LabelSelfPraiseNum.setText(String.valueOf(event.getLabelSelfInfo().getLikeNum()));
            mViewBinding.LabelSelfStarNum.setText(String.valueOf(event.getLabelSelfInfo().getFocusNum()));
        }
    }

    /*
     * 显示关注状态
     * */
    private void ChangeFocusState(boolean isFocus){
        if (isFocus) {
            mViewBinding.LabelSelfFocus.setBackground(ActivityCompat
                    .getDrawable(this, R.drawable.gray_focus_radius_10_shape));
            mViewBinding.LabelSelfFocus.setTextColor(ActivityCompat
                    .getColor(this, R.color.gray_666));
            mViewBinding.LabelSelfFocus.setText(getString(R.string.IsFocus));
            mViewBinding.LabelSelfToolBarFocus.setBackground(ActivityCompat
                    .getDrawable(this, R.drawable.gray_focus_radius_10_shape));
            mViewBinding.LabelSelfToolBarFocus.setTextColor(ActivityCompat
                    .getColor(this, R.color.gray_666));
            mViewBinding.LabelSelfToolBarFocus.setText(getString(R.string.IsFocus));
        }else {
            mViewBinding.LabelSelfFocus.setBackground(ActivityCompat
                    .getDrawable(this, R.drawable.white_border_focus_radius_10_shape));
            mViewBinding.LabelSelfFocus.setTextColor(ActivityCompat
                    .getColor(this, R.color.white));
            mViewBinding.LabelSelfFocus.setText(getString(R.string.AddFocus));
            mViewBinding.LabelSelfToolBarFocus.setBackground(ActivityCompat
                    .getDrawable(this, R.drawable.red_border_focus_radius_10_shape));
            mViewBinding.LabelSelfToolBarFocus.setTextColor(ActivityCompat
                    .getColor(this, R.color.main_red));
            mViewBinding.LabelSelfToolBarFocus.setText(getString(R.string.AddFocus));
        }
    }

    /*
    * 接收栏目列表
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnLabelSelfListEvent(LabelSelfListEvent event){
        Log.d(TAG, "接收到栏目列表");
        if (event.isFlag()){
            if (mViewBinding.LabelSelfRefresh.isRefreshing()){
                mAdapter.setList(event.getDataList());
                mViewBinding.LabelSelfRefresh.setRefreshing(false);
            }else {
                mAdapter.addData(event.getDataList());
            }
            if (event.isEnd()){
                mAdapter.getLoadMoreModule().loadMoreEnd();
            }else {
                mAdapter.getLoadMoreModule().loadMoreComplete();
            }
        }else {
            if (mViewBinding.LabelSelfRefresh.isRefreshing()){
                mViewBinding.LabelSelfRefresh.setRefreshing(false);
            }
            if (mAdapter.getLoadMoreModule().isLoading()) {
                mAdapter.getLoadMoreModule().loadMoreFail();
            }
            Toast.makeText(this, event.getError(), Toast.LENGTH_SHORT).show();
        }
    }

    /*
    * 加载更多
    * */
    @Override
    public void onLoadMore() {
        mPresenter.RequestLabelSelf(this, this, LabelName);
    }

    /*
    * 刷新
    * */
    @Override
    public void onRefresh() {
        mPresenter.setCurrentPage(1);
        mPresenter.RequestLabelSelf(this, this, LabelName);
    }

    /*
    * 接收栏目关注更新
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnChangeFocusEvent(GuBbChangeFocusEvent event){
        if (event.isFlag() && !event.isTeacher()){
            Log.d(TAG, "接收到栏目关注更新");
            ChangeFocusState(event.isFocus());
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.LabelSelfFocus || view.getId()==R.id.LabelSelfToolBarFocus){
            //点击关注
            if (SpTool.Instance(this).IsGuBbLogin()) {
                //已登录关注操作
                NormalActionTool.FocusAction(this, null, this, LabelId, false, TAG);
            }else {
                //未登录前往登录
                NormalActionTool.LoginAction(this, null, this, null, TAG);
            }
        }
    }

    /*
    * 列表子控件点击
    * */
    @Override
    public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        switch (view.getId()){
            case R.id.LSelfJTextPraiseImg:
            case R.id.LSelfJTextPraiseNum:
            case R.id.LSelfAPicPraiseImg:
            case R.id.LSelfAPicPraiseNum:
            case R.id.LSelfMPicPraiseImg:
            case R.id.LSelfMPicPraiseNum:
            case R.id.LSelfHVPraiseImg:
            case R.id.LSelfHVPraiseNum:
            case R.id.LSelfVVPraiseImg:
            case R.id.LSelfVVPraiseNum:
                //点击了点赞
                if (SpTool.Instance(this).IsGuBbLogin()){
                    //已登录，点赞操作
                    NormalActionTool.PraiseAction(this, null, this, position,
                            mAdapter.getData().get(position).getInfo().getNewsId(), false, TAG);
                }else {
                    //未登录，跳转至登录界面
                    NormalActionTool.LoginAction(this, null, this, null, TAG);
                }
                break;
            case R.id.LSelfJTextShare:
            case R.id.LSelfJTextShareImg:
            case R.id.LSelfAPicShare:
            case R.id.LSelfAPicShareImg:
            case R.id.LSelfMPicShare:
            case R.id.LSelfMPicShareImg:
            case R.id.LSelfHVShare:
            case R.id.LSelfHVShareImg:
            case R.id.LSelfVVShare:
            case R.id.LSelfVVShareImg:
                //点击分享
                if (sharePopup==null){
                    sharePopup = new SharePopup(this, mViewBinding.getRoot(), true);
                }
                sharePopup.setUrlPath(ConnPath.NormalLabelShareUrl+mAdapter.getData().get(position).getInfo().getNewsId());
                sharePopup.setTitle(mAdapter.getData().get(position).getInfo().getTitle());
                sharePopup.Show(mViewBinding.getRoot(), Gravity.BOTTOM, 0, 0);
                break;
        }
    }

    /*
     * 接收点赞更新
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnChangePraiseEvent(GuBbChangePraiseEvent event){
        if (event.isFlag() && !event.isTeacher()){
            Log.d(TAG, "接收到点赞更新");
            mAdapter.getData().get(event.getPosition()).getInfo().setLike(event.isPraise());
            if (event.isPraise()){
                mAdapter.getData().get(event.getPosition()).getInfo().setPraise(event.getPraiseNum());
                int oldPraise = Integer.parseInt(mViewBinding.LabelSelfPraiseNum.getText().toString());
                mViewBinding.LabelSelfPraiseNum.setText(String.valueOf(oldPraise+1));
            }else {
                int oldPraise = Integer.parseInt(mViewBinding.LabelSelfPraiseNum.getText().toString());
                mViewBinding.LabelSelfPraiseNum.setText(String.valueOf(oldPraise-1));
            }
            mAdapter.notifyItemRangeChanged(event.getPosition(), 1);
        }
    }

    /*
    * 列表条目点击
    * */
    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        Intent intent = new Intent(this, LabelArtActivity.class);
        intent.putExtra(LabelArtActivity.LabelArtKey, mAdapter.getData().get(position).getInfo().getNewsId());
        intent.putExtra(LabelArtActivity.LabelArtPos, position);
        startActivity(intent);
    }
}