package com.gzyslczx.yslc.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.gzyslczx.yslc.fragments.BaseFragment;

import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private List<BaseFragment> mFragments;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public ViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (mFragments!=null && mFragments.size()>0){
            return mFragments.get(position);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        if (mFragments!=null){
            return mFragments.size();
        }
        return 0;
    }

    public void setmFragments(List<BaseFragment> mFragments) {
        this.mFragments = mFragments;
    }


}
