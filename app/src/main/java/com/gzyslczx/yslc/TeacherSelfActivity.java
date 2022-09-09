package com.gzyslczx.yslc;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.gzyslczx.yslc.adapters.ViewPagerAdapter;
import com.gzyslczx.yslc.databinding.ActivityTeacherSelfBinding;
import com.gzyslczx.yslc.events.GuBbChangeFocusEvent;
import com.gzyslczx.yslc.events.TeacherSelfInfoEvent;
import com.gzyslczx.yslc.events.TeacherSelfInfoRefreshEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.fragments.home.MainLivingFragment;
import com.gzyslczx.yslc.fragments.teacher.TeacherSelfAllFragment;
import com.gzyslczx.yslc.fragments.teacher.TeacherSelfArtFragment;
import com.gzyslczx.yslc.fragments.teacher.TeacherSelfIdeaFragment;
import com.gzyslczx.yslc.fragments.teacher.TeacherSelfSmallVideoFragment;
import com.gzyslczx.yslc.fragments.teacher.TeacherSelfVideoFragment;
import com.gzyslczx.yslc.presenter.TeacherSelfPresenter;
import com.gzyslczx.yslc.tools.AppBarScrollChangeListener;
import com.gzyslczx.yslc.tools.DisplayTool;
import com.gzyslczx.yslc.tools.NormalActionTool;
import com.gzyslczx.yslc.tools.SpTool;
import com.gzyslczx.yslc.tools.TransStatusTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class TeacherSelfActivity extends BaseActivity<ActivityTeacherSelfBinding> implements View.OnClickListener {

    private final String TAG = "TSelfAct";
    private TeacherSelfPresenter mPresenter;
//    private String[] mTabs = new String[]{"全部", "文章", "视频", "小视频", "直播", "观点"};
    private String[] mTabs1 = new String[]{"全部", "文章", "视频", "小视频", "观点"};
    private TabLayoutMediator mTabLayoutMediator;
    public static final String TIDKey = "TeacherId";
    private String TeacherID;

    @Override
    void InitParentLayout() {
        mViewBinding = ActivityTeacherSelfBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        TransStatusTool.translucent(this);
        TransStatusTool.setStatusBarDarkMode(this);
    }

    @Override
    void InitView() {
        EventBus.getDefault().register(this);
        CollapsingToolbarLayout.LayoutParams layoutParams = (CollapsingToolbarLayout.LayoutParams) mViewBinding.TSelfToolBar.getLayoutParams();
        layoutParams.topMargin = TransStatusTool.getStatusbarHeight(TeacherSelfActivity.this);
        mViewBinding.TSelfToolBar.setLayoutParams(layoutParams);
        TeacherID = getIntent().getStringExtra(TIDKey);
        //新建ViewPager适配器并初始化Fragments
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPagerAdapter.setmFragments(InitFragments());
        mViewBinding.TSelfPages.setAdapter(viewPagerAdapter);
        mViewBinding.TSelfPages.setOffscreenPageLimit(mTabs1.length - 1);
        //滑动监听
        SetupAppBarScrollListener();
        //初始化Tab
        InitTab();
        //回退点击
        SetupBackClicked();
        //点击上收下拉名师介绍
        mViewBinding.TSelfDesImg.setOnClickListener(this::onClick);
        //点击关注
        mViewBinding.TSelfFocus.setOnClickListener(this::onClick);
        mViewBinding.TSelfToolBarFocus.setOnClickListener(this::onClick);
        mPresenter = new TeacherSelfPresenter();
        //type: 0为全部 1为文章 2为观点 3为视频 4为小视频
        mPresenter.RequestTeacherSelf(this, null, this, 0, 1, TeacherID, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /*
    * 设置滑动监听
    * */
    private void SetupAppBarScrollListener() {
        mViewBinding.TSelfAppBar.addOnOffsetChangedListener(new AppBarScrollChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    //展开状态
                    TransStatusTool.setStatusBarDarkMode(TeacherSelfActivity.this);
                    mViewBinding.TSelfToolBar.setNavigationIcon(ActivityCompat.getDrawable(TeacherSelfActivity.this, R.drawable.white_left));
                    mViewBinding.TSelfToolBarFocus.setVisibility(View.GONE);
                    mViewBinding.TSelfToolBarName.setVisibility(View.GONE);
                    mViewBinding.TSelfToolBarLogo.setVisibility(View.GONE);
                } else if (state == State.COLLAPSED) {
                    //折叠状态
                    TransStatusTool.setStatusBarLightMode(TeacherSelfActivity.this);
                    mViewBinding.TSelfToolBar.setNavigationIcon(ActivityCompat.getDrawable(TeacherSelfActivity.this, R.drawable.black_left));
                    mViewBinding.TSelfToolBarFocus.setVisibility(View.VISIBLE);
                    mViewBinding.TSelfToolBarName.setVisibility(View.VISIBLE);
                    mViewBinding.TSelfToolBarLogo.setVisibility(View.VISIBLE);
                } else {
                    //中间状态

                }
            }
        });
    }

    /*
    * 设置回退点击
    * */
    private void SetupBackClicked() {
        mViewBinding.TSelfToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /*
     * 初始化Tab
     * */
    private void InitTab() {
        //自定义TabItem
        mTabLayoutMediator = new TabLayoutMediator(mViewBinding.TSelfTab, mViewBinding.TSelfPages, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
//                TextView tabView = new TextView(TeacherSelfActivity.this);
//                tabView.setTextSize(16);
//                tabView.setText(mTabs1[position]);
//                tab.setCustomView(tabView);
                tab.setText(mTabs1[position]);
            }
        });
        mTabLayoutMediator.attach();
        //绑定页面切换监听
        mViewBinding.TSelfPages.registerOnPageChangeCallback(pageChangeCallback);
    }

    /*
     * Tab切换事件
     * */
    private ViewPager2.OnPageChangeCallback pageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
