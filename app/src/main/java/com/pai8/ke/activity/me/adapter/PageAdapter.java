package com.pai8.ke.activity.me.adapter;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * @author Created by zzf
 * @time 2020/12/15 22:32
 * Description：
 */
public class PageAdapter extends FragmentPagerAdapter {
    private List<String> mTitle;
    private List<Fragment> mFragments;

    public PageAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        super(fm);
        this.mFragments = fragments;
        this.mTitle = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }


    @Override
    public int getCount() {
        return mFragments.size();
    }
}
