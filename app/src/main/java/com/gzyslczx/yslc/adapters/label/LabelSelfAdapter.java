package com.gzyslczx.yslc.adapters.label;

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
import com.gzyslczx.yslc.adapters.label.bean.LabelSelfData;
import com.gzyslczx.yslc.databinding.ListPicItemBinding;
import com.gzyslczx.yslc.tools.DisplayTool;
import com.gzyslczx.yslc.tools.customviews.IGridView;

import java.util.List;

public class LabelSelfAdapter extends BaseMultiItemQuickAdapter<LabelSelfData, BaseViewHolder> implements LoadMoreModule {

    public LabelSelfAdapter(@Nullable List<LabelSelfData> data) {
        super(data);
        addItemType(LabelSelfData.JustText, R.layout.label_self_list_just_text_item);
        addItemType(LabelSelfData.OnePic, R.layout.label_self_list_one_pic_item);
        addItemType(LabelSelfData.PicText, R.layout.label_self_list_pic_item);
        addItemType(LabelSelfData.BVideo, R.layout.label_self_list_bvideo_item);
        addItemType(LabelSelfData.SVideo, R.layout.label_self_list_svideo_item);
        addChildClickViewIds(R.id.LSelfJTextPraiseImg, R.id.LSelfAPicPraiseImg, R.id.LSelfMPicPraiseImg, R.id.LSelfHVPraiseImg, R.id.LSelfVVPraiseImg,
                R.id.LSelfJTextPraiseNum, R.id.LSelfAPicPraiseNum, R.id.LSelfMPicPraiseNum, R.id.LSelfHVPraiseNum, R.id.LSelfVVPraiseNum,
                R.id.LSelfJTextShareImg, R.id.LSelfAPicShareImg, R.id.LSelfMPicShareImg, R.id.LSelfHVShareImg, R.id.LSelfVVShareImg,
                R.id.LSelfJTextShare, R.id.LSelfAPicShare, R.id.LSelfMPicShare, R.id.LSelfHVShare, R.id.LSelfVVShare);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, LabelSelfData data) {
        switch (data.getItemType()){
            case LabelSelfData.JustText:
                //纯文案类
                Glide.with(getContext()).load(data.getInfo().getLabelHeadImg())
                        .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.head_img))
                        .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(),
                                new RoundedCorners(DisplayTool.dp2px(getContext(), 44)))))
                        .into((ImageView) baseViewHolder.getView(R.id.LSelfJTextHeadImg));
                baseViewHolder.setText(R.id.LSelfJTextName, data.getInfo().getNewLabel());
                baseViewHolder.setText(R.id.LSelfJTextTime, data.getInfo().getDateInfo());
                baseViewHolder.setText(R.id.LSelfJTextTitle, data.getInfo().getTitle());
                baseViewHolder.setText(R.id.LSelfJTextDes, data.getInfo().getDescribe());
                if (data.getInfo().isLike()){
                    //已点赞
                    baseViewHolder.setTextColor(R.id.LSelfJTextPraiseNum, ContextCompat.getColor(getContext(), R.color.main_red));
                    baseViewHolder.setText(R.id.LSelfJTextPraiseNum, String.valueOf(data.getInfo().getPraise()));
                    Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.is_praise))
                            .fitCenter().into((ImageView) baseViewHolder.getView(R.id.LSelfJTextPraiseImg));
                }else {
                    //未点赞
                    baseViewHolder.setTextColor(R.id.LSelfJTextPraiseNum, ContextCompat.getColor(getContext(), R.color.gray_999));
                    baseViewHolder.setText(R.id.LSelfJTextPraiseNum, getContext().getString(R.string.Praise));
                    Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.un_praise))
                            .fitCenter().into((ImageView) baseViewHolder.getView(R.id.LSelfJTextPraiseImg));
                }
                break;
            case LabelSelfData.OnePic:
                Glide.with(getContext()).load(data.getInfo().getLabelHeadImg())
                        .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.head_img))
                        .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(),
                                new RoundedCorners(DisplayTool.dp2px(getContext(), 44)))))
                        .into((ImageView) baseViewHolder.getView(R.id.LSelfAPicHeadImg));
                baseViewHolder.setText(R.id.LSelfAPicName, data.getInfo().getNewLabel());
                Glide.with(getContext()).load(data.getInfo().getArrImg()[0])
                        .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.thump_shape))
                        .apply(new RequestOptions().transform(new MultiTransformation<>(new FitCenter(),
                                new RoundedCorners(DisplayTool.dp2px(getContext(), 4)))))
                        .into((ImageView) baseViewHolder.getView(R.id.LSelfAPicImg));
                baseViewHolder.setText(R.id.LSelfAPicTime, data.getInfo().getDateInfo());
                baseViewHolder.setText(R.id.LSelfAPicTitle, data.getInfo().getTitle());
                baseViewHolder.setText(R.id.LSelfAPicDes, data.getInfo().getDescribe());
                if (data.getInfo().isLike()){
                    //已点赞
                    baseViewHolder.setTextColor(R.id.LSelfAPicPraiseNum, ContextCompat.getColor(getContext(), R.color.main_red));
                    baseViewHolder.setText(R.id.LSelfAPicPraiseNum, String.valueOf(data.getInfo().getPraise()));
                    Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.is_praise))
                            .fitCenter().into((ImageView) baseViewHolder.getView(R.id.LSelfAPicPraiseImg));
                }else {
                    //未点赞
                    baseViewHolder.setTextColor(R.id.LSelfAPicPraiseNum, ContextCompat.getColor(getContext(), R.color.gray_999));
                    baseViewHolder.setText(R.id.LSelfAPicPraiseNum, getContext().getString(R.string.Praise));
                    Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.un_praise))
                            .fitCenter().into((ImageView) baseViewHolder.getView(R.id.LSelfAPicPraiseImg));
                }
                break;
            case LabelSelfData.PicText:
                //图文类
                Glide.with(getContext()).load(data.getInfo().getLabelHeadImg())
                        .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.head_img))
                        .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(),
                                new RoundedCorners(DisplayTool.dp2px(getContext(), 44)))))
                        .into((ImageView) baseViewHolder.getView(R.id.LSelfMPicHeadImg));
                baseViewHolder.setText(R.id.LSelfMPicName, data.getInfo().getNewLabel());
                baseViewHolder.setText(R.id.LSelfMPicTime, data.getInfo().getDateInfo());
                baseViewHolder.setText(R.id.LSelfMPicTitle, data.getInfo().getTitle());
                baseViewHolder.setText(R.id.LSelfMPicDes, data.getInfo().getDescribe());
                //设置多图显示
                IGridView iGridView = baseViewHolder.getView(R.id.LSelfMPicImgGrid);
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
                if (data.getInfo().isLike()){
                    //已点赞
                    baseViewHolder.setTextColor(R.id.LSelfMPicPraiseNum, ContextCompat.getColor(getContext(), R.color.main_red));
                    baseViewHolder.setText(R.id.LSelfMPicPraiseNum, String.valueOf(data.getInfo().getPraise()));
                    Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.is_praise))
                            .fitCenter().into((ImageView) baseViewHolder.getView(R.id.LSelfMPicPraiseImg));
                }else {
                    //未点赞
                    baseViewHolder.setTextColor(R.id.LSelfMPicPraiseNum, ContextCompat.getColor(getContext(), R.color.gray_999));
                    baseViewHolder.setText(R.id.LSelfMPicPraiseNum, getContext().getString(R.string.Praise));
                    Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.un_praise))
                            .fitCenter().into((ImageView) baseViewHolder.getView(R.id.LSelfMPicPraiseImg));
                }
                break;
            case LabelSelfData.BVideo:
                //宽屏视频类
                Glide.with(getContext()).load(data.getInfo().getLabelHeadImg())
                        .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.head_img))
                        .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(),
                                new RoundedCorners(DisplayTool.dp2px(getContext(), 44)))))
                        .into((ImageView) baseViewHolder.getView(R.id.LSelfHVHeadImg));
                baseViewHolder.setText(R.id.LSelfHVName, data.getInfo().getNewLabel());
                Glide.with(getContext()).load(data.getInfo().getImg()).centerCrop()
                        .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.thump_shape))
                        .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(),
                                new RoundedCorners(DisplayTool.dp2px(getContext(), 6)))))
                        .into((ImageView) baseViewHolder.getView(R.id.LSelfHVImg));
                baseViewHolder.setText(R.id.LSelfHVTime, data.getInfo().getDateInfo());
                baseViewHolder.setText(R.id.LSelfHVTitle, data.getInfo().getTitle());
                if (data.getInfo().isLike()){
                    //已点赞
                    baseViewHolder.setTextColor(R.id.LSelfHVPraiseNum, ContextCompat.getColor(getContext(), R.color.main_red));
                    baseViewHolder.setText(R.id.LSelfHVPraiseNum, String.valueOf(data.getInfo().getPraise()));
                    Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.is_praise))
                            .fitCenter().into((ImageView) baseViewHolder.getView(R.id.LSelfHVPraiseImg));
                }else {
                    //未点赞
                    baseViewHolder.setTextColor(R.id.LSelfHVPraiseNum, ContextCompat.getColor(getContext(), R.color.gray_999));
                    baseViewHolder.setText(R.id.LSelfHVPraiseNum, getContext().getString(R.string.Praise));
                    Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.un_praise))
                            .fitCenter().into((ImageView) baseViewHolder.getView(R.id.LSelfHVPraiseImg));
                }
                break;
            case LabelSelfData.SVideo:
                //竖屏视频类
                Glide.with(getContext()).load(data.getInfo().getLabelHeadImg())
                        .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.head_img))
                        .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(),
                                new RoundedCorners(DisplayTool.dp2px(getContext(), 44)))))
                        .into((ImageView) baseViewHolder.getView(R.id.LSelfVVHeadImg));
                baseViewHolder.setText(R.id.LSelfVVName, data.getInfo().getNewLabel());
                Glide.with(getContext()).load(data.getInfo().getImg()).centerCrop()
                        .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.thump_shape))
                        .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(),
                                new RoundedCorners(DisplayTool.dp2px(getContext(), 6)))))
                        .into((ImageView) baseViewHolder.getView(R.id.LSelfVVImg));
                baseViewHolder.setText(R.id.LSelfVVTime, data.getInfo().getDateInfo());
                baseViewHolder.setText(R.id.LSelfVVTitle, data.getInfo().getTitle());
                if (data.getInfo().isLike()){
                    //已点赞
                    baseViewHolder.setTextColor(R.id.LSelfVVPraiseNum, ContextCompat.getColor(getContext(), R.color.main_red));
                    baseViewHolder.setText(R.id.LSelfVVPraiseNum, String.valueOf(data.getInfo().getPraise()));
                    Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.is_praise))
                            .fitCenter().into((ImageView) baseViewHolder.getView(R.id.LSelfVVPraiseImg));
                }else {
                    //未点赞
                    baseViewHolder.setTextColor(R.id.LSelfVVPraiseNum, ContextCompat.getColor(getContext(), R.color.gray_999));
                    baseViewHolder.setText(R.id.LSelfVVPraiseNum, getContext().getString(R.string.Praise));
                    Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.un_praise))
                            .fitCenter().into((ImageView) baseViewHolder.getView(R.id.LSelfVVPraiseImg));
                }
                break;
        }
    }
}
