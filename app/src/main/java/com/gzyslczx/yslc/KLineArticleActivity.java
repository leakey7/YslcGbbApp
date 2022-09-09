package com.gzyslczx.yslc;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.gzyslczx.yslc.adapters.kline.KLineArticleRecoListAdapter;
import com.gzyslczx.yslc.databinding.ActivityKlineArticleBinding;
import com.gzyslczx.yslc.events.GuBbKLineDetailEvent;
import com.gzyslczx.yslc.events.GuBbKLinePraiseEvent;
import com.gzyslczx.yslc.events.NoticeKLineLearnEvent;
import com.gzyslczx.yslc.presenter.KLinePresenter;
import com.gzyslczx.yslc.tools.NormalActionTool;
import com.gzyslczx.yslc.tools.SpTool;
import com.gzyslczx.yslc.tools.TransStatusTool;
import com.gzyslczx.yslc.tools.WebTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class KLineArticleActivity extends BaseActivity<ActivityKlineArticleBinding> implements View.OnClickListener, OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private final String TAG = "KLArtAct";
    public static final String KLineArtID = "KLArtID";
    public static final String KLineArtPos = "KLArtPosition";
    public static final String KLineArtCID = "KLArtCID";
    private int ArtID, CID, Position;
    private KLinePresenter mPresenter;
    private KLineArticleRecoListAdapter mRecAdapter;
    private boolean isLearn = false;

    @Override
    void InitParentLayout() {
        mViewBinding = ActivityKlineArticleBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        getWindow().setStatusBarColor(ActivityCompat.getColor(this, R.color.white));
        TransStatusTool.setStatusBarLightMode(this);
    }

    @Override
    void InitView() {
        EventBus.getDefault().register(this);
        WebTool.SetWebRichText(mViewBinding.KLArtContent);
        ArtID = getIntent().getIntExtra(KLineArtID, -1);
        Position = getIntent().getIntExtra(KLineArtPos, -1);
        CID = getIntent().getIntExtra(KLineArtCID, -1);
        mViewBinding.KLArtRecList.setLayoutManager(new LinearLayoutManager(this));
        mRecAdapter = new KLineArticleRecoListAdapter(R.layout.k_line_article_reco_item);
        //点击推荐列表条目
        mRecAdapter.setOnItemClickListener(this::onItemClick);
        mViewBinding.KLArtRecList.setAdapter(mRecAdapter);
        //点击回退
        SetupBackClicked();
        //点击点赞
        mViewBinding.KLArtPraiseImg.setOnClickListener(this::onClick);
        mViewBinding.KLArtPraiseNum.setOnClickListener(this::onClick);
        mPresenter = new KLinePresenter();
        //下拉刷新
        mViewBinding.KLArtRefresh.setOnRefreshListener(this::onRefresh);
        mViewBinding.KLArtRefresh.setRefreshing(true);
        mPresenter.RequestKLineArticle(this, this, ArtID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /*
     * 设置回退点击事件
     * */
    private void SetupBackClicked(){
        mViewBinding.KLArtToolBar.setNavigationOnClickListener(new View.OnClickListener() {
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
        if (view.getId() == R.id.KLArtPraiseImg || view.getId() == R.id.KLArtPraiseNum){
            if (SpTool.Instance(this).IsGuBbLogin()){
                mPresenter.RequestKLineLearnOrPraise(this, this, ArtID, 1, 4, Position, CID);
            }else {
                NormalActionTool.LoginAction(this, null, this, null, TAG);
            }
        }
    }

    /*
    * 设置推荐列表条目点击
    * */
    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        Intent intent = new Intent(this, KLineArticleActivity.class);
        intent.putExtra(KLineArtID, mRecAdapter.getData().get(position).getId());
        startActivity(intent);
    }

    /*
    * 刷新
    * */
    @Override
    public void onRefresh() {
        mPresenter.RequestKLineArticle(this, this, ArtID);
    }

    /*
    * 接收K线秘籍文章详情
    * */
    @Subscribe(threadMode =  ThreadMode.MAIN)
    public void OnKLineArticleDetailEvent(GuBbKLineDetailEvent event){
        Log.d(TAG, "接收到K线秘籍文章详情");
        if (event.isFlag()){
            ArtID =  event.getDetailsObj().getArtDetail().getId();
            mViewBinding.KLArtTitle.setText(event.getDetailsObj().getArtDetail().getTitle());
            mViewBinding.KLArtContent.loadDataWithBaseURL(null, WebTool.SetHtmlData(event.getDetailsObj().getArtDetail().getContent()).toString(),
                    "text/html", "utf-8", null);
            mViewBinding.KLArtRead.setText(event.getDetailsObj().getArtDetail().getReadTimes());
            if (event.getDetailsObj().getArtDetail().isLikes()){
                Glide.with(this).load(ActivityCompat.getDrawable(this, R.drawable.is_praise))
                        .fitCenter().into(mViewBinding.KLArtPraiseImg);
                mViewBinding.KLArtPraiseNum.setText(event.getDetailsObj().getArtDetail().getLikes());
            }else {
                Glide.with(this).load(ActivityCompat.getDrawable(this, R.drawable.un_praise))
                        .fitCenter().into(mViewBinding.KLArtPraiseImg);
                mViewBinding.KLArtPraiseNum.setText(getString(R.string.Praise));
                mViewBinding.KLArtPraiseNum.setTag(event.getDetailsObj().getArtDetail().getLikes());
            }
            if (TextUtils.isEmpty(event.getDetailsObj().getArtDetail().getFxsm())){
                mViewBinding.KLArtRisk.setText(getString(R.string.ArtWarm));
            }else {
                mViewBinding.KLArtRisk.setText(event.getDetailsObj().getArtDetail().getFxsm());
            }
            if (!event.getDetailsObj().getArtDetail().isLearn()) {
                NoticeKLineLearnEvent noticeKLineLearnEvent = new NoticeKLineLearnEvent(true, 2, event.getDetailsObj().getArtDetail().getId(), CID);
                EventBus.getDefault().post(noticeKLineLearnEvent);
            }
            mRecAdapter.setList(event.getDetailsObj().getArtList());
        }else {
            Toast.makeText(this, event.getError(), Toast.LENGTH_SHORT).show();
        }
        mViewBinding.KLArtRefresh.setRefreshing(false);
    }

    /*
     * 收到点赞更新
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnGuBbKLinePraiseEvent(GuBbKLinePraiseEvent event){
        Log.d(TAG, "接收到K线秘籍文章点赞更新");
        if (event.isFlag()){
            if (event.isPraise()){
                Glide.with(this).load(ActivityCompat.getDrawable(this, R.drawable.is_praise))
                        .fitCenter().into(mViewBinding.KLArtPraiseImg);
                mViewBinding.KLArtPraiseNum.setText(String.valueOf(event.getPraiseNum()));
            }else {
                Glide.with(this).load(ActivityCompat.getDrawable(this, R.drawable.un_praise))
                        .fitCenter().into(mViewBinding.KLArtPraiseImg);
                mViewBinding.KLArtPraiseNum.setText(getString(R.string.Praise));
            }
        }
    }

}