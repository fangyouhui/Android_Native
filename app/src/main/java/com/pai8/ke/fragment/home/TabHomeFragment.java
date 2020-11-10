package com.pai8.ke.fragment.home;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pai8.ke.R;
import com.pai8.ke.activity.video.VideoDetailActivity;
import com.pai8.ke.adapter.TabAdapter;
import com.pai8.ke.base.BaseFragment;
import com.pai8.ke.utils.TabCreateUtils;

import net.lucode.hackware.magicindicator.MagicIndicator;

import java.util.ArrayList;
import java.util.List;

import androidx.constraintlayout.widget.Guideline;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;

public class TabHomeFragment extends BaseFragment {

    @BindView(R.id.guide_line_top)
    Guideline guideLineTop;
    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.iv_liwu)
    ImageView ivLiwu;
    @BindView(R.id.iv_scan)
    ImageView ivScan;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private List<Fragment> mFragments;
    private List<String> mTitles;
    private TabAdapter mTabAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab_home;
    }

    @Override
    protected void initView(Bundle arguments) {
        mTitles = new ArrayList<>();
        mFragments = new ArrayList<>();

        mTitles.add("附近");
        mTitles.add("推荐");
        mTitles.add("关注");

        for (int i = 0; i < mTitles.size(); i++) {
            mFragments.add(TabHomeChildFragment.newInstance(i));
        }

        mTabAdapter = new TabAdapter(getFragmentManager(), mFragments, mTitles);

        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(mTabAdapter);

        TabCreateUtils.setHomeTab(getActivity(), magicIndicator, viewPager, mTitles);
    }

    @Override
    protected void onLazyLoad() {
    }

    @OnClick({R.id.tv_search, R.id.iv_liwu, R.id.iv_scan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_search:
                break;
            case R.id.iv_liwu:
                break;
            case R.id.iv_scan:
                break;
        }
    }
}
