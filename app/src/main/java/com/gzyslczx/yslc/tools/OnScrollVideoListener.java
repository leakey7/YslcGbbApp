package com.gzyslczx.yslc.tools;

import com.gzyslczx.yslc.databinding.ScrollSmallVideItemBinding;

public interface OnScrollVideoListener {

    /*
    * 初始化
    * */
    void onInitComplete(ScrollSmallVideItemBinding view);

    /*
    * 释放
    * */
    void onPageRelease(boolean isNext, int position, ScrollSmallVideItemBinding view);

    /*
    * 选中
    * */
    void onPageSelect(int position, boolean isBottom, ScrollSmallVideItemBinding view);

}
