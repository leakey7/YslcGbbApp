package com.gzyslczx.yslc.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.trello.rxlifecycle4.components.support.RxFragment;

public abstract class BaseFragment<T extends ViewBinding> extends RxFragment {

    public T mViewBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        InitParentLayout(inflater, container, savedInstanceState);
        return mViewBinding.getRoot();
    }

    //初始化父布局
    protected abstract void InitParentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);
    //初始化子控件
    protected abstract void InitView();

}
