package com.pai8.ke.groupBuy.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lihongshi
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mTitleList = new ArrayList<>();

    public List<Fragment> getFragmentList() {
        return mFragmentList;
    }

    /**
     * 构造方法
     */
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public List<String> getTitleList() {
        return mTitleList;
    }

    public void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mTitleList.add(title);
    }

    public Fragment getFragment(int pos){
        return mFragmentList.get(pos);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.size() >= 1 ? mTitleList.get(position) : "";
    }
}