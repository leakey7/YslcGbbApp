package com.gzyslczx.yslc.fragments.home;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.MsgBoxActivity;
import com.gzyslczx.yslc.MyApp;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.SearchActivity;
import com.gzyslczx.yslc.SpecialSupActivity;
import com.gzyslczx.yslc.SpecialSupDetailActivity;
import com.gzyslczx.yslc.WebActivity;
import com.gzyslczx.yslc.adapters.ViewPagerAdapter;
import com.gzyslczx.yslc.adapters.main.MainFragIconAdapter;
import com.gzyslczx.yslc.adapters.main.MainFragSpecialSupAdapter;
import com.gzyslczx.yslc.adapters.main.MainMovementAdapter;
import com.gzyslczx.yslc.adapters.main.MainRiskRadarAdapter;
import com.gzyslczx.yslc.adapters.main.MainTeacherLivingAdapter;
import com.gzyslczx.yslc.databinding.MainFragmentBinding;
import com.gzyslczx.yslc.databinding.ProtocolDialogLayoutBinding;
import com.gzyslczx.yslc.events.GuBbAdvEvent;
import com.gzyslczx.yslc.events.GuBbHomeChangePageEvent;
import com.gzyslczx.yslc.events.GuBbMainMovementEvent;
import com.gzyslczx.yslc.events.GuBbMainRiskRadarEvent;
import com.gzyslczx.yslc.events.GuBbMainSupEvent;
import com.gzyslczx.yslc.events.GuBbMainTeacherLivingEvent;
import com.gzyslczx.yslc.events.GuBbTeacherDynEvent;
import com.gzyslczx.yslc.events.GuBbTokenEvent;
import com.gzyslczx.yslc.events.MainPageStateEvent;
import com.gzyslczx.yslc.events.NoticeMainRefreshEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.modes.response.ResAdvObj;
import com.gzyslczx.yslc.modes.response.ResTeacherDynObj;
import com.gzyslczx.yslc.presenter.MainFragmentPresenter;
import com.gzyslczx.yslc.tools.AppBarScrollChangeListener;
import com.gzyslczx.yslc.tools.ConnPath;
import com.gzyslczx.yslc.tools.DisplayTool;
import com.gzyslczx.yslc.tools.JGVerifyLogin;
import com.gzyslczx.yslc.tools.NormalActionTool;
import com.gzyslczx.yslc.tools.SecretCodeTool;
import com.gzyslczx.yslc.tools.SpTool;
import com.gzyslczx.yslc.tools.TransStatusTool;
import com.qw.curtain.lib.Curtain;
import com.qw.curtain.lib.CurtainFlow;
import com.qw.curtain.lib.IGuide;
import com.qw.curtain.lib.ViewGetter;
import com.qw.curtain.lib.flow.CurtainFlowInterface;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.jiguang.api.utils.JCollectionAuth;
import cn.jiguang.verifysdk.api.JVerificationInterface;
import cn.jiguang.verifysdk.api.RequestCallback;
import cn.jpush.android.api.JPushInterface;

public class MainFragment extends BaseFragment<MainFragmentBinding> implements View.OnClickListener {

    private final String TAG = "MainFrag";
    private MainFragmentPresenter mPresenter;
    //Tab??????
    private String[] mTabs = new String[]{"??????", "??????", "????????????", "??????", "?????????", "??????"};
    //?????????????????????????????????
    private MainPageStateEvent stateEvent;
    //Icon?????????
    private MainFragIconAdapter mIconAdapter;
    //?????????????????????
    private MainFragSpecialSupAdapter mSupAdapter;
    //??????????????????
    private List<ResTeacherDynObj> mTeacherDynData;
    //?????????????????????
    private MainTeacherLivingAdapter mTLivingAdapter;
    //?????????????????????
    private MainMovementAdapter mMovementAdapter;
    //?????????????????????
    private MainRiskRadarAdapter mRiskRadarAdapter;
    private TabLayoutMediator mTabLayoutMediator;
    private boolean isInit=false;

