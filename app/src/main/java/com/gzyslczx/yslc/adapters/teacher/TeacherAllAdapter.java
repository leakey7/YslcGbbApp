package com.gzyslczx.yslc.adapters.teacher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.adapters.teacher.bean.TeacherAllData;
import com.gzyslczx.yslc.adapters.teacher.bean.TeacherSelfData;
import com.gzyslczx.yslc.databinding.ListPicItemBinding;
import com.gzyslczx.yslc.tools.DisplayTool;
import com.gzyslczx.yslc.tools.customviews.IGridView;

import java.util.List;

public class TeacherAllAdapter extends BaseMultiItemQuickAdapter<TeacherAllData, BaseViewHolder> {

    public TeacherAllAdapter(@Nullable List<TeacherAllData> data) {
        super(data);
        addItemType(TeacherAllData.NoneContent, R.layout.focus_teacher_item);
        addItemType(TeacherAllData.JustText, R.layout.teacher_all_just_text_item);
        addItemType(TeacherAllData.OnePic, R.layout.teacher_all_one_pic_item);
        addItemType(TeacherAllData.MorePic, R.layout.teacher_all_more_pic_item);
        addItemType(TeacherAllData.BVideo, R.layout.teacher_all_hvideo_item);
        addItemType(TeacherAllData.SVideo, R.layout.teacher_all_vvideo_item);
        addChildClickViewIds(R.id.tJTextHeadImg, R.id.tJTextFocus, R.id.tJTextPraiseImg, R.id.tJTextPraiseNum, R.id.tJTextShareImg, R.id.tJTextShare,
                R.id.tAPicHeadImg, R.id.tAPicFocus, R.id.tAPicPraiseImg, R.id.tAPicPraiseNum, R.id.tAPicShareImg, R.id.tAPicShare,
                R.id.tMPicHeadImg, R.id.tMPicFocus, R.id.tMPicPraiseImg, R.id.tMPicPraiseNum, R.id.tMPicShareImg, R.id.tMPicShare,
                R.id.tHVHeadImg, R.id.tHVFocus, R.id.tHVPraiseImg, R.id.tHVPraiseNum, R.id.tHVShareImg, R.id.tHVShare,
                R.id.tVVHeadImg, R.id.tVVFocus, R.id.tVVPraiseImg, R.id.tVVPraiseNum, R.id.tVVShareImg, R.id.tVVShare,
                R.id.FocusTHeadImg, R.id.FocusTState);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, TeacherAllData data) {
        switch (data.getItemType()){
            case TeacherAllData.JustText:
                Glide.with(getContext()).load(data.getObjData().getImg())
                        .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.head_img))
                        .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(DisplayTool.dp2px(getContext(), 44)))))
                        .into((ImageView) baseViewHolder.getView(R.id.tJTextHeadImg));
                baseViewHolder.setText(R.id.tJTextName, data.getObjData().getName());
                baseViewHolder.setText(R.id.tJTextIntro, data.getObjData().getAdvantage());
                baseViewHolder.setText(R.id.tJTextTitle, data.getObjData().getTitle());
                baseViewHolder.setText(R.id.tJTextDes, data.getObjData().getDesc());
                baseViewHolder.setText(R.id.tJTextCode, data.getObjData().getNumber());
                if (data.getObjData().isLike()) {
                    baseViewHolder.setTextColor(R.id.tJTextPraiseNum, ContextCompat.getColor(getContext(), R.color.main_red));
                    baseViewHolder.setText(R.id.tJTextPraiseNum, String.valueOf(data.getObjData().getLikeNum()));
                    Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.is_praise))
                            .fitCenter().into((ImageView) baseViewHolder.getView(R.id.tJTextPraiseImg));
                } else {
                    baseViewHolder.setTextColor(R.id.tJTextPraiseNum, ContextCompat.getColor(getContext(), R.color.gray_999));
                    baseViewHolder.setText(R.id.tJTextPraiseNum, getContext().getString(R.string.Praise));
                    Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.un_praise))
                            .fitCenter().into((ImageView) baseViewHolder.getView(R.id.tJTextPraiseImg));
                }
                if (data.getObjData().isFocus()) {
                    baseViewHolder.setTextColor(R.id.tJTextFocus, ContextCompat.getColor(getContext(), R.color.gray_666));
                    baseViewHolder.setText(R.id.tJTextFocus, getContext().getString(R.string.IsFocus));
                    baseViewHolder.setBackgroundResource(R.id.tJTextFocus, R.drawable.gray_focus_radius_10_shape);
                } else {
                    baseViewHolder.setTextColor(R.id.tJTextFocus, ContextCompat.getColor(getContext(), R.color.main_red));
                    baseViewHolder.setText(R.id.tJTextFocus, getContext().getString(R.string.AddFocus));
                    baseViewHolder.setBackgroundResource(R.id.tJTextFocus, R.drawable.red_border_focus_radius_10_shape);
                }
                break;
            case TeacherAllData.OnePic:
                Glide.with(getContext()).load(data.getObjData().getImg()).placeholder(ContextCompat.getDrawable(getContext(), R.drawable.head_img))
                        .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(DisplayTool.dp2px(getContext(), 44)))))
                        .into((ImageView) baseViewHolder.getView(R.id.tAPicHeadImg));
                baseViewHolder.setText(R.id.tAPicName, data.getObjData().getName());
                Glide.with(getContext()).load(data.getObjData().getArtImg()).placeholder(ContextCompat.getDrawable(getContext(), R.drawable.thump_shape))
                        .fitCenter().into((ImageView) baseViewHolder.getView(R.id.tAPicImg));
                baseViewHolder.setText(R.id.tAPicIntro, data.getObjData().getAdvantage());
                baseViewHolder.setText(R.id.tAPicTitle, data.getObjData().getTitle());
                baseViewHolder.setText(R.id.tAPicDes, data.getObjData().getDesc());
                baseViewHolder.setText(R.id.tAPicCode, data.getObjData().getNumber());
                if (data.getObjData().isLike()) {
                    baseViewHolder.setTextColor(R.id.tAPicPraiseNum, ContextCompat.getColor(getContext(), R.color.main_red));
                    baseViewHolder.setText(R.id.tAPicPraiseNum, String.valueOf(data.getObjData().getLikeNum()));
                    Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.is_praise))
                            .fitCenter().into((ImageView) baseViewHolder.getView(R.id.tAPicPraiseImg));
                } else {
                    baseViewHolder.setTextColor(R.id.tAPicPraiseNum, ContextCompat.getColor(getContext(), R.color.gray_999));
                    baseViewHolder.setText(R.id.tAPicPraiseNum, getContext().getString(R.string.Praise));
                    Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.un_praise))
                            .fitCenter().into((ImageView) baseViewHolder.getView(R.id.tAPicPraiseImg));
                }
                if (data.getObjData().isFocus()) {
                    baseViewHolder.setTextColor(R.id.tAPicFocus, ContextCompat.getColor(getContext(), R.color.gray_666));
                    baseViewHolder.setText(R.id.tAPicFocus, getContext().getString(R.string.IsFocus));
                    baseViewHolder.setBackgroundResource(R.id.tAPicFocus, R.drawable.gray_focus_radius_10_shape);
                } else {
                    baseViewHolder.setTextColor(R.id.tAPicFocus, ContextCompat.getColor(getContext(), R.color.main_red));
                    baseViewHolder.setText(R.id.tAPicFocus, getContext().getString(R.string.AddFocus));
                    baseViewHolder.setBackgroundResource(R.id.tAPicFocus, R.drawable.red_border_focus_radius_10_shape);
                }
                break;
            case TeacherAllData.MorePic:
                Glide.with(getContext()).load(data.getObjData().getImg()).placeholder(ContextCompat.getDrawable(getContext(), R.drawable.head_img))
                        .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(DisplayTool.dp2px(getContext(), 44)))))
                        .into((ImageView) baseViewHolder.getView(R.id.tMPicHeadImg));
                baseViewHolder.setText(R.id.tMPicName, data.getObjData().getName());
                IGridView iGridView = baseViewHolder.getView(R.id.tMPicImgGrid);
                iGridView.setAdapter(new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return data.getObjData().getArrImg().length;
                    }

                    @Override
                    public Object getItem(int i) {
                        return data.getObjData().getArrImg()[i];
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
                        Glide.with(getContext()).load(data.getObjData().getArrImg()[i])
                                .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.thump_shape))
                                .fitCenter().into(binding.GImg);
                        return view;
                    }
                });
                baseViewHolder.setText(R.id.tMPicIntro, data.getObjData().getAdvantage());
                baseViewHolder.setText(R.id.tMPicTitle, data.getObjData().getTitle());
                baseViewHolder.setText(R.id.tMPicDes, data.getObjData().getDesc());
                baseViewHolder.setText(R.id.tMPicCode, data.getObjData().getNumber());
                if (data.getObjData().isLike()) {
                    baseViewHolder.setTextColor(R.id.tMPicPraiseNum, ContextCompat.getColor(getContext(), R.color.main_red));
                    baseViewHolder.setText(R.id.tMPicPraiseNum, String.valueOf(data.getObjData().getLikeNum()));
                    Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.is_praise))
                            .fitCenter().into((ImageView) baseViewHolder.getView(R.id.tMPicPraiseImg));
                } else {
                    baseViewHolder.setTextColor(R.id.tMPicPraiseNum, ContextCompat.getColor(getContext(), R.color.gray_999));
                    baseViewHolder.setText(R.id.tMPicPraiseNum, getContext().getString(R.string.Praise));
                    Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.un_praise))
                            .fitCenter().into((ImageView) baseViewHolder.getView(R.id.tMPicPraiseImg));
                }
                if (data.getObjData().isFocus()) {
                    baseViewHolder.setTextColor(R.id.tMPicFocus, ContextCompat.getColor(getContext(), R.color.gray_666));
                    baseViewHolder.setText(R.id.tMPicFocus, getContext().getString(R.string.IsFocus));
                    baseViewHolder.setBackgroundResource(R.id.tMPicFocus, R.drawable.gray_focus_radius_10_shape);
                } else {
                    baseViewHolder.setTextColor(R.id.tMPicFocus, ContextCompat.getColor(getContext(), R.color.main_red));
                    baseViewHolder.setText(R.id.tMPicFocus, getContext().getString(R.string.AddFocus));
                    baseViewHolder.setBackgroundResource(R.id.tMPicFocus, R.drawable.red_border_focus_radius_10_shape);
                }
                break;
            case TeacherAllData.BVideo:
                Glide.with(getContext()).load(data.getObjData().getImg()).placeholder(ContextCompat.getDrawable(getContext(), R.drawable.head_img))
                        .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(DisplayTool.dp2px(getContext(), 44)))))
                        .into((ImageView) baseViewHolder.getView(R.id.tHVHeadImg));
                baseViewHolder.setText(R.id.tHVName, data.getObjData().getName());
                Glide.with(getContext()).load(data.getObjData().getArtImg()).placeholder(ContextCompat.getDrawable(getContext(), R.drawable.thump_shape))
                        .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(DisplayTool.dp2px(getContext(), 4)))))
                        .into((ImageView) baseViewHolder.getView(R.id.tHVImg));
                baseViewHolder.setText(R.id.tHVIntro, data.getObjData().getAdvantage());
                baseViewHolder.setText(R.id.tHVTitle, data.getObjData().getTitle());
                baseViewHolder.setText(R.id.tHVCode, data.getObjData().getNumber());
                if (data.getObjData().isLike()) {
                    baseViewHolder.setTextColor(R.id.tHVPraiseNum, ContextCompat.getColor(getContext(), R.color.main_red));
                    baseViewHolder.setText(R.id.tHVPraiseNum, String.valueOf(data.getObjData().getLikeNum()));
                    Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.is_praise))
                            .fitCenter().into((ImageView) baseViewHolder.getView(R.id.tHVPraiseImg));
                } else {
                    baseViewHolder.setTextColor(R.id.tHVPraiseNum, ContextCompat.getColor(getContext(), R.color.gray_999));
                    baseViewHolder.setText(R.id.tHVPraiseNum, getContext().getString(R.string.Praise));
                    Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.un_praise))
                            .fitCenter().into((ImageView) baseViewHolder.getView(R.id.tHVPraiseImg));
                }
                if (data.getObjData().isFocus()) {
                    baseViewHolder.setTextColor(R.id.tHVFocus, ContextCompat.getColor(getContext(), R.color.gray_666));
                    baseViewHolder.setText(R.id.tHVFocus, getContext().getString(R.string.IsFocus));
                    baseViewHolder.setBackgroundResource(R.id.tHVFocus, R.drawable.gray_focus_radius_10_shape);
                } else {
                    baseViewHolder.setTextColor(R.id.tHVFocus, ContextCompat.getColor(getContext(), R.color.main_red));
                    baseViewHolder.setText(R.id.tHVFocus, getContext().getString(R.string.AddFocus));
                    baseViewHolder.setBackgroundResource(R.id.tHVFocus, R.drawable.red_border_focus_radius_10_shape);
                }
                break;
            case TeacherAllData.SVideo:
                Glide.with(getContext()).load(data.getObjData().getImg()).placeholder(ContextCompat.getDrawable(getContext(), R.drawable.head_img))
                        .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(DisplayTool.dp2px(getContext(), 44)))))
                        .into((ImageView) baseViewHolder.getView(R.id.tVVHeadImg));
                baseViewHolder.setText(R.id.tVVName, data.getObjData().getName());
                Glide.with(getContext()).load(data.getObjData().getArtImg()).placeholder(ContextCompat.getDrawable(getContext(), R.drawable.thump_shape))
                        .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(DisplayTool.dp2px(getContext(), 6)))))
                        .into((ImageView) baseViewHolder.getView(R.id.tVVImg));
                baseViewHolder.setText(R.id.tVVIntro, data.getObjData().getAdvantage());
                baseViewHolder.setText(R.id.tVVTitle, data.getObjData().getTitle());
                baseViewHolder.setText(R.id.tVVCode, data.getObjData().getNumber());
                if (data.getObjData().isLike()) {
                    baseViewHolder.setTextColor(R.id.tVVPraiseNum, ContextCompat.getColor(getContext(), R.color.main_red));
                    baseViewHolder.setText(R.id.tVVPraiseNum, String.valueOf(data.getObjData().getLikeNum()));
                    Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.is_praise))
                            .fitCenter().into((ImageView) baseViewHolder.getView(R.id.tVVPraiseImg));
                } else {
                    baseViewHolder.setTextColor(R.id.tVVPraiseNum, ContextCompat.getColor(getContext(), R.color.gray_999));
                    baseViewHolder.setText(R.id.tVVPraiseNum, getContext().getString(R.string.Praise));
                    Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.un_praise))
                            .fitCenter().into((ImageView) baseViewHolder.getView(R.id.tVVPraiseImg));
                }
                if (data.getObjData().isFocus()) {
                    baseViewHolder.setTextColor(R.id.tVVFocus, ContextCompat.getColor(getContext(), R.color.gray_666));
                    baseViewHolder.setText(R.id.tVVFocus, getContext().getString(R.string.IsFocus));
                    baseViewHolder.setBackgroundResource(R.id.tVVFocus, R.drawable.gray_focus_radius_10_shape);
                } else {
                    baseViewHolder.setTextColor(R.id.tVVFocus, ContextCompat.getColor(getContext(), R.color.main_red));
                    baseViewHolder.setText(R.id.tVVFocus,  getContext().getString(R.string.AddFocus));
                    baseViewHolder.setBackgroundResource(R.id.tVVFocus, R.drawable.red_border_focus_radius_10_shape);
                }
                break;
                case TeacherAllData.NoneContent:
                    Glide.with(getContext()).load(data.getObjData().getImg())
                            .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.head_img))
                            .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(DisplayTool.dp2px(getContext(), 44)))))
                            .into((ImageView) baseViewHolder.getView(R.id.FocusTHeadImg));
                    baseViewHolder.setText(R.id.FocusTName, data.getObjData().getName());
                    TextView advantage = baseViewHolder.getView(R.id.FocusTDes);
                    advantage.setMaxLines(Integer.MAX_VALUE);
                    advantage.setText(data.getObjData().getAdvantage());
                    baseViewHolder.setText(R.id.FocusTCode, data.getObjData().getNumber());
                    if (data.getObjData().isFocus()){
                        baseViewHolder.setText(R.id.FocusTState, getContext().getString(R.string.IsFocus));
                        baseViewHolder.setTextColor(R.id.FocusTState, ContextCompat.getColor(getContext(), R.color.gray_666));
                        baseViewHolder.setBackgroundResource(R.id.FocusTState, R.drawable.gray_focus_radius_10_shape);
                    }else {
                        baseViewHolder.setText(R.id.FocusTState, getContext().getString(R.string.AddFocus));
                        baseViewHolder.setTextColor(R.id.FocusTState, ContextCompat.getColor(getContext(), R.color.main_red));
                        baseViewHolder.setBackgroundResource(R.id.FocusTState, R.drawable.red_border_focus_radius_10_shape);
                    }
                    break;
        }
    }
}
