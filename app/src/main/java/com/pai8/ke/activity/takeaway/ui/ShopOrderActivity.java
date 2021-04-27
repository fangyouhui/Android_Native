package com.pai8.ke.activity.takeaway.ui;

import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.lhs.library.base.BaseBottomDialogFragment;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.ViewPagerAdapter;
import com.pai8.ke.activity.takeaway.entity.req.OrderStatusInfo;
import com.pai8.ke.activity.takeaway.fragment.ShopGroupOrderListFragment;
import com.pai8.ke.activity.takeaway.fragment.ShopTakeawayOrderListFragment;
import com.pai8.ke.activity.takeaway.presenter.ShopOrderPresenter;
import com.pai8.ke.base.BaseMvpActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 商家订单处理
 */
public class ShopOrderActivity extends BaseMvpActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ArrayList<Fragment> fragments;

    private ShopTakeawayOrderListFragment waimaifragment = new ShopTakeawayOrderListFragment();
    private ShopGroupOrderListFragment shopGroupfragment = new ShopGroupOrderListFragment();

    @Override
    public ShopOrderPresenter initPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_processing;
    }

    @Override
    public void initView() {
        setImmersionBar(R.id.base_tool_bar);
        findViewById(R.id.base_tool_bar).setOnClickListener(this);
        findViewById(R.id.toolbar_iv_menu).setOnClickListener(this);
        SlidingTabLayout mTabLayout = findViewById(R.id.tabLayout);
        ViewPager mViewPager = findViewById(R.id.vp_balance);

        mViewPager.addOnPageChangeListener((ViewPager.OnPageChangeListener) this);
        fragments = new ArrayList<>();
        fragments.add(waimaifragment);
        fragments.add(shopGroupfragment);
        String[] mTitles = new String[]{"外卖", "团购"};
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments, mTitles));
        mTabLayout.setViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setCurrentItem(0);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.base_tool_bar) {
            finish();
        } else if (v.getId() == R.id.toolbar_iv_menu) {
            showBottomDialog();
        }
    }

    private void showBottomDialog() {
        OrderFilterBottomDialogFragment bottomDialogFragment = new OrderFilterBottomDialogFragment();
        bottomDialogFragment.setListener(new BaseBottomDialogFragment.OnDialogListener() {
            @Override
            public void onConfirmClickListener(@NotNull Object data) {
                List<OrderStatusInfo> list = (List<OrderStatusInfo>) data;
                StringBuilder stringBuilder = new StringBuilder();
                for (OrderStatusInfo orderStatusInfo : list) {
                    stringBuilder.append(orderStatusInfo.status).append("/");
                }
                stringBuilder.deleteCharAt(stringBuilder.lastIndexOf("/"));
                waimaifragment.filter(stringBuilder.toString());

            }
        });
        bottomDialogFragment.showNow(getSupportFragmentManager(), "filter");
    }


    /**
     * This method will be invoked when the current page is scrolled, either as part
     * of a programmatically initiated smooth scroll or a user initiated touch scroll.
     *
     * @param position             Position index of the first page currently being displayed.
     *                             Page position+1 will be visible if positionOffset is nonzero.
     * @param positionOffset       Value from [0, 1) indicating the offset from the page at position.
     * @param positionOffsetPixels Value in pixels indicating the offset from position.
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    /**
     * This method will be invoked when a new page becomes selected. Animation is not
     * necessarily complete.
     *
     * @param position Position index of the new selected page.
     */
    @Override
    public void onPageSelected(int position) {
        TextView textView = findViewById(R.id.toolbar_iv_menu);
        if (position == 1) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
