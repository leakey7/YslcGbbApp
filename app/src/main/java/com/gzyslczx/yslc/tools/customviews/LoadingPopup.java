package com.gzyslczx.yslc.tools.customviews;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.databinding.LoadingLayoutBinding;

public class LoadingPopup {

    private PopupWindow popupWindow;
    private LoadingLayoutBinding binding;
    public void ShowLoading(Context context, View parent, View.OnClickListener clickListener){
        if (popupWindow==null) {
            binding = LoadingLayoutBinding.bind(LayoutInflater.from(context).inflate(R.layout.loading_layout, null));
            binding.LoadingRetry.setOnClickListener(clickListener);
            popupWindow = new PopupWindow(binding.getRoot(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        binding.LoadingRetry.setVisibility(View.GONE);
        binding.LoadingView.setVisibility(View.VISIBLE);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }

    public void Dismiss(){
        if (popupWindow!=null){
            popupWindow.dismiss();
        }
    }

    public void Show(View parent){
        if (popupWindow!=null){
            popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        }
    }

    public void ShowRetry(){
        if (popupWindow!=null) {
            binding.LoadingRetry.setVisibility(View.VISIBLE);
            binding.LoadingView.setVisibility(View.GONE);
        }
    }


    public void DisRetry(){
        if (popupWindow!=null) {
            binding.LoadingView.setVisibility(View.VISIBLE);
            binding.LoadingRetry.setVisibility(View.GONE);
        }
    }

}
