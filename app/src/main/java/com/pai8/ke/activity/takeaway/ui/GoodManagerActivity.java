package com.pai8.ke.activity.takeaway.ui;

import android.view.View;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.ViewPagerAdapter;
import com.pai8.ke.activity.takeaway.api.TakeawayApi;
import com.pai8.ke.activity.takeaway.fragment.GroupBuyManagerFragment;
import com.pai8.ke.activity.takeaway.fragment.TakeawayManagerFragment;
import com.pai8.ke.activity.takeaway.widget.SendPricePop;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.base.BasePresenter;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class GoodManagerActivity extends BaseMvpActivity implements View.OnClickListener,ViewPager.OnPageChangeListener {

    private ArrayList<Fragment> fragments;

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_good_manager;
    }

    @Override
    public void initView() {
        setImmersionBar(R.id.base_tool_bar);
        findViewById(R.id.toolbar_back_all).setOnClickListener(this);

        SlidingTabLayout mTabLayout = findViewById(R.id.tabLayout);
        ViewPager mViewPager = findViewById(R.id.vp_balance);
        mViewPager.addOnPageChangeListener((ViewPager.OnPageChangeListener) this);

        fragments = new ArrayList<>();
        fragments.add(new TakeawayManagerFragment());
        fragments.add(new GroupBuyManagerFragment());
        String[] mTitles = new String[]{"外卖", "团购"};
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments, mTitles));
        mTabLayout.setViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setCurrentItem(0);
        findViewById(R.id.iv_price_setting).setOnClickListener(v -> {
            SendPricePop pricePop = new SendPricePop(GoodManagerActivity.this);
            pricePop.setOnSelectListener((content, distance) -> {
                shopDealOrder(content, distance);
            });
            pricePop.showPopupWindow();
        });

    }

    public void shopDealOrder(String floor_send_cost, String send_range) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("shop_id", AccountManager.getInstance().getShopId());
        map.put("floor_send_cost", floor_send_cost);
        map.put("send_range", send_range);
        TakeawayApi.getInstance().floorSendCost(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String data) {
                        ToastUtils.showShort("配置成功");
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toolbar_back_all) {
            finish();
        }
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
        if (position==1){

            TextView textView = findViewById(R.id.iv_price_setting);
            textView.setVisibility(View.GONE);
        }
        else{
            TextView textView = findViewById(R.id.iv_price_setting);
            textView.setVisibility(View.VISIBLE);

        }
    }

    /**
     * Called when the scroll state changes. Useful for discovering when the user
     * begins dragging, when the pager is automatically settling to the current page,
     * or when it is fully stopped/idle.
     *
     * @param state The new scroll state.
     * @see ViewPager#SCROLL_STATE_IDLE
     * @see ViewPager#SCROLL_STATE_DRAGGING
     * @see ViewPager#SCROLL_STATE_SETTLING
     */
    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
