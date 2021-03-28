package com.pai8.ke.fragment.shop;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.pai8.ke.R;
import com.pai8.ke.adapter.TabAdapter;
import com.pai8.ke.base.BaseFragment;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.utils.TabCreateUtils;

import net.lucode.hackware.magicindicator.MagicIndicator;

import java.util.ArrayList;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

public class TabShopFragment extends BaseFragment {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.cv_banner)
    CardView cvBanner;
    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.iv_banner)
    ImageView ivBanner;

    private List<Fragment> mFragments;
    private List<String> mTitles;
    private TabAdapter mTabAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab_shop;
    }

    @Override
    protected void initView(Bundle arguments) {
        mTitles = new ArrayList<>();
        mFragments = new ArrayList<>();

        mTitles.add("全部");
        mTitles.add("美食");
        mTitles.add("娱乐");
        mTitles.add("教育");
        mTitles.add("购物");
        mTitles.add("建材");
        mTitles.add("酒店");

        for (int i = 0; i < mTitles.size(); i++) {
            mFragments.add(TabShopChildFragment.newInstance(i));
        }

        mTabAdapter = new TabAdapter(getChildFragmentManager(), mFragments, mTitles);

        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(mTabAdapter);
        viewPager.setCurrentItem(0);
        TabCreateUtils.setShopTab(getActivity(), magicIndicator, viewPager, mTitles);

        ImageLoadUtils.loadImage(getActivity(), "https://timgsa.baidu" +
                ".com/timg?image&quality=80&size=b9999_10000&sec=1606673026873&di" +
                "=55d468d616150eb70fdf593f7e826610&imgtype=0&src=http%3A%2F%2Fimage.suning" +
                ".cn%2Fuimg%2Fsop%2Fcommodity%2F194646643740991480165390_x.jpg", ivBanner, 0);
    }
}