    @Override
    protected void InitParentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = MainFragmentBinding.inflate(inflater, container, false);
        InitView();
    }

    @Override
    protected void InitView() {
        //????????????
        SetupAppBarScrollListener();
        //??????Icon
        mIconAdapter = new MainFragIconAdapter(getContext());
        mViewBinding.MainIcons.setAdapter(mIconAdapter);
        //??????????????????
        mSupAdapter = new MainFragSpecialSupAdapter(getContext());
        mViewBinding.MainSupGrid.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mViewBinding.MainSupGrid.setAdapter(mSupAdapter);
        //??????????????????
        if (!SpTool.Instance(getContext()).GetHiddenLiveOrder()) {
            mViewBinding.MainTLivingList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        }else {
            mViewBinding.MainTLivingTag.setVisibility(View.GONE);
            mViewBinding.MainTLivingList.setVisibility(View.GONE);
        }
        //??????????????????
        mMovementAdapter = new MainMovementAdapter(getContext());
        mViewBinding.MainMovementGrid.setAdapter(mMovementAdapter);
        //??????????????????????????????
        AddMovementItemClicked();
        //????????????
        mViewBinding.MainToolBarSearchText.setOnClickListener(this::onClick);
        mViewBinding.MainToolBarSearchBg.setOnClickListener(this::onClick);
        mViewBinding.MainToolBarSearchImg.setOnClickListener(this::onClick);
        //????????????????????????
        mViewBinding.MainMovementNorthBuy.setOnClickListener(this::onClick);
        //????????????????????????
        mViewBinding.MainMovementInstitutionBuy.setOnClickListener(this::onClick);
        //????????????????????????
        mViewBinding.MainMovementFinancingBuy.setOnClickListener(this::onClick);
        //??????????????????????????????
        mViewBinding.MainMovementLookMore.setOnClickListener(this::onClick);
        //??????????????????
        mViewBinding.MainToolBarMsgImg.setOnClickListener(this::onClick);
        //????????????
        mViewBinding.MainRefresh.setOnClickListener(this::onClick);
        //??????????????????
        mViewBinding.MainRiskRadarList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mRiskRadarAdapter = new MainRiskRadarAdapter(R.layout.main_risk_radar_item);
        AddRiskRadarItemClicked();
        mViewBinding.MainRiskRadarList.setAdapter(mRiskRadarAdapter);
        mViewBinding.MainRiskRadarList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                if (parent.getChildAdapterPosition(view) == mRiskRadarAdapter.getData().size() - 1) {
                    outRect.set(0, 0, 0, 0);
                } else {
                    outRect.set(0, 0, DisplayTool.dp2px(getContext(), 8), 0);
                }
            }
        });
        //??????ViewPager???Tab
        mViewBinding.MainPager.setUserInputEnabled(true);
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(this);
        pagerAdapter.setmFragments(InitFragment());
        mViewBinding.MainPager.setAdapter(pagerAdapter);
        mViewBinding.MainPager.setOffscreenPageLimit(mTabs.length - 1);
        InitTab();
        mPresenter = new MainFragmentPresenter();
        isInit=true;
        /*
        * ??????????????????
        * */
        if (SpTool.Instance(getContext()).GetFirst()) {
            JCollectionAuth.setAuth(getContext(), false);
            ShowTipAlter();
        }else {
            JCollectionAuth.setAuth(getContext(),true);
            InitJGVerify();
            InitJGPush();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isInit){
            EventBus.getDefault().register(this);
            mPresenter.RequestToken(getContext(), this);
            isInit=false;
        }
    }

    /*
    * ?????????FragmentList
    * */
    private List<BaseFragment> InitFragment() {
        List<BaseFragment> fragments = new ArrayList<BaseFragment>();
        //??????
        fragments.add(new MainFocusFragment());
        //??????
        fragments.add(new MainRecoFragment());
        //????????????
        fragments.add(new YsFlashFragment());
        //??????
        MainVideoFragment HVideoFragment = new MainVideoFragment();
        Bundle HVBundle = new Bundle();
        HVBundle.putInt(MainVideoFragment.VideoTypeKey, 2);
        HVideoFragment.setArguments(HVBundle);
        fragments.add(HVideoFragment);
        //?????????
        MainVideoFragment VVideoFragment = new MainVideoFragment();
        Bundle VVBundle = new Bundle();
        VVBundle.putInt(MainVideoFragment.VideoTypeKey, 3);
        VVideoFragment.setArguments(VVBundle);
        fragments.add(VVideoFragment);
        //??????
        if (!SpTool.Instance(getContext()).GetHiddenLiveOrder()) {
            fragments.add(new MainLivingFragment());
        }
        return fragments;
    }

    @Override
    public void onDestroy() {
        mTabLayoutMediator.detach();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /*
     * ?????????Tab
     * */
    private void InitTab() {
        //?????????TabItem
        mTabLayoutMediator = new TabLayoutMediator(mViewBinding.MainTab, mViewBinding.MainPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                TextView tabView = new TextView(getContext());
                tabView.setTextSize(16);
                tabView.setText(mTabs[position]);
                tab.setCustomView(tabView);
            }
        });
        mTabLayoutMediator.attach();
        //????????????????????????
        mViewBinding.MainPager.registerOnPageChangeCallback(pageChangeCallback);
        //???????????????????????????
        mViewBinding.MainPager.setCurrentItem(1);
    }

    /*
     * Tab????????????
     * */
    private ViewPager2.OnPageChangeCallback pageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            int tabCount = mViewBinding.MainTab.getTabCount();
            for (int i = 0; i < tabCount; i++) {
                TabLayout.Tab tab = mViewBinding.MainTab.getTabAt(i);
                TextView customView = (TextView) tab.getCustomView();
                if (tab.getPosition() == position) {
                    customView.setTextColor(ContextCompat.getColor(getContext(), R.color.black_333));
                } else {
                    customView.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_999));
                }
            }
        }
    };

    /*
    * AppBar????????????
    * */
    private void SetupAppBarScrollListener(){
        stateEvent = new MainPageStateEvent();
        mViewBinding.MainAppBar.addOnOffsetChangedListener(new AppBarScrollChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    //????????????
                    TransStatusTool.translucent((BaseActivity) getActivity());
                    TransStatusTool.setStatusBarDarkMode(getActivity());
                    mViewBinding.MainToolBarMsgImg.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.white)));
                    mViewBinding.MainToolBarSearchImg.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.white)));
                    mViewBinding.MainToolBarSearchText.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    stateEvent.setState(0);
                    EventBus.getDefault().post(stateEvent);
                } else if (state == State.COLLAPSED) {
                    //????????????
                    getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.white));
                    TransStatusTool.setStatusBarLightMode(getActivity());
                    mViewBinding.MainToolBarMsgImg.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.black)));
                    mViewBinding.MainToolBarSearchImg.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.black)));
                    mViewBinding.MainToolBarSearchText.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                    stateEvent.setState(1);
                    EventBus.getDefault().post(stateEvent);
                } else {
                    //????????????

                }
            }
        });
    }

    /*
    * ??????Banner
    * */
    private void SetupBanner(List<ResAdvObj> data){
        mViewBinding.MainBanner.setAdapter(new BannerImageAdapter<ResAdvObj>(data) {
            @Override
            public void onBindView(BannerImageHolder holder, ResAdvObj data, int position, int size) {
                ImageView imageView = holder.imageView;
                Glide.with(getContext()).load(data.getImg()).override(imageView.getWidth(), imageView.getHeight()).centerCrop().dontAnimate().into(imageView);
            }
        });
        SetupBannerOnClick();
        mViewBinding.MainBanner.start();
    }

    /*
     * ??????Banner????????????
     * */
    private void SetupBannerOnClick() {
        mViewBinding.MainBanner.setOnBannerListener(new OnBannerListener<ResAdvObj>() {
            @Override
            public void OnBannerClick(ResAdvObj data, int position) {
                Log.d(TAG, String.format("Banner?????????%d???????????????:%s", position, data.getAppUrl()));
                Intent intent = SecretCodeTool.AnalysisSecretCode(getContext(), data.getAppUrl());
                if (intent!=null){
                    startActivity(intent);
                }
            }
        });
    }

    /*
    * ??????????????????????????????
    * */
    private void SetupSpecialSupportOnClick(){
        mViewBinding.MainSupGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), SpecialSupDetailActivity.class);
                intent.putExtra(SpecialSupDetailActivity.SpecialSupKey, mSupAdapter.getData().get(i).getSId());
                intent.putExtra(SpecialSupDetailActivity.SpecialSupArtKey, mSupAdapter.getData().get(i).getNsId());
                startActivity(intent);
            }
        });
        mViewBinding.MainSupMore.setOnClickListener(this::onClick);
        mViewBinding.MainSupRight.setOnClickListener(this::onClick);
        mViewBinding.MainSupGood.setOnClickListener(this::onClick);
    }

    /*
    * ??????Token
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnTokenEvent(GuBbTokenEvent event){
        Log.d(TAG, "?????????Token");
        if (event.isFLAG()){
            //??????Icon
            mPresenter.ContactGuBbAdv(getContext(), this, 1);
            //??????Banner
            mPresenter.ContactGuBbAdv(getContext(), this, 7);
            //??????????????????
            mPresenter.ContactSpecialSupport(getContext(), this);
            //??????????????????
            mPresenter.ContactTeacherDyn(getContext(), this);
            //??????????????????
            if (!SpTool.Instance(getContext()).GetHiddenLiveOrder()) {
                mPresenter.ContactTeacherLiving(getContext(), this);
            }
            //??????????????????????????????
            mPresenter.ContactNorthBuy(getContext(), this);
            //??????????????????
            mPresenter.ContactRiskRadar(getContext(), this);
        }else {
            Toast.makeText(getContext(), event.getERROR(), Toast.LENGTH_SHORT).show();
        }

    }

    /*
    * ????????????????????????
    * id???1????????????????????? 2????????? 3?????????????????? 4?????????????????? 5??????????????? 6????????????????????? 7??????????????????????????? 8????????????????????? 9???????????????
    * */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void OnSetupBannerEvent(GuBbAdvEvent event){
        int id = event.getId();
        switch (id){
            case 1:
                Log.d(TAG, "???????????????Icon");
                if (event.isFlag()){
                    mIconAdapter.setDataList(event.getDataList());
                    mIconAdapter.notifyDataSetChanged();
                    //Icon??????
                    IconOnClicked();
                }else {
                    Log.d(TAG, String.format("Main Icon Error=%s", event.getError()));
                }
                break;
            case 7:
                Log.d(TAG, "???????????????Banner");
                if (event.isFlag()) {
                    SetupBanner(event.getDataList());
                }else {
                    Log.d(TAG, String.format("Banner Error=%s", event.getError()));
                }
                break;
        }
    }

    /*
    * ??????????????????
    * */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void OnSetupSpecialSupportEvent(GuBbMainSupEvent event){
        Log.d(TAG, "???????????????????????????");
        if (event.isFlag()){
            mSupAdapter.setData(event.getDataList());
            mSupAdapter.notifyDataSetChanged();
            mViewBinding.MainSupTime.setText(event.getDataList().get(0).getAddDate().substring(event.getDataList().get(0).getAddDate().indexOf('-') + 1));
            SetupSpecialSupportOnClick();
        }else {
            Log.d(TAG, String.format("???????????? Error=%s", event.getError()));
        }
    }

    /*
    * ??????????????????
    * */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void OnSetupTeacherDynEvent(GuBbTeacherDynEvent event){
        Log.d(TAG, "?????????????????????");
        if (event.isFlag()){
            mViewBinding.MainDynamicFlipper.removeAllViews();
            for (int i = 0; i < event.getDataList().size(); i++) {
                TextView textView = new TextView(getContext());
                textView.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                textView.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                textView.setMaxLines(2);
                textView.setEllipsize(TextUtils.TruncateAt.END);
                textView.setTextColor(ContextCompat.getColor(getContext(), R.color.black_333));
                textView.setTextSize(14);
                textView.setGravity(Gravity.CENTER_VERTICAL);
                textView.setText(event.getDataList().get(i).getTitle());
                mViewBinding.MainDynamicFlipper.addView(textView);
            }
            //??????????????????????????????
            mViewBinding.MainDynamicFlipper.setOnClickListener(this::onClick);
            mTeacherDynData = event.getDataList();
            mViewBinding.MainDynamicFlipper.startFlipping();
        }
    }

    /*
    * ??????????????????
    * */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void OnSetupTeacherLivingEvent(GuBbMainTeacherLivingEvent event){
        Log.d(TAG, "?????????????????????");
        if (event.isFlag()){
            if (mTLivingAdapter==null){
                mTLivingAdapter = new MainTeacherLivingAdapter(event.getLivingData());
                int width = mViewBinding.MainTLivingList.getWidth()/2-14;
                mTLivingAdapter.setDefWidth(width);
                AddTeacherLivingItemClicked();
                mViewBinding.MainTLivingList.setAdapter(mTLivingAdapter);
                mViewBinding.MainTLivingList.addItemDecoration(new RecyclerView.ItemDecoration() {
                    @Override
                    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                        if (parent.getChildAdapterPosition(view) == event.getLivingData().size() - 1) {
                            outRect.set(0, 0, 0, 0);
                        } else {
                            outRect.set(0, 0, DisplayTool.dp2px(getContext(), 8), 0);
                        }
                    }
                });
            }else {
                mTLivingAdapter.setList(event.getLivingData());
            }
            //??????????????????????????????
            mViewBinding.MainTLivingMore.setOnClickListener(this::onClick);
            mViewBinding.MainTLivingRight.setOnClickListener(this::onClick);
        }else {
            Log.d(TAG, String.format("???????????? Error=%s", event.getError()));
        }
    }

    /*
    * ????????????Item??????
    * */
    private void AddTeacherLivingItemClicked(){
        mTLivingAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                if (SpTool.Instance(getContext()).IsGuBbLogin()) {
                    Intent intent = new Intent(getContext(), WebActivity.class);
                    intent.putExtra(WebActivity.WebKey, mTLivingAdapter.getData().get(position).getDataObj().getVideourl());
                    intent.putExtra(WebActivity.WebVideoKey, true);
                    startActivity(intent);
                }else {
                    NormalActionTool.LoginAction(getContext(), MainFragment.this, (BaseActivity) getActivity(), null, TAG);
                }
            }
        });
    }

    /*
    * ????????????
    * */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.MainSupGood:
            case R.id.MainSupMore:
            case R.id.MainSupRight:
                //?????????????????????????????????????????????Activity
                Intent SpecialSupIntent = new Intent(getContext(), SpecialSupActivity.class);
                SpecialSupIntent.putExtra(SpecialSupActivity.SupportDate, mSupAdapter.getData().get(0).getAddDate());
                startActivity(SpecialSupIntent);
                break;
            case R.id.MainDynamicFlipper:
                //??????????????????
                int position = mViewBinding.MainDynamicFlipper.getDisplayedChild();
                Intent teacherDynIntent = SecretCodeTool.AnalysisSecretCode(getContext(), mTeacherDynData.get(position).getAPPUrl());
                if (teacherDynIntent!=null){
                    startActivity(teacherDynIntent);
                }
                break;
            case R.id.MainTLivingRight:
            case R.id.MainTLivingMore:
                //?????????????????????????????????LivingFragment
                GuBbHomeChangePageEvent event = new GuBbHomeChangePageEvent(1);
                EventBus.getDefault().post(event);
                break;
            case R.id.MainMovementNorthBuy:
                //????????????????????????
                mViewBinding.MainMovementNorthBuy.setBackgroundColor(ActivityCompat.getColor(getContext(), R.color.yellow_ffe4c0));
                mViewBinding.MainMovementInstitutionBuy.setBackground(null);
                mViewBinding.MainMovementFinancingBuy.setBackground(null);
                if (mMovementAdapter.getNorthBuy()==null){
                    //????????????????????????????????????
                    mViewBinding.MainMovementProgress.setVisibility(View.VISIBLE);
                    mViewBinding.MainMovementInstitutionBuy.setClickable(false);
                    mViewBinding.MainMovementFinancingBuy.setClickable(false);
                    mPresenter.ContactNorthBuy(getContext(), this);
                }else {
                    //?????????????????????????????????
                    if (mMovementAdapter.getItemType()!=MainMovementAdapter.NorthItem) {
                        mMovementAdapter.setItemType(MainMovementAdapter.NorthItem);
                        mMovementAdapter.notifyDataSetChanged();
                        mViewBinding.MainMovementTip.setText(mMovementAdapter.getNorthBuy().getResultObj().get(0).getQuoteDate()
                                +getString(R.string.north_buy));
                        mViewBinding.MainMovementValueTag.setText(getString(R.string.increase_market_value));
                        mViewBinding.MainMovementRatioTag.setText(getString(R.string.proportion_of_total_equity));
                    }
                }
                break;
            case R.id.MainMovementInstitutionBuy:
                //??????????????????
                mViewBinding.MainMovementInstitutionBuy.setBackgroundColor(ActivityCompat.getColor(getContext(), R.color.yellow_ffe4c0));
                mViewBinding.MainMovementNorthBuy.setBackground(null);
                mViewBinding.MainMovementFinancingBuy.setBackground(null);
                if (mMovementAdapter.getInstitutionsBuy()==null){
                    //????????????????????????????????????
                    mViewBinding.MainMovementProgress.setVisibility(View.VISIBLE);
                    mViewBinding.MainMovementNorthBuy.setClickable(false);
                    mViewBinding.MainMovementFinancingBuy.setClickable(false);
                    mPresenter.ContactInstitutionBuy(getContext(), this);
                }else {
                    //?????????????????????????????????
                    if (mMovementAdapter.getItemType()!=MainMovementAdapter.InstitutionItem) {
                        mMovementAdapter.setItemType(MainMovementAdapter.InstitutionItem);
                        mMovementAdapter.notifyDataSetChanged();
                        mViewBinding.MainMovementTip.setText(mMovementAdapter.getInstitutionsBuy().getResultObj().get(0).getQuoteDate()
                                +getString(R.string.institutions_buy));
                        mViewBinding.MainMovementValueTag.setText(getString(R.string.increase_market_value));
                        mViewBinding.MainMovementRatioTag.setText(getString(R.string.proportion_of_total_equity));
                    }
                }
                break;
            case R.id.MainMovementFinancingBuy:
                //??????????????????
                mViewBinding.MainMovementFinancingBuy.setBackgroundColor(ActivityCompat.getColor(getContext(), R.color.yellow_ffe4c0));
                mViewBinding.MainMovementNorthBuy.setBackground(null);
                mViewBinding.MainMovementInstitutionBuy.setBackground(null);
                if (mMovementAdapter.getFinancingBuy()==null){
                    //????????????????????????????????????
                    mViewBinding.MainMovementProgress.setVisibility(View.VISIBLE);
                    mViewBinding.MainMovementNorthBuy.setClickable(false);
                    mViewBinding.MainMovementInstitutionBuy.setClickable(false);
                    mPresenter.ContactFinancingBuy(getContext(), this);
                }else {
                    //?????????????????????????????????
                    if (mMovementAdapter.getItemType()!=MainMovementAdapter.FinancingItem) {
                        mMovementAdapter.setItemType(MainMovementAdapter.FinancingItem);
                        mMovementAdapter.notifyDataSetChanged();
                        mViewBinding.MainMovementTip.setText(mMovementAdapter.getFinancingBuy().getResultObj().get(0).getQuoteDate()
                                +getString(R.string.financing_buy));
                        mViewBinding.MainMovementValueTag.setText(getString(R.string.financing_balance));
                        mViewBinding.MainMovementRatioTag.setText(getString(R.string.financing_balance_ration));
                    }
                }
                break;
            case R.id.MainMovementLookMore:
                if (mMovementAdapter.getItemType() == MainMovementAdapter.NorthItem && mMovementAdapter.getNorthBuy()!=null){
                    //????????????
                    Intent northBuyIntent = SecretCodeTool.AnalysisSecretCode(getContext(),  mMovementAdapter.getNorthBuy().getResultObj().get(0).getMoreUrl());
                    if (northBuyIntent!=null){
                        startActivity(northBuyIntent);
                    }
                    break;
                }
                if (mMovementAdapter.getItemType() == MainMovementAdapter.InstitutionItem && mMovementAdapter.getInstitutionsBuy()!=null){
                    //????????????
                    Intent institutionsBuyIntent = SecretCodeTool.AnalysisSecretCode(getContext(),  mMovementAdapter.getInstitutionsBuy().getResultObj().get(0).getMoreUrl());
                    if (institutionsBuyIntent!=null){
                        startActivity(institutionsBuyIntent);
                    }
                    break;
                }
                if (mMovementAdapter.getItemType() == MainMovementAdapter.FinancingItem && mMovementAdapter.getFinancingBuy()!=null){
                    //????????????
                    Intent financingBuy = SecretCodeTool.AnalysisSecretCode(getContext(),  mMovementAdapter.getFinancingBuy().getResultObj().get(0).getMoreUrl());
                    if (financingBuy!=null){
                        startActivity(financingBuy);
                    }
                    break;
                }
                break;
            case R.id.MainToolBarSearchBg:
            case R.id.MainToolBarSearchText:
            case R.id.MainToolBarSearchImg:
                Intent searchIntent = new Intent(getContext(), SearchActivity.class);
                startActivity(searchIntent);
                break;
            case R.id.MainToolBarMsgImg:
                Intent msgBoxIntent = new Intent(getContext(), MsgBoxActivity.class);
                startActivity(msgBoxIntent);
                break;
            case R.id.MainRefresh:
                NoticeMainRefreshEvent noticeMainRefreshEvent = new NoticeMainRefreshEvent(mViewBinding.MainPager.getCurrentItem());
                EventBus.getDefault().post(noticeMainRefreshEvent);
                break;
        }
    }

    /*
     * Icon??????
     * */
    private void IconOnClicked() {
        mViewBinding.MainIcons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, String.format("Icon??????:%s", mIconAdapter.getDataList().get(i).getAppUrl()));
                Intent intent = SecretCodeTool.AnalysisSecretCode(getContext(), mIconAdapter.getDataList().get(i).getAppUrl());
                if (intent != null) {
                    startActivity(intent);
                }
            }
        });
    }

    /*
    * ??????????????????
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMovementEvent(GuBbMainMovementEvent event){
        mViewBinding.MainMovementProgress.setVisibility(View.GONE);
        mViewBinding.MainMovementNorthBuy.setClickable(true);
        mViewBinding.MainMovementInstitutionBuy.setClickable(true);
        mViewBinding.MainMovementFinancingBuy.setClickable(true);
        if (event.isFlag()){
            Log.d(TAG, "?????????????????????");
            if (event.getItemType() == MainMovementAdapter.NorthItem){
                //??????????????????
                Log.d(TAG, "?????????????????????-????????????");
                mMovementAdapter.setItemType(MainMovementAdapter.NorthItem);
                mMovementAdapter.setNorthBuy(event.getNorthBuy());
                mMovementAdapter.notifyDataSetChanged();
                mViewBinding.MainMovementTip.setText(event.getNorthBuy().getResultObj().get(0).getQuoteDate()+getString(R.string.north_buy));
                mViewBinding.MainMovementValueTag.setText(getString(R.string.increase_market_value));
                mViewBinding.MainMovementRatioTag.setText(getString(R.string.proportion_of_total_equity));
                return;
            }
            if (event.getItemType() == MainMovementAdapter.InstitutionItem){
                //??????????????????
                Log.d(TAG, "?????????????????????-????????????");
                mMovementAdapter.setItemType(MainMovementAdapter.InstitutionItem);
                mMovementAdapter.setInstitutionsBuy(event.getInstitutionBuy());
                mMovementAdapter.notifyDataSetChanged();
                mViewBinding.MainMovementTip.setText(event.getInstitutionBuy().getResultObj().get(0).getQuoteDate()+getString(R.string.institutions_buy));
                mViewBinding.MainMovementValueTag.setText(getString(R.string.increase_market_value));
                mViewBinding.MainMovementRatioTag.setText(getString(R.string.proportion_of_total_equity));
                return;
            }
            if (event.getItemType() == MainMovementAdapter.FinancingItem){
                //??????????????????
                Log.d(TAG, "?????????????????????-????????????");
                mMovementAdapter.setItemType(MainMovementAdapter.FinancingItem);
                mMovementAdapter.setFinancingBuy(event.getFinancingBuy());
                mMovementAdapter.notifyDataSetChanged();
                mViewBinding.MainMovementTip.setText(event.getFinancingBuy().getResultObj().get(0).getQuoteDate()+getString(R.string.financing_buy));
                mViewBinding.MainMovementValueTag.setText(getString(R.string.financing_balance));
                mViewBinding.MainMovementRatioTag.setText(getString(R.string.financing_balance_ration));
                return;
            }
        }else {
            Log.d(TAG, String.format("????????????ERROR= %s", event.getError()));
            mMovementAdapter.setItemType(event.getItemType());
            mMovementAdapter.notifyDataSetChanged();
            if (event.getItemType()==MainMovementAdapter.NorthItem || event.getItemType()==MainMovementAdapter.InstitutionItem) {
                mViewBinding.MainMovementValueTag.setText(getString(R.string.increase_market_value));
                mViewBinding.MainMovementRatioTag.setText(getString(R.string.proportion_of_total_equity));
                mViewBinding.MainMovementTip.setText("");
            }else {
                mViewBinding.MainMovementValueTag.setText(getString(R.string.financing_balance));
                mViewBinding.MainMovementRatioTag.setText(getString(R.string.financing_balance_ration));
                mViewBinding.MainMovementTip.setText("");
            }
        }
    }

    /*
    * ??????????????????
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnRiskRadarEvent(GuBbMainRiskRadarEvent event){
        if (event.isFlag()){
            Log.d(TAG, "?????????????????????");
            mRiskRadarAdapter.setList(event.getDataList());
            //??????????????????????????????
            mViewBinding.MainTLivingMore.setOnClickListener(this::onClick);
            mViewBinding.MainTLivingRight.setOnClickListener(this::onClick);
        }else {
            Log.d(TAG, String.format("???????????? Error= %s", event.getError()));
        }
    }

    /*
    * ????????????Item??????
    * */
    private void AddRiskRadarItemClicked(){
        mRiskRadarAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = SecretCodeTool.AnalysisSecretCode(getContext(), mRiskRadarAdapter.getData().get(position).getUrl());
                if (intent!=null){
                    startActivity(intent);
                }
            }
        });
    }

    /*
    * ????????????Item??????
    * */
    private void AddMovementItemClicked(){
        mViewBinding.MainMovementGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String url = null;
                if (mMovementAdapter.getItemType() == MainMovementAdapter.NorthItem && mMovementAdapter.getNorthBuy()!=null){
                    //????????????
                    url = mMovementAdapter.getNorthBuy().getResultObj().get(i).getUrl();
                }else if (mMovementAdapter.getItemType() == MainMovementAdapter.InstitutionItem && mMovementAdapter.getInstitutionsBuy()!=null){
                    url = mMovementAdapter.getInstitutionsBuy().getResultObj().get(i).getUrl();
                }else if (mMovementAdapter.getItemType() == MainMovementAdapter.FinancingItem && mMovementAdapter.getFinancingBuy()!=null){
                    url = mMovementAdapter.getFinancingBuy().getResultObj().get(i).getUrl();
                }
                Intent intent = SecretCodeTool.AnalysisSecretCode(getContext(), url);
                if (intent!=null){
                    startActivity(intent);
                }
            }
        });
    }

    /*
    * ?????????????????????Alter
    * */
    private void ShowTipAlter(){
        AlertDialog.Builder protocolTipBuilder = new AlertDialog.Builder(getContext(), R.style.ProtocolDialogThem);
        protocolTipBuilder.setCancelable(false);
        AlertDialog protocolTipDialog = protocolTipBuilder.create();
        ProtocolDialogLayoutBinding protocolTipBinding = ProtocolDialogLayoutBinding.bind(LayoutInflater.from(getContext()).inflate(R.layout.protocol_dialog_layout, null));
        protocolTipBinding.ProtocolTitle.setVisibility(View.VISIBLE);
        protocolTipBinding.ProtocolTitle.setText("?????????????????????");
        protocolTipBinding.ProtocolMsg.setGravity(Gravity.LEFT);
        protocolTipBinding.ProtocolMsg.setText(getString(R.string.ProtocolTipStart));
        //??????????????????
        SpannableString UserClickableString = new SpannableString(getString(R.string.UserProtocolTip));
        UserClickableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent UserProtocolWeb = new Intent(getContext(), WebActivity.class);
                UserProtocolWeb.putExtra(WebActivity.WebKey, ConnPath.UserXY);
                startActivity(UserProtocolWeb);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ContextCompat.getColor(getContext(), R.color.blue_0C318E));
            }
        }, 0, UserClickableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        protocolTipBinding.ProtocolMsg.append(UserClickableString);
        //????????????
        SpannableString PrivateClickableString = new SpannableString(getString(R.string.PrivateProtocolTip));
        PrivateClickableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent UserProtocolWeb = new Intent(getContext(), WebActivity.class);
                UserProtocolWeb.putExtra(WebActivity.WebKey, ConnPath.PrivateXY);
                startActivity(UserProtocolWeb);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ContextCompat.getColor(getContext(), R.color.blue_0C318E));
            }
        }, 1, PrivateClickableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        protocolTipBinding.ProtocolMsg.append(PrivateClickableString);
        protocolTipBinding.ProtocolMsg.append(getString(R.string.ProtocolTipEnd));
        //??????
        protocolTipBinding.ProtocolYes.setText(getString(R.string.ProtocolYes));
        protocolTipBinding.ProtocolYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (protocolTipDialog != null) {
                    SpTool.Instance(getContext()).SaveIsFirst(false);
                    protocolTipDialog.dismiss();
                    JCollectionAuth.setAuth(getContext(),true);
                    InitJGVerify();
                    InitJGPush();
                    //????????????
                    ShowGuide();
                }
            }
        });
        //?????????
        protocolTipBinding.ProtocolNo.setText(getString(R.string.ProtocolNo));
        protocolTipBinding.ProtocolNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (protocolTipDialog != null) {
                    protocolTipDialog.dismiss();
                    //????????????
                    ShowWarmAlter();
                }
            }
        });
        protocolTipBinding.ProtocolMsg.setHighlightColor(Color.TRANSPARENT);
        //????????????
        protocolTipBinding.ProtocolMsg.setMovementMethod(LinkMovementMethod.getInstance());
        //?????????????????????
        protocolTipDialog.show();
        WindowManager.LayoutParams layoutParams = protocolTipDialog.getWindow().getAttributes();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.CENTER;
        protocolTipDialog.getWindow().setAttributes(layoutParams);
        protocolTipDialog.setContentView(protocolTipBinding.getRoot());
    }

    /*
     * ??????????????????Alter
     * */
    private void ShowWarmAlter(){
        AlertDialog.Builder protocolWarmBuilder = new AlertDialog.Builder(getContext(), R.style.ProtocolDialogThem);
        protocolWarmBuilder.setCancelable(false);
        AlertDialog protocolWarmDialog = protocolWarmBuilder.create();
        ProtocolDialogLayoutBinding protocolWarmBinding = ProtocolDialogLayoutBinding.bind(LayoutInflater.from(getContext()).inflate(R.layout.protocol_dialog_layout, null));
        protocolWarmBinding.ProtocolTitle.setVisibility(View.VISIBLE);
        protocolWarmBinding.ProtocolTitle.setText("????????????");
        protocolWarmBinding.ProtocolMsg.setGravity(Gravity.LEFT);
        protocolWarmBinding.ProtocolMsg.setText(getString(R.string.ProtocolWarmStart));
        //??????????????????
        SpannableString UserClickableString = new SpannableString(getString(R.string.UserProtocolTip));
        UserClickableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent UserProtocolWeb = new Intent(getContext(), WebActivity.class);
                UserProtocolWeb.putExtra(WebActivity.WebKey, ConnPath.UserXY);
                startActivity(UserProtocolWeb);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ContextCompat.getColor(getContext(), R.color.blue_0C318E));
            }
        }, 0, UserClickableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        protocolWarmBinding.ProtocolMsg.append(UserClickableString);
        //????????????
        SpannableString PrivateClickableString = new SpannableString(getString(R.string.PrivateProtocolTip));
        PrivateClickableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent UserProtocolWeb = new Intent(getContext(), WebActivity.class);
                UserProtocolWeb.putExtra(WebActivity.WebKey, ConnPath.PrivateXY);
                startActivity(UserProtocolWeb);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ContextCompat.getColor(getContext(), R.color.blue_0C318E));
            }
        }, 1, PrivateClickableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        protocolWarmBinding.ProtocolMsg.append(PrivateClickableString);
        protocolWarmBinding.ProtocolMsg.append(getString(R.string.ProtocolTipEnd));
        //??????
        protocolWarmBinding.ProtocolYes.setText(getString(R.string.Agree));
        protocolWarmBinding.ProtocolYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (protocolWarmDialog != null) {
                    SpTool.Instance(getContext()).SaveIsFirst(false);
                    protocolWarmDialog.dismiss();
                    JCollectionAuth.setAuth(getContext(),true);
                    InitJGVerify();
                    InitJGPush();
                    //????????????
                    ShowGuide();
                }
            }
        });
        //?????????
        protocolWarmBinding.ProtocolNo.setText(getString(R.string.UnAgreeExit));
        protocolWarmBinding.ProtocolNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (protocolWarmDialog != null) {
                    protocolWarmDialog.dismiss();
                    //???????????????APP
                    getActivity().finish();
                }
            }
        });
        protocolWarmBinding.ProtocolMsg.setHighlightColor(Color.TRANSPARENT);
        //????????????
        protocolWarmBinding.ProtocolMsg.setMovementMethod(LinkMovementMethod.getInstance());
        //?????????????????????
        protocolWarmDialog.show();
        WindowManager.LayoutParams layoutParams = protocolWarmDialog.getWindow().getAttributes();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.CENTER;
        protocolWarmDialog.getWindow().setAttributes(layoutParams);
        protocolWarmDialog.setContentView(protocolWarmBinding.getRoot());
    }

    /*
    * ???????????????
    * */
    private void ShowGuide(){
        new CurtainFlow.Builder()
                .with(1001, ShowGuideInBaFengXian())
                .with(1002, ShowGuideInBaJiHui())
                .with(1003, ShowGuideInPanQian())
                .create()
                .start(new CurtainFlow.CallBack() {
                    @Override
                    public void onProcess(int currentId, CurtainFlowInterface curtainFlow) {
                        switch (currentId){
                            case 1001:
                                curtainFlow.findViewInCurrentCurtain(R.id.bafengxian_know)
                                        .setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                curtainFlow.push();
                                            }
                                        });
                                break;
                            case 1002:
                                ImageView JiHuiImg = curtainFlow.findViewInCurrentCurtain(R.id.jihui);
                                ConstraintLayout.LayoutParams JiHuiLayoutParams = (ConstraintLayout.LayoutParams) JiHuiImg.getLayoutParams();
                                int width = mViewBinding.MainIcons.getChildAt(0).getWidth()*2+DisplayTool.dp2px(getContext(),14);
                                int height = mViewBinding.MainIcons.getChildAt(0).getHeight()+mViewBinding.MainBanner.getHeight();
                                JiHuiLayoutParams.setMargins(0, height, width, 0);
                                JiHuiImg.setLayoutParams(JiHuiLayoutParams);
                                curtainFlow.findViewInCurrentCurtain(R.id.jihui_know)
                                        .setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                curtainFlow.push();
                                            }
                                        });
                                break;
                            case 1003:
                                TextView PanQianText = curtainFlow.findViewInCurrentCurtain(R.id.panqian_text);
                                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) PanQianText.getLayoutParams();
                                int PanQianHeight = mViewBinding.MainIcons.getHeight()+mViewBinding.MainBanner.getHeight();
                                layoutParams.setMargins(0, PanQianHeight, 0, 0);
                                PanQianText.setLayoutParams(layoutParams);
                                curtainFlow.findViewInCurrentCurtain(R.id.panqian_know)
                                        .setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                curtainFlow.finish();
                                            }
                                        });
                                break;
                        }
                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }


    /*
    * ??????????????????
    * */
    private Curtain ShowGuideInBaFengXian(){
        GridView view = mViewBinding.MainIcons;
        View bafengxian = ViewGetter.getFromAdapterView(view, 0);
        int width = mViewBinding.MainIcons.getChildAt(0).getWidth()+DisplayTool.dp2px(getContext(),14);
        int height = mViewBinding.MainIcons.getChildAt(0).getHeight()+mViewBinding.MainBanner.getHeight();
        if (bafengxian!=null) {
            return new Curtain(this)
                    .with(bafengxian)
                    .setCancelBackPressed(false)
                    .setTopView(R.layout.bafengxian_guide_layout)
                    .setCallBack(new Curtain.CallBack() {
                        @Override
                        public void onShow(IGuide curtain) {
                            ImageView img = curtain.findViewByIdInTopView(R.id.bafengxian);
                            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) img.getLayoutParams();
                            layoutParams.setMargins(width, height, 0, 0);
                            img.setLayoutParams(layoutParams);
                        }

                        @Override
                        public void onDismiss(IGuide iGuide) {
                        }
                    });
        }
        return null;
    }

    /*
    * ??????????????????
    * */
    private Curtain ShowGuideInBaJiHui(){
        GridView view = mViewBinding.MainIcons;
        View bazijin = ViewGetter.getFromAdapterView(view, 1);
        View bajihui = ViewGetter.getFromAdapterView(view, 2);
        View bafenxishi = ViewGetter.getFromAdapterView(view, 3);
        View bayouzi = ViewGetter.getFromAdapterView(view, 4);
        if (bazijin!=null && bajihui!=null && bafenxishi!=null && bayouzi!=null) {
            return new Curtain(this)
                    .with(bazijin)
                    .with(bajihui)
                    .with(bafenxishi)
                    .with(bayouzi)
                    .setCancelBackPressed(false)
                    .setTopView(R.layout.jihui_guide_layout)
                    .setCallBack(new Curtain.CallBack() {
                        @Override
                        public void onShow(IGuide curtain) {
                        }

                        @Override
                        public void onDismiss(IGuide iGuide) {

                        }
                    });
        }
        return null;
    }

    /*
     * ??????????????????
     * */
    private Curtain ShowGuideInPanQian(){
        GridView view = mViewBinding.MainSupGrid;
        View one = ViewGetter.getFromAdapterView(view, 0);
        View two = ViewGetter.getFromAdapterView(view, 1);
        View three = ViewGetter.getFromAdapterView(view, 2);
        View tag = mViewBinding.MainSupImg;
        if (one!=null && two!=null && three!=null && tag!=null) {
            return new Curtain(this)
                    .with(one)
                    .with(two)
                    .with(three)
                    .with(tag)
                    .setCancelBackPressed(false)
                    .setTopView(R.layout.panqiantegong_guide_layout)
                    .setCallBack(new Curtain.CallBack() {
                        @Override
                        public void onShow(IGuide curtain) {

                        }

                        @Override
                        public void onDismiss(IGuide iGuide) {

                        }
                    });
        }
        return null;
    }


    /*
     * ?????????????????????
     * */
    private void InitJGVerify(){
        JVerificationInterface.init(getContext(), 10000, new RequestCallback<String>() {
            @Override
            public void onResult(int i, String s) {
                if (i==8000){
                    Log.d(TAG, "???????????????????????????");
                    JGVerifyLogin.PreLogin(getContext());
                }else {
                    Log.d(TAG, String.format("???????????????????????????%d:%s", i, s));
                }
            }
        });
    }

    /*
     * ?????????????????????
     * */
    private void InitJGPush(){
        JPushInterface.init(getContext());
    }

}
