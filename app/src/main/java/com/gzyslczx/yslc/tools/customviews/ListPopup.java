package com.gzyslczx.yslc.tools.customviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.adapters.fundtong.FundTongTradeLevelListAdapter;
import com.gzyslczx.yslc.databinding.RecyclerListPopupLayoutBinding;

public class ListPopup {

    private PopupWindow popupWindow;
    private RecyclerListPopupLayoutBinding binding;

    public ListPopup(Context context, FundTongTradeLevelListAdapter listAdapter){
        if (popupWindow==null) {
            binding = RecyclerListPopupLayoutBinding.bind(LayoutInflater.from(context).inflate(R.layout.recycler_list_popup_layout, null));
            binding.PopupList.setLayoutManager(new LinearLayoutManager(context));
            binding.PopupList.setAdapter(listAdapter);
            popupWindow = new PopupWindow(binding.getRoot(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setFocusable(true);
        }
    }

    public void ShowListPopup(View parent){
        if (popupWindow!=null) {
            popupWindow.showAsDropDown(parent);
        }
    }

    public void DismissListPopup(){
        if (popupWindow!=null){
            popupWindow.dismiss();
        }
    }

}
