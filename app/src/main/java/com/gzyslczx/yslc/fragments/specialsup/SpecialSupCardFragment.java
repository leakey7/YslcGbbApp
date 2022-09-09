package com.gzyslczx.yslc.fragments.specialsup;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.SpecialSupDetailActivity;
import com.gzyslczx.yslc.databinding.SpecialSupCardFragmentBinding;
import com.gzyslczx.yslc.events.NoticeChangeSpecialSupCardEvent;
import com.gzyslczx.yslc.events.SpecialSupPraiseEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.modes.response.ResDGSCard;
import com.gzyslczx.yslc.presenter.SpecialSupPresenter;
import com.gzyslczx.yslc.tools.DateTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SpecialSupCardFragment extends BaseFragment<SpecialSupCardFragmentBinding> implements View.OnClickListener {

    private final String TAG = "SpecialSupFragment";
    public static final String SpecialSupKey = "CardCode";
    private int CardCode;
    private ResDGSCard data;
    private SpecialSupPresenter mPresenter;

    @Override
    protected void InitParentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = SpecialSupCardFragmentBinding.inflate(inflater, container, false);
        InitView();
    }

    @Override
    protected void InitView() {
        EventBus.getDefault().register(this);
        CardCode = getArguments().getInt(SpecialSupKey);
        //点击查看理由
        mViewBinding.SSupItemLook.setOnClickListener(this::onClick);
        mViewBinding.SSupItemPraiseNum.setOnClickListener(this::onClick);
        mViewBinding.SSupItemPraiseImg.setOnClickListener(this::onClick);
        mPresenter = new SpecialSupPresenter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    /*
     * 设置特供卡片
     * */
    private void SetUpCard(ResDGSCard obj){
        data = obj;
        mViewBinding.SSupItemYear.setText(obj.getAddDate().substring(0, obj.getAddDate().indexOf("-")));
        mViewBinding.SSupItemDate.setText(obj.getAddDate().substring(obj.getAddDate().indexOf("-")+1));
        mViewBinding.SSupItemText.setText(obj.getRemarks());
        mViewBinding.SSupItemCode.setText(obj.getStockCode());
        mViewBinding.SSupItemName.setText(obj.getStockName());
        mViewBinding.SSupItemWeek.setText(DateTool.GetWeek(obj.getAddDate()));
        if (obj.isLike()){
            Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.is_praise)).fitCenter().into(mViewBinding.SSupItemPraiseImg);
            mViewBinding.SSupItemPraiseNum.setTextColor(ContextCompat.getColor(getContext(), R.color.main_red));
            mViewBinding.SSupItemPraiseNum.setText(String.valueOf(obj.getLikeNum()));
        }else {
            Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.un_praise)).fitCenter().into(mViewBinding.SSupItemPraiseImg);
            mViewBinding.SSupItemPraiseNum.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_999));
            mViewBinding.SSupItemPraiseNum.setText(String.valueOf(data.getLikeNum()));
        }
    }

    /*
    * 接收通知更新卡片
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnNoticeChangeCardEvent(NoticeChangeSpecialSupCardEvent event){
        if (CardCode == event.getCardCode()){
            Log.d(TAG, "接收到通知更新卡片");
            SetUpCard(event.getDateCard());
        }
    }

    /*
    * 点击事件
    * */
    @Override
    public void onClick(View view) {
        if (view.getId()==mViewBinding.SSupItemLook.getId()) {
            //跳转盘前特供详情页面
            Intent intent = new Intent(getContext(), SpecialSupDetailActivity.class);
            intent.putExtra(SpecialSupDetailActivity.SpecialSupKey, data.getSId());
            intent.putExtra(SpecialSupDetailActivity.SpecialSupArtKey, data.getNsId());
            startActivity(intent);
            return;
        }
        if (view.getId()==mViewBinding.SSupItemPraiseNum.getId() || view.getId()==mViewBinding.SSupItemPraiseImg.getId()){
            //点赞
            if (data!=null) {
                mPresenter.RequestSpecialSupportPraise(getContext(), (BaseActivity) getActivity(), this, data.getSId(), data.getStockCode());
            }
        }
    }


    /*
     * 接收盘前特供点赞
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnSpecialSupPraiseEvent(SpecialSupPraiseEvent event){
        if (!TextUtils.isEmpty(event.getCode()) && event.isFlag() && event.getCode().equals(data.getStockCode())){
            Log.d(TAG, "接收到盘前特供点赞");
            if (event.isPraise()){
                Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.is_praise)).fitCenter().into(mViewBinding.SSupItemPraiseImg);
                mViewBinding.SSupItemPraiseNum.setTextColor(ActivityCompat.getColor(getContext(), R.color.main_red));
                mViewBinding.SSupItemPraiseNum.setText(String.valueOf(event.getPraiseNum()));
            }else {
                Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.un_praise)).fitCenter().into(mViewBinding.SSupItemPraiseImg);
                mViewBinding.SSupItemPraiseNum.setTextColor(ActivityCompat.getColor(getContext(), R.color.gray_999));
                mViewBinding.SSupItemPraiseNum.setText(String.valueOf(event.getPraiseNum()));
            }
        }
    }

}
