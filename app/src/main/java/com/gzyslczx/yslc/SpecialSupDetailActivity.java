package com.gzyslczx.yslc;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gzyslczx.yslc.databinding.ActivitySpecialSupDetailBinding;
import com.gzyslczx.yslc.events.GuBbChangeFocusEvent;
import com.gzyslczx.yslc.events.GuBbChangeLoginEvent;
import com.gzyslczx.yslc.events.GuBbChangeOptionStateEvent;
import com.gzyslczx.yslc.events.SpecialSupDetailEvent;
import com.gzyslczx.yslc.events.SpecialSupPraiseEvent;
import com.gzyslczx.yslc.modes.response.ResSpecialSupDetailObj;
import com.gzyslczx.yslc.presenter.SpecialSupPresenter;
import com.gzyslczx.yslc.tools.ConnPath;
import com.gzyslczx.yslc.tools.NormalActionTool;
import com.gzyslczx.yslc.tools.SpTool;
import com.gzyslczx.yslc.tools.TransStatusTool;
import com.gzyslczx.yslc.tools.customviews.SharePopup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SpecialSupDetailActivity extends BaseActivity<ActivitySpecialSupDetailBinding> implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private final String TAG = "SSDetailAct";
    public final static String SpecialSupKey = "SpecialSupID";
    public final static String SpecialSupArtKey = "SpecialSupArtID";
    private String SpecialSupID, SpecialSupArtID;
    private AnimationDrawable animationDrawable;
    private SpecialSupPresenter mPresenter;
    private SharePopup sharePopup;
    private ResSpecialSupDetailObj DataObj;

    @Override
    void InitParentLayout() {
        mViewBinding = ActivitySpecialSupDetailBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        getWindow().setStatusBarColor(ActivityCompat.getColor(this, R.color.white));
        TransStatusTool.setStatusBarLightMode(this);
    }

    @Override
    void InitView() {
        EventBus.getDefault().register(this);
        SpecialSupID = getIntent().getStringExtra(SpecialSupKey);
        SpecialSupArtID = getIntent().getStringExtra(SpecialSupArtKey);
        animationDrawable = (AnimationDrawable) mViewBinding.SSDetailPraiseImg.getBackground();
        //点击回退
        SetupBackClick();
        //点击分享
        SetupShareClick();
        //点赞
        mViewBinding.SSDetailPraiseImg.setOnClickListener(this::onClick);
        //点击自选
        mViewBinding.SSDetailAStockAdd.setOnClickListener(this::onClick);
        //点击关注
        mViewBinding.SSDetailFocus.setOnClickListener(this::onClick);
        //个股点击
        mViewBinding.SSDetailStockTag.setOnClickListener(this::onClick);
        //刷新监听
        mViewBinding.SSDetailRefresh.setOnRefreshListener(this::onRefresh);
        mPresenter = new SpecialSupPresenter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewBinding.SSDetailRefresh.setRefreshing(true);
        mPresenter.RequestSpecialSupportDetail(this, this, SpecialSupID);
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
     * 回退点击
     * */
    private void SetupBackClick() {
        mViewBinding.SSDetailToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /*
     * 分享点击
     * */
    private void SetupShareClick() {
        mViewBinding.SSDetailToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.share){
                    if (sharePopup==null){
                        sharePopup = new SharePopup(SpecialSupDetailActivity.this, mViewBinding.getRoot(), true,
                                ConnPath.SpecialSupShare, "盘前特供");
                    }
                    sharePopup.Show(mViewBinding.getRoot(), Gravity.BOTTOM, 0, 0);
                    return true;
                }
                return false;
            }
        });
    }

    /*
    * 点击事件
    * */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.SSDetailPraiseImg:
                //点赞
                if (animationDrawable.isRunning()){
                    animationDrawable.stop();
                }
                animationDrawable.start();
                mPresenter.RequestSpecialSupportPraise(this, this, null, SpecialSupID, mViewBinding.SSDetailAStockCode.getText().toString().trim());
                break;
            case R.id.SSDetailFocus:
                //点击关注
                if (SpTool.Instance(this).IsGuBbLogin()){
                    if (DataObj!=null) {
                        NormalActionTool.FocusAction(this, null, this, DataObj.getColumnId(), false, TAG);
                    }
                }else {
                    NormalActionTool.LoginAction(this, null, this, null, TAG);
                }
                break;
            case R.id.SSDetailAStockAdd:
                //点击加自选
                if (SpTool.Instance(this).IsGuBbLogin()) {
                    //已登录
                    if (DataObj!=null && !DataObj.isSelected()) {
                        NormalActionTool.AddOptionAction(this, null, this,
                                new String[]{DataObj.getStockCode()}, TAG);
                    } else if (DataObj!=null && DataObj.isSelected()){
                        NormalActionTool.DeleteOptionAction(this, null, this,
                                new String[]{DataObj.getStockCode()}, TAG);
                    }
                }else {
                    //未登录
                    NormalActionTool.LoginAction(this, null, this, null, TAG);
                }
                break;
            case R.id.SSDetailStockTag:
                if (!TextUtils.isEmpty(mViewBinding.SSDetailAStockCode.getText().toString().trim())) {
                    Intent intent = new Intent(this, WebActivity.class);
                    intent.putExtra(WebActivity.WebKey, ConnPath.StockDetailUrl + mViewBinding.SSDetailAStockCode.getText().toString().trim());
                    startActivity(intent);
                }
                break;
        }
    }

    /*
    * 接收盘前特供点赞
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnSpecialSupPraiseEvent(SpecialSupPraiseEvent event){
        if (event.isFlag()){
            Log.d(TAG, "接收到盘前特供点赞");
            if (event.isPraise()){
                mViewBinding.SSDetailPraiseNum.setTextColor(ActivityCompat.getColor(this, R.color.main_red));
                mViewBinding.SSDetailPraiseNum.setText(String.valueOf(event.getPraiseNum()));
            }else {
                mViewBinding.SSDetailPraiseNum.setTextColor(ActivityCompat.getColor(this, R.color.gray_999));
                mViewBinding.SSDetailPraiseNum.setText(getString(R.string.Praise));
            }
        }
    }

    /*
    * 接收盘前特供详情
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnSpecialSupDetailEvent(SpecialSupDetailEvent event){
        if (event.isFlag()){
            DataObj = event.getDataObj();
            mViewBinding.SSDetailName.setText(event.getDataObj().getStockName());
            mViewBinding.SSDetailTime.setText(event.getDataObj().getAddDate());
            mViewBinding.SSDetailStockTag.setText(event.getDataObj().getStockName());
            mViewBinding.SSDetailStock.setText(event.getDataObj().getStockName()+event.getDataObj().getStockCode());
            mViewBinding.SSDetailContent.setText(event.getDataObj().getRemarks());
            mViewBinding.SSDetailAStockName.setText(event.getDataObj().getStockName());
            mViewBinding.SSDetailAStockCode.setText(event.getDataObj().getStockCode());
            if (!TextUtils.isEmpty(event.getDataObj().getFxsm())) {
                mViewBinding.SSDetailRisk.setText(event.getDataObj().getFxsm());
            }
            if (event.getDataObj().isLike()){
                mViewBinding.SSDetailPraiseNum.setTextColor(ActivityCompat.getColor(this, R.color.main_red));
                mViewBinding.SSDetailPraiseNum.setText(String.valueOf(event.getDataObj().getLikeNum()));
            }else {
                mViewBinding.SSDetailPraiseNum.setTextColor(ActivityCompat.getColor(this, R.color.gray_999));
                mViewBinding.SSDetailPraiseNum.setText(getString(R.string.Praise));
            }
            ChangeFocusState(event.getDataObj().isFocus());
            ChangeOptionState(event.getDataObj().isSelected());
        }else {
            Toast.makeText(this, event.getError(), Toast.LENGTH_SHORT).show();
        }
        mViewBinding.SSDetailRefresh.setRefreshing(false);
    }

    /*
     * 显示关注状态
     * */
    private void ChangeFocusState(boolean isFocus){
        if (isFocus){
            mViewBinding.SSDetailFocus.setBackground(ActivityCompat.getDrawable(this, R.drawable.gray_focus_radius_10_shape));
            mViewBinding.SSDetailFocus.setTextColor(ActivityCompat.getColor(this, R.color.gray_666));
            mViewBinding.SSDetailFocus.setText(getString(R.string.IsFocus));
        }else {
            mViewBinding.SSDetailFocus.setBackground(ActivityCompat.getDrawable(this, R.drawable.red_border_focus_radius_10_shape));
            mViewBinding.SSDetailFocus.setTextColor(ActivityCompat.getColor(this, R.color.main_red));
            mViewBinding.SSDetailFocus.setText(getString(R.string.AddFocus));
        }
    }

    /*
     * 显示关注状态
     * */
    private void ChangeOptionState(boolean isAdd){
        if (isAdd){
            mViewBinding.SSDetailAStockAdd.setBackground(ActivityCompat.getDrawable(this, R.drawable.gray_focus_radius_10_shape));
            mViewBinding.SSDetailAStockAdd.setTextColor(ActivityCompat.getColor(this, R.color.gray_666));
            mViewBinding.SSDetailAStockAdd.setText(getString(R.string.DeleteOption));
        }else {
            mViewBinding.SSDetailAStockAdd.setBackground(ActivityCompat.getDrawable(this, R.drawable.red_border_focus_radius_10_shape));
            mViewBinding.SSDetailAStockAdd.setTextColor(ActivityCompat.getColor(this, R.color.main_red));
            mViewBinding.SSDetailAStockAdd.setText(getString(R.string.AddOption));
        }
    }

    /*
    * 刷新
    * */
    @Override
    public void onRefresh() {
        mPresenter.RequestSpecialSupportDetail(this, this, SpecialSupID);
    }

    /*
     * 接收登录状态更新
     * */
    @Subscribe(threadMode =  ThreadMode.MAIN)
    public void OnChangeLoginEvent(GuBbChangeLoginEvent event){
        //更新登录显示
        Log.d(TAG, "接收到登录更新");
        mViewBinding.SSDetailRefresh.setRefreshing(true);
        mPresenter.RequestSpecialSupportDetail(this, this, SpecialSupID);
    }

    /*
     * 接收更新关注
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnFocusChangeEvent(GuBbChangeFocusEvent event) {
        Log.d(TAG, "接收到关注更新");
        if (event.isFlag() && !event.isTeacher() && event.getFocusObj().getTid().equals(DataObj.getColumnId())){
            ChangeFocusState(event.isFocus());
        }
    }

    /*
    * 接收添删自选
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnChangeOptionStateEvent(GuBbChangeOptionStateEvent event){
        Log.d(TAG, "接收到添删自选");
        if (event.isFlag()){
            DataObj.setSelected(event.isAddOption());
            ChangeOptionState(event.isAddOption());
        }
        if (event.isAddOption()){
            Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "已删除", Toast.LENGTH_SHORT).show();
        }
    }

}