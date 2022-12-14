package com.gzyslczx.yslc;

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
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gzyslczx.yslc.databinding.ActivityTeacherArtBinding;
import com.gzyslczx.yslc.databinding.PhotoPopupLayoutBinding;
import com.gzyslczx.yslc.events.GuBbChangeFocusEvent;
import com.gzyslczx.yslc.events.GuBbChangeLoginEvent;
import com.gzyslczx.yslc.events.GuBbChangePraiseEvent;
import com.gzyslczx.yslc.events.GuBbTeacherDetailEvent;
import com.gzyslczx.yslc.presenter.TeacherArticlePresenter;
import com.gzyslczx.yslc.tools.ConnPath;
import com.gzyslczx.yslc.tools.DisplayTool;
import com.gzyslczx.yslc.tools.NormalActionTool;
import com.gzyslczx.yslc.tools.SpTool;
import com.gzyslczx.yslc.tools.TransStatusTool;
import com.gzyslczx.yslc.tools.WebTool;
import com.gzyslczx.yslc.tools.customviews.SharePopup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class TeacherArtActivity extends BaseActivity<ActivityTeacherArtBinding> implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, MenuItem.OnMenuItemClickListener{

    private final String TAG = "TeacherArtAct";
    public static final String TeacherArtKey = "TArtId";
    public static final String TeacherArtPos = "TArtPos";
    private TeacherArticlePresenter mPresenter;
    private String NID, TeacherID;
    private int ArtPos;
    private AnimationDrawable animationDrawable;
    private SharePopup sharePopup;

    @Override
    void InitParentLayout() {
        mViewBinding = ActivityTeacherArtBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        getWindow().setStatusBarColor(ActivityCompat.getColor(this, R.color.white));
        TransStatusTool.setStatusBarLightMode(this);
    }

    @Override
    void InitView() {
        NID = getIntent().getStringExtra(TeacherArtKey);
        ArtPos = getIntent().getIntExtra(TeacherArtPos, -1);
        WebTool.SetWebRichText(mViewBinding.TArtContent);
        animationDrawable = (AnimationDrawable) mViewBinding.TArtPraiseImg.getBackground();
        mViewBinding.TArtRefresh.setColorSchemeColors(ActivityCompat.getColor(this,R.color.main_red));
        mViewBinding.TArtRefresh.setOnRefreshListener(this);
        //????????????
        SetupBackClicked();
        //????????????
        mViewBinding.TArtToolBar.setOnMenuItemClickListener(this::onMenuItemClick);
        //??????
        mViewBinding.TArtPraiseImg.setOnClickListener(this::onClick);
        //????????????
        mViewBinding.TArtFocus.setOnClickListener(this::onClick);
        mPresenter = new TeacherArticlePresenter();
        mViewBinding.TArtRefresh.setRefreshing(true);
        mPresenter.RequestDetail(this, this, NID);
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
    * ??????????????????
    * */
    private void SetupBackClicked() {
        mViewBinding.TArtToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /*
    * ??????????????????
    * */
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.share && !TextUtils.isEmpty(NID) && !TextUtils.isEmpty(mViewBinding.TArtTitle.getText().toString())) {
            if (sharePopup==null){
                sharePopup = new SharePopup(this, mViewBinding.getRoot(), true,
                        ConnPath.TeacherArticleShareUrl+NID, mViewBinding.TArtTitle.getText().toString());
            }
            sharePopup.Show(mViewBinding.getRoot(), Gravity.BOTTOM, 0, 0);
            return true;
        }
        return false;
    }

    /*
    * ????????????
    * */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.TArtFocus:
                //??????????????????
                if (!TextUtils.isEmpty(TeacherID)) {
                    if (SpTool.Instance(this).IsGuBbLogin()) {
                        //????????????????????????
                        NormalActionTool.FocusAction(this, null, this, TeacherID, true, TAG);
                    }else {
                        //????????????????????????
                        NormalActionTool.LoginAction(this, null, this, null, TAG);
                    }
                }
                break;
            case R.id.TArtPraiseImg:
                if (SpTool.Instance(this).IsGuBbLogin()) {
                    if (!TextUtils.isEmpty(NID)) {
                        NormalActionTool.PraiseAction(this, null, this, ArtPos, NID, true, TAG);
                    }
                    if (animationDrawable.isRunning()) {
                        animationDrawable.stop();
                    }
                    animationDrawable.start();
                }else {
                    NormalActionTool.LoginAction(this, null, this, null, TAG);
                }
                break;
        }
    }

    /*
    * ??????
    * */
    @Override
    public void onRefresh() {
        mPresenter.RequestDetail(this ,this, NID);
    }

    /*
     * ??????????????????
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnChangePraiseEvent(GuBbChangePraiseEvent event){
        if (event.isFlag() && event.isTeacher()){
            Log.d(TAG, "?????????????????????");
            if (event.isPraise()){
                mViewBinding.TArtPraiseNum.setTextColor(ActivityCompat.getColor(this, R.color.main_red));
                mViewBinding.TArtPraiseNum.setText(String.valueOf(event.getPraiseNum()));
            }else {
                mViewBinding.TArtPraiseNum.setTextColor(ActivityCompat.getColor(this, R.color.gray_999));
                mViewBinding.TArtPraiseNum.setText(getString(R.string.Praise));
            }
        }
    }

    /*
     * ????????????????????????
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnChangeFocusEvent(GuBbChangeFocusEvent event){
        if (event.isFlag()){
            Log.d(TAG, "???????????????????????????");
            ChangeFocusState(event.isFocus());
        }
    }

    /*
    * ??????????????????
    * */
    private void ChangeFocusState(boolean focus) {
        if (focus){
            mViewBinding.TArtFocus.setBackground(ActivityCompat
                    .getDrawable(this, R.drawable.gray_focus_radius_10_shape));
            mViewBinding.TArtFocus.setTextColor(ActivityCompat
                    .getColor(this, R.color.gray_666));
            mViewBinding.TArtFocus.setText(getString(R.string.IsFocus));
        }else {
            mViewBinding.TArtFocus.setBackground(ActivityCompat
                    .getDrawable(this, R.drawable.red_border_focus_radius_10_shape));
            mViewBinding.TArtFocus.setTextColor(ActivityCompat
                    .getColor(this, R.color.main_red));
            mViewBinding.TArtFocus.setText(getString(R.string.AddFocus));
        }
    }

    /*
     * ??????????????????
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnDetailEvent(GuBbTeacherDetailEvent event){
        Log.d(TAG, "?????????????????????");
        if (event.getData()!=null) {
            if (event.isFlag()) {
                NID = event.getData().getNewsId();
                TeacherID = event.getData().getUserId();
                Glide.with(this).load(event.getData().getAuthorImg())
                        .placeholder(ActivityCompat.getDrawable(this, R.drawable.head_img))
                        .apply(new RequestOptions().transform(new MultiTransformation<>(new FitCenter(), new RoundedCorners(DisplayTool.dp2px(this, 44)))))
                        .into(mViewBinding.TArtLogo);
                mViewBinding.TArtContent.loadDataWithBaseURL(null, WebTool.SetHtmlData(event.getData().getContent()).toString(),
                        "text/html", "utf-8", null);
                mViewBinding.TArtToolBartTitle.setText(event.getData().getTitle());
                mViewBinding.TArtTitle.setText(event.getData().getTitle());
                mViewBinding.TArtName.setText(event.getData().getAuthor());
                mViewBinding.TArtTime.setText(event.getData().getAddDateTime());
                if (event.getData().isLike()) {
                    mViewBinding.TArtPraiseNum.setTextColor(ActivityCompat.getColor(this, R.color.main_red));
                    mViewBinding.TArtPraiseNum.setText(String.valueOf(event.getData().getPraise()));
                } else {
                    mViewBinding.TArtPraiseNum.setTextColor(ActivityCompat.getColor(this, R.color.gray_999));
                    mViewBinding.TArtPraiseNum.setText(getString(R.string.Praise));
                }
                ChangeFocusState(event.getData().isFocus());
                if (TextUtils.isEmpty(event.getData().getRiskTips())) {
                    mViewBinding.TArtRisk.setText(getString(R.string.ArtWarm));
                } else {
                    mViewBinding.TArtRisk.setText(event.getData().getRiskTips());
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
            Log.d(TAG, "???????????????");
        }
        if (mViewBinding.TArtRefresh.isRefreshing()){
            mViewBinding.TArtRefresh.setRefreshing(false);
        }
    }

    @JavascriptInterface
    public void OpenPic(String PicUrl){
        Log.d(TAG, "????????????");
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

    /*
     * ????????????????????????
     * */
    @Subscribe(threadMode =  ThreadMode.MAIN)
    public void OnChangeLoginEvent(GuBbChangeLoginEvent event){
        //??????????????????
        Log.d(TAG, "?????????????????????");
        mPresenter.RequestDetail(this, this, NID);
    }

}