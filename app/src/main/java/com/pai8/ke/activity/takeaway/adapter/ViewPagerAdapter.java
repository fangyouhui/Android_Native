package com.pai8.ke.activity.takeaway.adapter;


import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragments;
    private String[] mTitles ;

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragments, String[]title) {
        super(fm);
        this.mFragments = fragments;
        this.mTitles=title;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments == null || mFragments.size() == 0 ? 0 : mFragments.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

}
