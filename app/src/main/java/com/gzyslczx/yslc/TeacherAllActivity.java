package com.gzyslczx.yslc;

import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.gzyslczx.yslc.adapters.teacher.TeacherAllAdapter;
import com.gzyslczx.yslc.adapters.teacher.bean.TeacherAllData;
import com.gzyslczx.yslc.databinding.ActivityTeacharAllBinding;
import com.gzyslczx.yslc.events.GuBbChangeFocusEvent;
import com.gzyslczx.yslc.events.GuBbChangeLoginEvent;
import com.gzyslczx.yslc.events.GuBbChangePraiseEvent;
import com.gzyslczx.yslc.events.GuBbTeacherAllEvent;
import com.gzyslczx.yslc.events.NoticePraiseUpdateEvent;
import com.gzyslczx.yslc.modes.response.ResScrollSmallVideoInfo;
import com.gzyslczx.yslc.presenter.GuBbTeacherAllPresenter;
import com.gzyslczx.yslc.tools.ConnPath;
import com.gzyslczx.yslc.tools.NormalActionTool;
import com.gzyslczx.yslc.tools.SpTool;
import com.gzyslczx.yslc.tools.TransStatusTool;
import com.gzyslczx.yslc.tools.customviews.SharePopup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class TeacherAllActivity extends BaseActivity<ActivityTeacharAllBinding> implements SwipeRefreshLayout.OnRefreshListener, OnItemClickListener, OnItemChildClickListener {

    private final String TAG = "TLabelAct";
    private GuBbTeacherAllPresenter mPresenter;
    private TeacherAllAdapter mAdapter;
    private int PraisePos=-1;
    private SharePopup sharePopup;


    @Override
    void InitParentLayout() {
        mViewBinding = ActivityTeacharAllBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        getWindow().setStatusBarColor(ActivityCompat.getColor(this, R.color.white));
        TransStatusTool.setStatusBarLightMode(this);
    }

    @Override
    void InitView() {
        EventBus.getDefault().register(this);
        mViewBinding.TLabelList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TeacherAllAdapter(new ArrayList<TeacherAllData>());
        mAdapter.setOnItemChildClickListener(this::onItemChildClick);
        mAdapter.setOnItemClickListener(this::onItemClick);
        mViewBinding.TLabelList.setAdapter(mAdapter);
        mViewBinding.TLabelRefresh.setOnRefreshListener(this);
        //点击回退
        SetupBackClicked();
        mPresenter = new GuBbTeacherAllPresenter();
        mViewBinding.TLabelRefresh.setRefreshing(true);
        mPresenter.RequestTeacherAllList(this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (sharePopup!=null){
            sharePopup.Clear();
        }
    }

    /*
    * 设置回退点击
    * */
    private void SetupBackClicked() {
        mViewBinding.TLabelToolBar.setNavigationOnClickListener(new View.OnClickListener() {
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
        mPresenter.RequestTeacherAllList(this, this);
    }

    /*
    * 列表条目子控件点击
    * */
    @Override
    public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        switch (view.getId()){
            case R.id.tJTextHeadImg:
            case R.id.tAPicHeadImg:
            case R.id.tMPicHeadImg:
            case R.id.tHVHeadImg:
            case R.id.tVVHeadImg:
            case R.id.FocusTHeadImg:
                //点击头像
                Intent tIntent = new Intent(this, TeacherSelfActivity.class);
                tIntent.putExtra(TeacherSelfActivity.TIDKey, mAdapter.getData().get(position).getObjData().getId());
                startActivity(tIntent);
                break;
            case R.id.tJTextFocus:
            case R.id.tAPicFocus:
            case R.id.tMPicFocus:
            case R.id.tHVFocus:
            case R.id.tVVFocus:
            case R.id.FocusTState:
                //点击关注
                if (SpTool.Instance(this).IsGuBbLogin()){
                    //已登录
                    NormalActionTool.FocusAction(this, null, this, mAdapter.getData().get(position).getObjData().getId(), true, TAG);
                }else {
                    //未登录
                    NormalActionTool.LoginAction(this, null, this, null, TAG);
                }
                break;
            case R.id.tJTextPraiseImg:
            case R.id.tAPicPraiseImg:
            case R.id.tMPicPraiseImg:
            case R.id.tHVPraiseImg:
            case R.id.tVVPraiseImg:
            case R.id.tJTextPraiseNum:
            case R.id.tAPicPraiseNum:
            case R.id.tMPicPraiseNum:
            case R.id.tHVPraiseNum:
            case R.id.tVVPraiseNum:
                //点击点赞
                if (SpTool.Instance(this).IsGuBbLogin()){
                    //已登录
                    PraisePos = position;
                    NormalActionTool.PraiseAction(this, null, this, position, mAdapter.getData().get(position).getObjData().getNewsId(), true, TAG);
                }else {
                    //未登录
                    NormalActionTool.LoginAction(this, null, this, null, TAG);
                }
                break;
            case R.id.tJTextShare:
            case R.id.tAPicShare:
            case R.id.tMPicShare:
            case R.id.tHVShare:
            case R.id.tVVShare:
            case R.id.tJTextShareImg:
            case R.id.tAPicShareImg:
            case R.id.tMPicShareImg:
            case R.id.tHVShareImg:
            case R.id.tVVShareImg:
                //点击分享
                if (sharePopup==null){
                    sharePopup = new SharePopup(this, mViewBinding.getRoot(), true);
                }
                if (mAdapter.getData().get(position).getItemType() == TeacherAllData.BVideo){
                    //大视频
                    sharePopup.setUrlPath(ConnPath.BigVideoShareUrl+mAdapter.getData().get(position).getObjData().getNewsId());
                }else if (mAdapter.getData().get(position).getItemType() == TeacherAllData.SVideo){
                    //小视频
                    sharePopup.setUrlPath(ConnPath.SmallVideoShareUrl+mAdapter.getData().get(position).getObjData().getNewsId());
                }else {
                    //文章
                    sharePopup.setUrlPath(ConnPath.TeacherArticleShareUrl+mAdapter.getData().get(position).getObjData().getNewsId());
                }
                sharePopup.setTitle(mAdapter.getData().get(position).getObjData().getTitle());
                sharePopup.Show(mViewBinding.getRoot(), Gravity.BOTTOM, 0, 0);
                break;

        }
    }

    /*
    * 列表条目点击
    * */
    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        switch (mAdapter.getData().get(position).getItemType()){
            case TeacherAllData.JustText:
            case TeacherAllData.OnePic:
            case TeacherAllData.MorePic:
                Intent tArtIntent = new Intent(this, TeacherArtActivity.class);
                tArtIntent.putExtra(TeacherArtActivity.TeacherArtKey, mAdapter.getData().get(position).getObjData().getNewsId());
                tArtIntent.putExtra(TeacherArtActivity.TeacherArtPos, position);
                startActivity(tArtIntent);
                break;
            case TeacherAllData.BVideo:
                Intent bVideoIntent = new Intent(this, BigVideoActivity.class);
                bVideoIntent.putExtra(BigVideoActivity.BidVideoKey, mAdapter.getData().get(position).getObjData().getNewsId());
                bVideoIntent.putExtra(BigVideoActivity.TeacherVideoPos, position);
                startActivity(bVideoIntent);
                break;
            case TeacherAllData.SVideo:
                Intent intent = new Intent(this, SmallVideoActivity.class);
                ResScrollSmallVideoInfo info = new ResScrollSmallVideoInfo();
                info.setAuthor(mAdapter.getData().get(position).getObjData().getName());
                info.setAuthorImg(mAdapter.getData().get(position).getObjData().getImg());
                info.setDescribe("");
                info.setFocus(mAdapter.getData().get(position).getObjData().isFocus());
                info.setFileUrl(mAdapter.getData().get(position).getObjData().getDesc());
                info.setNewsId(mAdapter.getData().get(position).getObjData().getNewsId());
                info.setUserId(mAdapter.getData().get(position).getObjData().getId());
                info.setTitle(mAdapter.getData().get(position).getObjData().getTitle());
                info.setPraise(mAdapter.getData().get(position).getObjData().getLikeNum());
                info.setLike(mAdapter.getData().get(position).getObjData().isLike());
                intent.putExtra(SmallVideoActivity.FirstVideo, info);
                startActivity(intent);
                break;
            case TeacherAllData.NoneContent:
                Intent tIntent = new Intent(this, TeacherSelfActivity.class);
                tIntent.putExtra(TeacherSelfActivity.TIDKey, mAdapter.getData().get(position).getObjData().getId());
                startActivity(tIntent);
                break;
        }
    }

    /*
    * 接收名字专栏列表
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnTeacherAllEvent(GuBbTeacherAllEvent event){
        if (event.isFlag()){
            mAdapter.setList(event.getDataList());
        }else {
            Toast.makeText(this, event.getError(), Toast.LENGTH_SHORT).show();
        }
        mViewBinding.TLabelRefresh.setRefreshing(false);
    }

    /*
     * 接收登录状态更新
     * */
    @Subscribe(threadMode =  ThreadMode.MAIN)
    public void OnChangeLoginEvent(GuBbChangeLoginEvent event){
        //更新登录显示
        Log.d(TAG, "接收到登录更新");
        mViewBinding.TLabelRefresh.setRefreshing(true);
        mPresenter.RequestTeacherAllList(this, this);
    }

    /*
     * 接收更新关注
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnFocusChangeEvent(GuBbChangeFocusEvent event){
        Log.d(TAG, "接收到关注更新");
        if (event.isFlag()){
            if (event.isTeacher()){
                //更新名师关注
                for (int i=0; i<mAdapter.getData().size(); i++){
                    if (mAdapter.getData().get(i).getObjData().getId().equals(event.getFocusObj().getTid())){
                        mAdapter.getData().get(i).getObjData().setFocus(event.isFocus());
                    }
                }
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    /*
     * 接收点赞更新
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnChangePraiseEvent(GuBbChangePraiseEvent event){
        if (PraisePos!=-1 && event.isFlag()){
            Log.d(TAG, "接收到点赞更新");
            mAdapter.getData().get(PraisePos).getObjData().setLike(event.isPraise());
            if (event.isPraise()){
                mAdapter.getData().get(PraisePos).getObjData().setLikeNum(event.getPraiseNum());
            }
            mAdapter.notifyItemRangeChanged(PraisePos,1);
            PraisePos = -1;
        }
    }

    /*
     * 接收通知点赞更新
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnNoticePraiseUpdateEvent(NoticePraiseUpdateEvent event){
        if (PraisePos==-1 && event.isFlag()){
            Log.d(TAG, "接收到通知点赞更新");
            if (event.isTeacher()){
                //更新名师点赞
                for (int i=0; i<mAdapter.getData().size(); i++){
                    if (mAdapter.getData().get(i).getObjData().getNewsId().equals(event.getNID())){
                        mAdapter.getData().get(i).getObjData().setLike(event.isPraise());
                        if (event.isPraise()) {
                            mAdapter.getData().get(i).getObjData().setLikeNum(event.getPraiseNum());
                        }
                        mAdapter.notifyItemRangeChanged(i,1);
                        break;
                    }
                }
            }
        }
    }
}