//            int tabCount = mViewBinding.TSelfTab.getTabCount();
//            for (int i = 0; i < tabCount; i++) {
//                TabLayout.Tab tab = mViewBinding.TSelfTab.getTabAt(i);
//                TextView customView = (TextView) tab.getCustomView();
//                if (tab.getPosition() == position) {
//                    customView.setTextColor(ContextCompat.getColor(TeacherSelfActivity.this, R.color.black_333));
//                } else {
//                    customView.setTextColor(ContextCompat.getColor(TeacherSelfActivity.this, R.color.gray_999));
//                }
//            }
        }

    };

    /*
    * 初始化Fragments
    * */
    private List<BaseFragment> InitFragments(){
        List<BaseFragment> fragmentList = new ArrayList<BaseFragment>();
        Bundle bundle = new Bundle();
        bundle.putString(TIDKey, TeacherID);
        //全部资讯
        TeacherSelfAllFragment allFragment = new TeacherSelfAllFragment();
        allFragment.setArguments(bundle);
        fragmentList.add(allFragment);
        //文章资讯
        TeacherSelfArtFragment artFragment = new TeacherSelfArtFragment();
        artFragment.setArguments(bundle);
        fragmentList.add(artFragment);
        //视频资讯
        TeacherSelfVideoFragment videoFragment = new TeacherSelfVideoFragment();
        videoFragment.setArguments(bundle);
        fragmentList.add(videoFragment);
        //小视频资讯
        TeacherSelfSmallVideoFragment svideoFragment = new TeacherSelfSmallVideoFragment();
        svideoFragment.setArguments(bundle);
        fragmentList.add(svideoFragment);
        //直播资讯
//        if (!SpTool.Instance(this).GetHiddenLiveOrder()) {
//            MainLivingFragment livingFragment = new MainLivingFragment();
//            Bundle livingBundle = new Bundle();
//            livingBundle.putBoolean(MainLivingFragment.NeedInitKey, true);
//            livingFragment.setArguments(livingBundle);
//            fragmentList.add(livingFragment);
//        }
        //观点资讯
        TeacherSelfIdeaFragment ideaFragment = new TeacherSelfIdeaFragment();
        ideaFragment.setArguments(bundle);
        fragmentList.add(ideaFragment);
        return fragmentList;
    }

    /*
    * 点击事件
    * */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.TSelfDesImg:
                //上收下拉名师介绍
                if (mViewBinding.TSelfDesImg.getTag().equals("down")){
                    Glide.with(TeacherSelfActivity.this).load(ActivityCompat.getDrawable(TeacherSelfActivity.this, R.drawable.up))
                            .fitCenter().into(mViewBinding.TSelfDesImg);
                    mViewBinding.TSelfDesImg.setTag("up");
                    mViewBinding.TSelfDes.setMaxLines(Integer.MAX_VALUE);
                }else {
                    Glide.with(TeacherSelfActivity.this).load(ActivityCompat.getDrawable(TeacherSelfActivity.this, R.drawable.down))
                            .fitCenter().into(mViewBinding.TSelfDesImg);
                    mViewBinding.TSelfDesImg.setTag("down");
                    mViewBinding.TSelfDes.setMaxLines(2);
                }
                break;
            case R.id.TSelfFocus:
            case R.id.TSelfToolBarFocus:
                //点击关注名师
                if (SpTool.Instance(this).IsGuBbLogin()){
                    //已登录，关注操作
                    NormalActionTool.FocusAction(this, null, this, TeacherID, true, TAG);
                }else {
                    //未登录，前往登录
                    NormalActionTool.LoginAction(this, null, this, null, TAG);
                }
                break;
        }
    }

    /*
    * 接收关注更新
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnChangeFocusEvent(GuBbChangeFocusEvent event){
        Log.d(TAG, "接收到关注更新");
        if (event.isFlag() && event.isTeacher()){
            ChangeFocusState(event.isFocus());
        }else if (!event.isFlag() && event.isTeacher()){
            Log.d(TAG, String.format("关注Error= %s", event.getError()));
        }
    }

    /*
     * 显示关注状态
     * */
    private void ChangeFocusState(boolean isFocus){
        if (isFocus){
            mViewBinding.TSelfFocus.setBackground(ActivityCompat
                    .getDrawable(TeacherSelfActivity.this, R.drawable.gray_focus_radius_10_shape));
            mViewBinding.TSelfFocus.setTextColor(ActivityCompat
                    .getColor(TeacherSelfActivity.this, R.color.gray_666));
            mViewBinding.TSelfFocus.setText(getString(R.string.IsFocus));
            mViewBinding.TSelfToolBarFocus.setBackground(ActivityCompat
                    .getDrawable(TeacherSelfActivity.this, R.drawable.gray_focus_radius_10_shape));
            mViewBinding.TSelfToolBarFocus.setTextColor(ActivityCompat
                    .getColor(TeacherSelfActivity.this, R.color.gray_666));
            mViewBinding.TSelfToolBarFocus.setText(getString(R.string.IsFocus));
        }else {
            mViewBinding.TSelfFocus.setBackground(ActivityCompat
                    .getDrawable(TeacherSelfActivity.this, R.drawable.white_border_focus_radius_10_shape));
            mViewBinding.TSelfFocus.setTextColor(ActivityCompat
                    .getColor(TeacherSelfActivity.this, R.color.white));
            mViewBinding.TSelfFocus.setText(getString(R.string.AddFocus));
            mViewBinding.TSelfToolBarFocus.setBackground(ActivityCompat
                    .getDrawable(TeacherSelfActivity.this, R.drawable.red_border_focus_radius_10_shape));
            mViewBinding.TSelfToolBarFocus.setTextColor(ActivityCompat
                    .getColor(TeacherSelfActivity.this, R.color.main_red));
            mViewBinding.TSelfToolBarFocus.setText(getString(R.string.AddFocus));
        }
    }

    /*
    * 接收名师个人信息
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnTeacherSelfInfoEvent(TeacherSelfInfoEvent event){
        Log.d(TAG, "接收到名师个人信息");
        if (event.isFlag()){
            Glide.with(TeacherSelfActivity.this).load(event.getInfo().getImg())
                    .placeholder(ActivityCompat.getDrawable(TeacherSelfActivity.this, R.drawable.head_img))
                    .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(),
                            new RoundedCorners(DisplayTool.dp2px(TeacherSelfActivity.this, 60)))))
                    .into(mViewBinding.TSelfLogo);
            Glide.with(TeacherSelfActivity.this).load(event.getInfo().getImg())
                    .placeholder(ActivityCompat.getDrawable(TeacherSelfActivity.this, R.drawable.head_img))
                    .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(),
                            new RoundedCorners(DisplayTool.dp2px(TeacherSelfActivity.this, 26)))))
                    .into(mViewBinding.TSelfToolBarLogo);
            mViewBinding.TSelfName.setText(event.getInfo().getName());
            mViewBinding.TSelfToolBarName.setText(event.getInfo().getName());
            mViewBinding.TSelfIntro.setText(event.getInfo().getAdvantage());
            mViewBinding.TSelfDes.setText(event.getInfo().getIntroduce());
            mViewBinding.TSelfCode.setText(event.getInfo().getNumber());
            ChangeFocusState(event.getInfo().isFocus());
        }else {
            Toast.makeText(this, event.getError(), Toast.LENGTH_SHORT).show();
        }
    }

    /*
    * 接收刷新页面
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnTeacherSelfRefreshEvent(TeacherSelfInfoRefreshEvent event){
        Log.d(TAG, "接收到刷新页面");
        if (TextUtils.isEmpty(TeacherID)){
            Toast.makeText(this, "名师ID获取异常", Toast.LENGTH_SHORT).show();
        }else {
            mPresenter.RequestTeacherSelf(this, null, this, 1, 1, TeacherID, true);
        }
    }

    public boolean IsTFocus(){
        if (mViewBinding.TSelfFocus.getText().toString().equals(getString(R.string.AddFocus))){
            return false;
        }else {
            return true;
        }
    }

    public String getTeacherID() {
        return TeacherID;
    }
}