package com.gzyslczx.yslc.adapters.fundtong;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.adapters.fundtong.bean.ConceptSelectData;


import java.util.List;

public class ConceptFundListAdapter extends BaseSectionQuickAdapter<ConceptSelectData, BaseViewHolder> {


    public ConceptFundListAdapter(int sectionHeadResId, @Nullable List<ConceptSelectData> data) {
        super(sectionHeadResId, data);
        setNormalLayout(R.layout.concept_select_left_word_item);
        addChildClickViewIds(R.id.ConceptItemWord);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, ConceptSelectData data) {
        if (data.isSelect()){
            baseViewHolder.setBackgroundResource(R.id.ConceptItemWord, R.drawable.concept_item_word_selected_shape);
            baseViewHolder.setTextColor(R.id.ConceptItemWord, ContextCompat.getColor(getContext(), R.color.white));
        }else {
            baseViewHolder.setBackgroundResource(R.id.ConceptItemWord, R.drawable.concept_item_word_unselect_shape);
            baseViewHolder.setTextColor(R.id.ConceptItemWord, ContextCompat.getColor(getContext(), R.color.black_333));
        }
        baseViewHolder.setText(R.id.ConceptItemWord, data.getFundName());
    }

    @Override
    protected void convertHeader(@NonNull BaseViewHolder baseViewHolder, @NonNull ConceptSelectData data) {
        baseViewHolder.setText(R.id.ConceptHead, data.getWord());
    }
}
