package com.gzyslczx.yslc.adapters.main;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.adapters.main.bean.MainRecoData;
import com.gzyslczx.yslc.databinding.ListPicItemBinding;
import com.gzyslczx.yslc.tools.DisplayTool;
import com.gzyslczx.yslc.tools.customviews.IGridView;

import java.util.List;

public class MainRecoAdapter extends BaseMultiItemQuickAdapter<MainRecoData, BaseViewHolder> implements LoadMoreModule {

    public MainRecoAdapter(@Nullable List<MainRecoData> data) {
        super(data);
        addItemType(MainRecoData.JustText, R.layout.main_reco_just_text_item);
        addItemType(MainRecoData.OnePic, R.layout.main_reco_one_pic_item);
        addItemType(MainRecoData.MorePic, R.layout.main_reco_pic_item);
        addItemType(MainRecoData.BVideo, R.layout.main_reco_bvideo_item);
        addItemType(MainRecoData.SVideo, R.layout.main_reco_svideo_item);
        addChildClickViewIds(R.id.JTextHeadImg, R.id.APicHeadImg, R.id.MPicHeadImg, R.id.HVHeadImg, R.id.VVHeadImg,
                R.id.JTextFocus, R.id.APicFocus, R.id.MPicFocus, R.id.HVFocus, R.id.VVFocus,
                R.id.JTextPraiseImg, R.id.APicPraiseImg, R.id.MPicPraiseImg, R.id.HVPraiseImg, R.id.VVPraiseImg,
                R.id.JTextPraiseNum, R.id.APicPraiseNum, R.id.MPicPraiseNum, R.id.HVPraiseNum, R.id.VVPraiseNum,
                R.id.JTextShareImg, R.id.APicShareImg, R.id.MPicShareImg, R.id.HVShareImg, R.id.VVShareImg,
                R.id.JTextShare, R.id.APicShare, R.id.MPicShare, R.id.HVShare, R.id.VVShare);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, MainRecoData data) {
        switch (data.getItemType()){
            case MainRecoData.JustText:
                //纯文案类
                if (data.isTeacherItem()){
                    //名师类
                    Glide.with(getContext()).load(data.getInfo().getAuthorImg())
                            .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.head_img))
                            .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(),
                                    new RoundedCorners(DisplayTool.dp2px(getContext(), 44)))))
                            .into((ImageView) baseViewHolder.getView(R.id.JTextHeadImg));
                    baseViewHolder.setText(R.id.JTextName, data.getInfo().getAuthor());
                }else {
                    //栏目类
                    Glide.with(getContext()).load(data.getInfo().getLabelHeadImg())
                            .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.head_img))
                            .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(),
                                    new RoundedCorners(DisplayTool.dp2px(getContext(), 44)))))
                            .into((ImageView) baseViewHolder.getView(R.id.JTextHeadImg));
                    baseViewHolder.setText(R.id.JTextName, data.getInfo().getNewLabel());
                }
                baseViewHolder.setText(R.id.JTextTime, data.getInfo().getDateInfo());
                baseViewHolder.setText(R.id.JTextTitle, data.getInfo().getTitle());
                baseViewHolder.setText(R.id.JTextDes, data.getInfo().getDescribe());
                if (data.getInfo().isFocus()){
                    //已关注
                    baseViewHolder.setBackgroundResource(R.id.JTextFocus, R.drawable.gray_focus_radius_10_shape);
                    baseViewHolder.setTextColor(R.id.JTextFocus, ContextCompat.getColor(getContext(), R.color.gray_666));
                    baseViewHolder.setText(R.id.JTextFocus, getContext().getString(R.string.IsFocus));
                }else {
                    //未关注
                    baseViewHolder.setBackgroundResource(R.id.JTextFocus, R.drawable.red_border_focus_radius_10_shape);
                    baseViewHolder.setTextColor(R.id.JTextFocus, ContextCompat.getColor(getContext(), R.color.main_red));
                    baseViewHolder.setText(R.id.JTextFocus, getContext().getString(R.string.AddFocus));
                }
                if (data.getInfo().isLike()){
                    //已点赞
                    baseViewHolder.setTextColor(R.id.JTextPraiseNum, ContextCompat.getColor(getContext(), R.color.main_red));
                    baseViewHolder.setText(R.id.JTextPraiseNum, String.valueOf(data.getInfo().getPraise()));
                    Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.is_praise))
                            .fitCenter().into((ImageView) baseViewHolder.getView(R.id.JTextPraiseImg));
                }else {
                    //未点赞
                    baseViewHolder.setTextColor(R.id.JTextPraiseNum, ContextCompat.getColor(getContext(), R.color.gray_999));
                    baseViewHolder.setText(R.id.JTextPraiseNum, getContext().getString(R.string.Praise));
                    Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.un_praise))
                            .fitCenter().into((ImageView) baseViewHolder.getView(R.id.JTextPraiseImg));
                }
                break;
            case MainRecoData.OnePic:
                //单图文类
                if (data.isTeacherItem()){
                    //名师类
                    Glide.with(getContext()).load(data.getInfo().getAuthorImg())
                            .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.head_img))
                            .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(),
                                    new RoundedCorners(DisplayTool.dp2px(getContext(), 44)))))
                            .into((ImageView) baseViewHolder.getView(R.id.APicHeadImg));
                    baseViewHolder.setText(R.id.APicName, data.getInfo().getAuthor());
                }else {
                    //栏目类
                    Glide.with(getContext()).load(data.getInfo().getLabelHeadImg())
                            .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.head_img))
                            .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(),
                                    new RoundedCorners(DisplayTool.dp2px(getContext(), 44)))))
                            .into((ImageView) baseViewHolder.getView(R.id.APicHeadImg));
                    baseViewHolder.setText(R.id.APicName, data.getInfo().getNewLabel());
                }
                Glide.with(getContext()).load(data.getInfo().getArrImg()[0])
                        .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.thump_shape))
                        .apply(new RequestOptions().transform(new MultiTransformation<>(new FitCenter(),
                                new RoundedCorners(DisplayTool.dp2px(getContext(), 4)))))
                        .into((ImageView) baseViewHolder.getView(R.id.APicImg));
                baseViewHolder.setText(R.id.APicTime, data.getInfo().getDateInfo());
                baseViewHolder.setText(R.id.APicTitle, data.getInfo().getTitle());
                baseViewHolder.setText(R.id.APicDes, data.getInfo().getDescribe());
                if (data.getInfo().isFocus()){
                    //已关注
                    baseViewHolder.setBackgroundResource(R.id.APicFocus, R.drawable.gray_focus_radius_10_shape);
                    baseViewHolder.setTextColor(R.id.APicFocus, ContextCompat.getColor(getContext(), R.color.gray_666));
                    baseViewHolder.setText(R.id.APicFocus, getContext().getString(R.string.IsFocus));
                }else {
                    //未关注
                    baseViewHolder.setBackgroundResource(R.id.APicFocus, R.drawable.red_border_focus_radius_10_shape);
                    baseViewHolder.setTextColor(R.id.APicFocus, ContextCompat.getColor(getContext(), R.color.main_red));
                    baseViewHolder.setText(R.id.APicFocus, getContext().getString(R.string.AddFocus));
                }
                if (data.getInfo().isLike()){
                    //已点赞
                    baseViewHolder.setTextColor(R.id.APicPraiseNum, ContextCompat.getColor(getContext(), R.color.main_red));
                    baseViewHolder.setText(R.id.APicPraiseNum, String.valueOf(data.getInfo().getPraise()));
                    Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.is_praise))
                            .fitCenter().into((ImageView) baseViewHolder.getView(R.id.APicPraiseImg));
                }else {
                    //未点赞
                    baseViewHolder.setTextColor(R.id.APicPraiseNum, ContextCompat.getColor(getContext(), R.color.gray_999));
                    baseViewHolder.setText(R.id.APicPraiseNum, getContext().getString(R.string.Praise));
                    Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.un_praise))
                            .fitCenter().into((ImageView) baseViewHolder.getView(R.id.APicPraiseImg));
                }
                break;
            case MainRecoData.MorePic:
                //多图文类
                if (data.isTeacherItem()){
                    //名师类
                    Glide.with(getContext()).load(data.getInfo().getAuthorImg())
                            .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.head_img))
                            .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(),
                                    new RoundedCorners(DisplayTool.dp2px(getContext(), 44)))))
                            .into((ImageView) baseViewHolder.getView(R.id.MPicHeadImg));
                    baseViewHolder.setText(R.id.MPicName, data.getInfo().getAuthor());
                }else {
                    //栏目类
                    Glide.with(getContext()).load(data.getInfo().getLabelHeadImg())
                            .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.head_img))
                            .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(),
                                    new RoundedCorners(DisplayTool.dp2px(getContext(), 44)))))
                            .into((ImageView) baseViewHolder.getView(R.id.MPicHeadImg));
                    baseViewHolder.setText(R.id.MPicName, data.getInfo().getNewLabel());
                }
                baseViewHolder.setText(R.id.MPicTime, data.getInfo().getDateInfo());
                baseViewHolder.setText(R.id.MPicTitle, data.getInfo().getTitle());
                baseViewHolder.setText(R.id.MPicDes, data.getInfo().getDescribe());
                //设置多图显示
                IGridView iGridView = baseViewHolder.getView(R.id.MPicImgGrid);
                iGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
                iGridView.setAdapter(new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return data.getInfo().getArrImg().length;
                    }

                    @Override
                    public Object getItem(int i) {
                        return data.getInfo().getArrImg()[i];
                    }

                    @Override
                    public long getItemId(int i) {
                        return i;
                    }

                    @Override
                    public View getView(int i, View view, ViewGroup viewGroup) {
                        ListPicItemBinding binding;
                        if (view == null) {
                            binding = ListPicItemBinding.inflate(LayoutInflater.from(getContext()));
                            view = binding.getRoot();
                            view.setTag(binding);
                        } else {
                            binding = (ListPicItemBinding) view.getTag();
                        }
                        Glide.with(getContext()).load(data.getInfo().getArrImg()[i])
                                .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.thump_shape))
                                .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(),
                                        new RoundedCorners(DisplayTool.dp2px(getContext(), 4)))))
                                .into(binding.GImg);
                        return view;
                    }
                });
                if (data.getInfo().isFocus()){
                    //已关注
                    baseViewHolder.setBackgroundResource(R.id.MPicFocus, R.drawable.gray_focus_radius_10_shape);
                    baseViewHolder.setTextColor(R.id.MPicFocus, ContextCompat.getColor(getContext(), R.color.gray_666));
                    baseViewHolder.setText(R.id.MPicFocus, getContext().getString(R.string.IsFocus));
                }else {
                    //未关注
                    baseViewHolder.setBackgroundResource(R.id.MPicFocus, R.drawable.red_border_focus_radius_10_shape);
                    baseViewHolder.setTextColor(R.id.MPicFocus, ContextCompat.getColor(getContext(), R.color.main_red));
                    baseViewHolder.setText(R.id.MPicFocus, getContext().getString(R.string.AddFocus));
                }
                if (data.getInfo().isLike()){
                    //已点赞
                    baseViewHolder.setTextColor(R.id.MPicPraiseNum, ContextCompat.getColor(getContext(), R.color.main_red));
                    baseViewHolder.setText(R.id.MPicPraiseNum, String.valueOf(data.getInfo().getPraise()));
                    Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.is_praise))
                            .fitCenter().into((ImageView) baseViewHolder.getView(R.id.MPicPraiseImg));
                }else {
                    //未点赞
                    baseViewHolder.setTextColor(R.id.MPicPraiseNum, ContextCompat.getColor(getContext(), R.color.gray_999));
                    baseViewHolder.setText(R.id.MPicPraiseNum, getContext().getString(R.string.Praise));
                    Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.un_praise))
                            .fitCenter().into((ImageView) baseViewHolder.getView(R.id.MPicPraiseImg));
                }
                break;
            case MainRecoData.BVideo:
                //宽屏视频类
                if (data.isTeacherItem()){
                    //名师类
                    Glide.with(getContext()).load(data.getInfo().getAuthorImg())
                            .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.head_img))
                            .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(),
                                    new RoundedCorners(DisplayTool.dp2px(getContext(), 44)))))
                            .into((ImageView) baseViewHolder.getView(R.id.HVHeadImg));
                    baseViewHolder.setText(R.id.HVName, data.getInfo().getAuthor());
                }else {
                    //栏目类
                    Glide.with(getContext()).load(data.getInfo().getLabelHeadImg())
                            .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.head_img))
                            .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(),
                                    new RoundedCorners(DisplayTool.dp2px(getContext(), 44)))))
                            .into((ImageView) baseViewHolder.getView(R.id.HVHeadImg));
                    baseViewHolder.setText(R.id.HVName, data.getInfo().getNewLabel());
                }
                Glide.with(getContext()).load(data.getInfo().getImg()).centerCrop()
                        .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.thump_shape))
                        .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(),
                                new RoundedCorners(DisplayTool.dp2px(getContext(), 6)))))
                        .into((ImageView) baseViewHolder.getView(R.id.HVImg));
                baseViewHolder.setText(R.id.HVTime, data.getInfo().getDateInfo());
                baseViewHolder.setText(R.id.HVTitle, data.getInfo().getTitle());
                if (data.getInfo().isFocus()){
                    //已关注
                    baseViewHolder.setBackgroundResource(R.id.HVFocus, R.drawable.gray_focus_radius_10_shape);
                    baseViewHolder.setTextColor(R.id.HVFocus, ContextCompat.getColor(getContext(), R.color.gray_666));
                    baseViewHolder.setText(R.id.HVFocus, getContext().getString(R.string.IsFocus));
                }else {
                    //未关注
                    baseViewHolder.setBackgroundResource(R.id.HVFocus, R.drawable.red_border_focus_radius_10_shape);
                    baseViewHolder.setTextColor(R.id.HVFocus, ContextCompat.getColor(getContext(), R.color.main_red));
                    baseViewHolder.setText(R.id.HVFocus, getContext().getString(R.string.AddFocus));
                }
                if (data.getInfo().isLike()){
                    //已点赞
                    baseViewHolder.setTextColor(R.id.HVPraiseNum, ContextCompat.getColor(getContext(), R.color.main_red));
                    baseViewHolder.setText(R.id.HVPraiseNum, String.valueOf(data.getInfo().getPraise()));
                    Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.is_praise))
                            .fitCenter().into((ImageView) baseViewHolder.getView(R.id.HVPraiseImg));
                }else {
                    //未点赞
                    baseViewHolder.setTextColor(R.id.HVPraiseNum, ContextCompat.getColor(getContext(), R.color.gray_999));
                    baseViewHolder.setText(R.id.HVPraiseNum, getContext().getString(R.string.Praise));
                    Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.un_praise))
                            .fitCenter().into((ImageView) baseViewHolder.getView(R.id.HVPraiseImg));
                }
                break;
            case MainRecoData.SVideo:
                //竖屏视频类
                if (data.isTeacherItem()){
                    //名师类
                    Glide.with(getContext()).load(data.getInfo().getAuthorImg())
                            .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.head_img))
                            .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(),
                                    new RoundedCorners(DisplayTool.dp2px(getContext(), 44)))))
                            .into((ImageView) baseViewHolder.getView(R.id.VVHeadImg));
                    baseViewHolder.setText(R.id.VVName, data.getInfo().getAuthor());
                }else {
                    //栏目类
                    Glide.with(getContext()).load(data.getInfo().getLabelHeadImg())
                            .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.head_img))
                            .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(),
                                    new RoundedCorners(DisplayTool.dp2px(getContext(), 44)))))
                            .into((ImageView) baseViewHolder.getView(R.id.VVHeadImg));
                    baseViewHolder.setText(R.id.VVName, data.getInfo().getNewLabel());
                }
                Glide.with(getContext()).load(data.getInfo().getImg()).centerCrop()
                        .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.thump_shape))
                        .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(),
                                new RoundedCorners(DisplayTool.dp2px(getContext(), 6)))))
                        .into((ImageView) baseViewHolder.getView(R.id.VVImg));
                baseViewHolder.setText(R.id.VVTime, data.getInfo().getDateInfo());
                baseViewHolder.setText(R.id.VVTitle, data.getInfo().getTitle());
                if (data.getInfo().isFocus()){
                    //已关注
                    baseViewHolder.setBackgroundResource(R.id.VVFocus, R.drawable.gray_focus_radius_10_shape);
                    baseViewHolder.setTextColor(R.id.VVFocus, ContextCompat.getColor(getContext(), R.color.gray_666));
                    baseViewHolder.setText(R.id.VVFocus, getContext().getString(R.string.IsFocus));
                }else {
                    //未关注
                    baseViewHolder.setBackgroundResource(R.id.VVFocus, R.drawable.red_border_focus_radius_10_shape);
                    baseViewHolder.setTextColor(R.id.VVFocus, ContextCompat.getColor(getContext(), R.color.main_red));
                    baseViewHolder.setText(R.id.VVFocus, getContext().getString(R.string.AddFocus));
                }
                if (data.getInfo().isLike()){
                    //已点赞
                    baseViewHolder.setTextColor(R.id.VVPraiseNum, ContextCompat.getColor(getContext(), R.color.main_red));
                    baseViewHolder.setText(R.id.VVPraiseNum, String.valueOf(data.getInfo().getPraise()));
                    Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.is_praise))
                            .fitCenter().into((ImageView) baseViewHolder.getView(R.id.VVPraiseImg));
                }else {
                    //未点赞
                    baseViewHolder.setTextColor(R.id.VVPraiseNum, ContextCompat.getColor(getContext(), R.color.gray_999));
                    baseViewHolder.setText(R.id.VVPraiseNum, getContext().getString(R.string.Praise));
                    Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.un_praise))
                            .fitCenter().into((ImageView) baseViewHolder.getView(R.id.VVPraiseImg));
                }
                break;
            default:
                break;
        }
    }
}
