package com.pai8.ke.activity.me.ui;

import com.hjq.bar.OnTitleBarListener;
import com.luck.picture.lib.tools.ScreenUtils;
import com.pai8.ke.R;
import com.pai8.ke.activity.me.adapter.PageAdapter;
import com.pai8.ke.activity.me.fragment.PKAttentionFragment;
import com.pai8.ke.activity.me.fragment.ShopAttentionFragment;
import com.pai8.ke.activity.me.fragment.ShopHistoryFragment;
import com.pai8.ke.activity.me.fragment.VideoHistoryFragment;
import com.pai8.ke.base.BaseActivity;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Created by zzf
 * @time 11:21
 * Description： 关注
 */
public class AttentionMineActivity extends BaseActivity {

    @BindView(R.id.mi_attention)
    MagicIndicator miAttention;
    @BindView(R.id.vp_attention)
    ViewPager mViewPager;
    private List<String> mTitleDataList = Arrays.asList("店铺", "拍客");
    private List<Fragment> mFragments;

    @Override
    public int getLayoutId() {
        return R.layout.activity_attention_mine;
    }

    @Override
    public void initView() {
        mTitleBar.setTitle("关注");

    }

    @Override
    public void initListener() {
        super.initListener();
        mTitleBar.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(View v) {
                finish();
            }

            @Override
            public void onTitleClick(View v) {

            }

            @Override
            public void onRightClick(View v) {

            }
        });
    }

    @Override
    public void initData() {
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mTitleDataList == null ? 0 : mTitleDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(context);
                View customLayout = LayoutInflater.from(context).inflate(R.layout.layout_tab_head, null);
                TextView titleText = customLayout.findViewById(R.id.title_text);
                titleText.setText(mTitleDataList.get(index));
                commonPagerTitleView.setContentView(customLayout);
                commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {

                    @Override
                    public void onSelected(int index, int totalCount) {
                        titleText.setTextColor(getResources().getColor(R.color.colorPrimary));
                    }

                    @Override
                    public void onDeselected(int index, int totalCount) {
                        titleText.setTextColor(getResources().getColor(R.color.color_light_font));
                    }

                    @Override
                    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {

                    }

                    @Override
                    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {

                    }
                });

                commonPagerTitleView.setOnClickListener(v -> mViewPager.setCurrentItem(index));

                return commonPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(getResources().getColor(R.color.colorPrimary));
                indicator.setYOffset(10);
                indicator.setRoundRadius(5);
                indicator.setLineWidth(ScreenUtils.dip2px(AttentionMineActivity.this,20));
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                return indicator;
            }
        });
        miAttention.setNavigator(commonNavigator);
        ViewPagerHelper.bind(miAttention, mViewPager);
        initFragments();
    }

    private void initFragments() {
        mFragments = new ArrayList<>();
        mFragments.add(new ShopAttentionFragment());
        mFragments.add(new PKAttentionFragment());
        mViewPager.setOffscreenPageLimit(2);
        PageAdapter pagerAdapter = new PageAdapter(getSupportFragmentManager(),
                mFragments, mTitleDataList);
        mViewPager.setAdapter(pagerAdapter);
    }

}
