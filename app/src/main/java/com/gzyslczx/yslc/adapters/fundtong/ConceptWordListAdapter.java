package com.gzyslczx.yslc.adapters.fundtong;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gzyslczx.yslc.R;

public class ConceptWordListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private StringBuilder stringBuilder = new StringBuilder();

    public ConceptWordListAdapter(int layoutResId) {
        super(layoutResId);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, String s) {
        if (s.equals(stringBuilder.toString())){
            baseViewHolder.setBackgroundResource(R.id.Word, R.drawable.oval_orange_shape);
            baseViewHolder.setTextColor(R.id.Word, ContextCompat.getColor(getContext(), R.color.white));
        }else {
            baseViewHolder.setBackgroundColor(R.id.Word, ContextCompat.getColor(getContext(), R.color.white));
            baseViewHolder.setTextColor(R.id.Word, ContextCompat.getColor(getContext(), R.color.black_333));
        }
        baseViewHolder.setText(R.id.Word, s);
    }

    public  void SetSelectWord(String word){
        stringBuilder.replace(0, stringBuilder.length(), word);
    }

}
