package com.gzyslczx.yslc;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gzyslczx.yslc.databinding.ActivityLabelArtBinding;
import com.gzyslczx.yslc.databinding.PhotoPopupLayoutBinding;
import com.gzyslczx.yslc.events.GuBbChangeFocusEvent;
import com.gzyslczx.yslc.events.GuBbChangePraiseEvent;
import com.gzyslczx.yslc.events.GuBbLabelDetailEvent;
import com.gzyslczx.yslc.presenter.LabelArticlePresenter;
import com.gzyslczx.yslc.tools.ConnPath;
import com.gzyslczx.yslc.tools.NormalActionTool;
import com.gzyslczx.yslc.tools.SpTool;
import com.gzyslczx.yslc.tools.TransStatusTool;
import com.gzyslczx.yslc.tools.WebTool;
import com.gzyslczx.yslc.tools.customviews.SharePopup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class LabelArtActivity extends BaseActivity<ActivityLabelArtBinding> implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, MenuItem.OnMenuItemClickListener {

    private final String TAG = "LabelArtAct";
    public static final String LabelArtKey = "LArtId";
    public static final String LabelArtPos = "LArtPos";
    private LabelArticlePresenter mPresenter;
    private String NID, LabelName, LabelID;
    private int ArtPos;
    private AnimationDrawable animationDrawable;
    private SharePopup sharePopup;

    @Override
    void InitParentLayout() {
        mViewBinding = ActivityLabelArtBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        getWindow().setStatusBarColor(ActivityCompat.getColor(this, R.color.white));
        TransStatusTool.setStatusBarLightMode(this);
    }

    @Override
    void InitView() {
        NID = getIntent().getStringExtra(LabelArtKey);
        ArtPos = getIntent().getIntExtra(LabelArtPos, -1);
        WebTool.SetWebRichText(mViewBinding.LArtContent);
        mViewBinding.LArtContent.addJavascriptInterface(this, "androidObj");
        animationDrawable = (AnimationDrawable) mViewBinding.LArtPraiseImg.getBackground();
        mViewBinding.LArtRefresh.setColorSchemeColors(ActivityCompat.getColor(this,R.color.main_red));
        mViewBinding.LArtRefresh.setOnRefreshListener(this);
        //回退点击
        SetupBackClicked();
        //分享点击
        mViewBinding.LArtToolBar.setOnMenuItemClickListener(this::onMenuItemClick);
        //点赞
        mViewBinding.LArtPraiseImg.setOnClickListener(this::onClick);
        //点击前往栏目
        mViewBinding.LArtRight.setOnClickListener(this::onClick);
        mViewBinding.LArtBar.setOnClickListener(this::onClick);
        mViewBinding.LArtLTag.setOnClickListener(this::onClick);
        mViewBinding.LArtLImg.setOnClickListener(this::onClick);
        //关注点击
        mViewBinding.LArtFocus.setOnClickListener(this::onClick);
        mPresenter = new LabelArticlePresenter();
        mViewBinding.LArtRefresh.setRefreshing(true);
        mPresenter.RequestLabelDetail(this, this, NID);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sharePopup!=null){
            sharePopup.Clear();
        }
    }

    /*
    * 设置回退点击
    * */
    private void SetupBackClicked() {
        mViewBinding.LArtToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /*
    * 刷新
    * */
    @Override
    public void onRefresh() {
        mPresenter.RequestLabelDetail(this, this, NID);
    }

    /*
    * 点击事件
    * */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.LArtFocus:
                //点击关注栏目
                if (!TextUtils.isEmpty(LabelID)) {
                    if (SpTool.Instance(this).IsGuBbLogin()) {
                        //已登录，关注操作
                        NormalActionTool.FocusAction(this, null, this, LabelID, false, TAG);
                    }else {
                        //未登录，前往登录
                        NormalActionTool.LoginAction(this, null, this, null, TAG);
                    }
                }
                break;
            case R.id.LArtBar:
            case R.id.LArtLTag:
            case R.id.LArtLImg:
            case R.id.LArtRight:
                if (!TextUtils.isEmpty(LabelName)) {
                    Intent intent = new Intent(this, LabelSelfActivity.class);
                    intent.putExtra(LabelSelfActivity.LNameKey, LabelName);
                    startActivity(intent);
                }
                break;
            case R.id.LArtPraiseImg:
                if (!TextUtils.isEmpty(NID)){
                    NormalActionTool.PraiseAction(this, null, this, ArtPos, NID, false, TAG);
                }
                if (animationDrawable.isRunning()){
                    animationDrawable.stop();
                }
                animationDrawable.start();
                break;
        }
    }

    /*
    * 弹出分享
    * */
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.share && !TextUtils.isEmpty(NID) && !TextUtils.isEmpty(mViewBinding.LArtTitle.getText().toString())) {
            if (sharePopup==null){
                sharePopup = new SharePopup(this, mViewBinding.getRoot(), true,
                        ConnPath.NormalLabelShareUrl+NID, mViewBinding.LArtTitle.getText().toString());
            }
            sharePopup.Show(mViewBinding.getRoot(), Gravity.BOTTOM, 0, 0);
            return true;
        }
        return false;
    }

    /*
     * 接收点赞更新
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnChangePraiseEvent(GuBbChangePraiseEvent event){
        if (event.isFlag() && !event.isTeacher()){
            Log.d(TAG, "接收到点赞更新");
            if (event.isPraise()){
                mViewBinding.LArtPraiseNum.setTextColor(ActivityCompat.getColor(this, R.color.main_red));
                mViewBinding.LArtPraiseNum.setText(String.valueOf(event.getPraiseNum()));
            }else {
                mViewBinding.LArtPraiseNum.setTextColor(ActivityCompat.getColor(this, R.color.gray_999));
                mViewBinding.LArtPraiseNum.setText(getString(R.string.Praise));
            }
        }
    }

    /*
     * 接收栏目关注更新
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnChangeFocusEvent(GuBbChangeFocusEvent event){
        if (event.isFlag()){
            Log.d(TAG, "接收到栏目关注更新");
            ChangeFocusState(event.isFocus());
        }
    }

    /*
    * 接收资讯详情
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnDetailEvent(GuBbLabelDetailEvent event){
        if (event.getData()!=null) {
            Log.d(TAG, String.format("接收到%s资讯详情", event.getData().getColumnName()));
            if (event.isFlag()) {
                NID = event.getData().getNewsDetail().getNewsId();
                LabelID = event.getData().getNewsDetail().getLabelId();
                LabelName = event.getData().getColumnName();
                Glide.with(LabelArtActivity.this).load(event.getData().getColumnImg())
                        .placeholder(ActivityCompat.getDrawable(LabelArtActivity.this, R.drawable.head_img))
                        .fitCenter()
                        .into(mViewBinding.LArtLogo);
                mViewBinding.LArtContent.loadDataWithBaseURL(null, WebTool.SetHtmlData(event.getData().getNewsDetail().getContent()).toString(),
                        "text/html", "utf-8", null);
                mViewBinding.LArtToolBartTitle.setText(event.getData().getColumnName());
                mViewBinding.LArtTitle.setText(event.getData().getNewsDetail().getTitle());
                mViewBinding.LArtName.setText(event.getData().getColumnName());
                mViewBinding.LArtTime.setText(event.getData().getNewsDetail().getAddDateTime());
                mViewBinding.LArtNum.setText(String.valueOf(event.getData().getArtNum()));
                if (event.getData().getNewsDetail().isLike()) {
                    mViewBinding.LArtPraiseNum.setTextColor(ActivityCompat.getColor(LabelArtActivity.this, R.color.main_red));
                    mViewBinding.LArtPraiseNum.setText(String.valueOf(event.getData().getNewsDetail().getPraise()));
                } else {
                    mViewBinding.LArtPraiseNum.setTextColor(ActivityCompat.getColor(LabelArtActivity.this, R.color.gray_999));
                    mViewBinding.LArtPraiseNum.setText(getString(R.string.Praise));
                }
                ChangeFocusState(event.getData().isFocus());
                if (TextUtils.isEmpty(event.getData().getNewsDetail().getRiskTips())) {
                    mViewBinding.LArtRisk.setText(getString(R.string.ArtWarm));
                } else {
                    mViewBinding.LArtRisk.setText(event.getData().getNewsDetail().getRiskTips());
                }
//            if (event.getData().getNewsDetail().getRecList()==null){
//                mViewBinding.LArtAboutStock.setVisibility(View.GONE);
//                mViewBinding.LArtAboutList.setVisibility(View.GONE);
//            }else {
//                mViewBinding.LArtAboutStock.setVisibility(View.VISIBLE);
//                mViewBinding.LArtAboutList.setVisibility(View.VISIBLE);
//            }
            } else {
                Toast.makeText(this, event.getError(), Toast.LENGTH_SHORT).show();
            }
        }else {
            Log.d(TAG, "无文章信息");
        }
        if (mViewBinding.LArtRefresh.isRefreshing()){
            mViewBinding.LArtRefresh.setRefreshing(false);
        }
    }

    /*
    * 显示关注状态
    * */
    private void ChangeFocusState(boolean isFocus) {
        if (isFocus){
            mViewBinding.LArtFocus.setBackground(ActivityCompat
                    .getDrawable(this, R.drawable.gray_focus_radius_10_shape));
            mViewBinding.LArtFocus.setTextColor(ActivityCompat
                    .getColor(this, R.color.gray_666));
            mViewBinding.LArtFocus.setText(getString(R.string.IsFocus));
        }else {
            mViewBinding.LArtFocus.setBackground(ActivityCompat
                    .getDrawable(this, R.drawable.red_border_focus_radius_10_shape));
            mViewBinding.LArtFocus.setTextColor(ActivityCompat
                    .getColor(this, R.color.main_red));
            mViewBinding.LArtFocus.setText(getString(R.string.AddFocus));
        }
    }

    @JavascriptInterface
    public void OpenPic(String PicUrl){
        Log.d(TAG, "打开图片");
        PopupWindow popupWindow = new PopupWindow(mViewBinding.getRoot(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        PhotoPopupLayoutBinding mBinding = PhotoPopupLayoutBinding.bind(LayoutInflater.from(this).inflate(R.layout.photo_popup_layout, null));
        popupWindow.setContentView(mBinding.getRoot());
        Glide.with(this).load(PicUrl)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        mBinding.PicScale.post(new Runnable() {
                            @Override
                            public void run() {
                                mBinding.PicScale.setImageDrawable(resource);
                            }
                        });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
        mBinding.PicScale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow!=null){
                    popupWindow.dismiss();
                }
            }
        });
        popupWindow.showAtLocation(mViewBinding.getRoot(), Gravity.BOTTOM, 0, 0);
    }

}