package com.pai8.ke.utils;

import android.content.Context;
import android.graphics.Color;


import com.pai8.ke.R;
import com.pai8.ke.widget.ScaleTransitionPagerTitleView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.WrapPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

/**
 * 菜单指示器创建工具类
 */
public class TabCreateUtils {
    /**
     * 类型：橘色指示器
     * 字：选中橘色，未选中黑色，加粗
     * 指示器：指示器长度和文字长度相同，橘色
     */
    public static void setHomeTab(Context context, MagicIndicator magicIndicator, ViewPager viewPager,
                                  List<String> tabNames) {
        CommonNavigator commonNavigator = new CommonNavigator(context);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return tabNames == null ? 0 : tabNames.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ScaleTransitionPagerTitleView scaleTransTitleView =
                        new ScaleTransitionPagerTitleView(context);
                scaleTransTitleView.setNormalColor(ContextCompat.getColor(context, R.color.color_mid_font));
                scaleTransTitleView.setSelectedColor(ContextCompat.getColor(context,
                        R.color.color_dark_font));
                scaleTransTitleView.setTextSize(20);
                scaleTransTitleView.setText(tabNames.get(index));
                scaleTransTitleView.setOnClickListener(view -> viewPager.setCurrentItem(index));
                return scaleTransTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setColors(ContextCompat.getColor(context, R.color.colorPrimary));
                //设置宽度
                indicator.setLineWidth(DensityUtils.dip2px(16));
                //设置高度
                indicator.setLineHeight(DensityUtils.dip2px(3));
                //设置圆角
                indicator.setRoundRadius(DensityUtils.dip2px(8));
                return indicator;
            }
        });
        commonNavigator.setAdjustMode(true);
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }

    public static void setHomeTypeTab(Context context, MagicIndicator magicIndicator, ViewPager viewPager,
                                      List<String> tabNames) {
        magicIndicator.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(context);
        commonNavigator.setScrollPivotX(0.2f);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return tabNames == null ? 0 : tabNames.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                simplePagerTitleView.setText(tabNames.get(index));
                simplePagerTitleView.setNormalColor(ResUtils.getColor(R.color.color_mid_font));
                simplePagerTitleView.setSelectedColor(ResUtils.getColor(R.color.colorPrimary));
                simplePagerTitleView.setTextSize(15);
                simplePagerTitleView.setOnClickListener(v -> viewPager.setCurrentItem(index));
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                WrapPagerIndicator indicator = new WrapPagerIndicator(context);
                indicator.setFillColor(ResUtils.getColor(R.color.colorPrimary));
                indicator.setVerticalPadding(10);
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }
}
