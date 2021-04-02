package com.pai8.ke.activity.takeaway.ui;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.tablayout.SlidingTabLayout;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.OrderStatusAdapter;
import com.pai8.ke.activity.takeaway.adapter.ViewPagerAdapter;
import com.pai8.ke.activity.takeaway.entity.req.OrderStatusInfo;
import com.pai8.ke.activity.takeaway.fragment.shopGroupOrderFragment;
import com.pai8.ke.activity.takeaway.fragment.shopWaiMainFragment;
import com.pai8.ke.activity.takeaway.presenter.ShopOrderPresenter;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.widget.BottomDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商家订单处理
 */

public class OrderProcessingActivity extends BaseMvpActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ArrayList<Fragment> fragments;

    private BottomDialog mOrderFilterDialog;
    private int page = 1;
    private String status = "";
    private Fragment waimaifragment = new shopWaiMainFragment();
    private Fragment shopGroupfragment = new shopGroupOrderFragment();

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
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setCurrentItem(0);


    }

    @Override
    public void initData() {
        super.initData();

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
        View view = View.inflate(this, R.layout.dialog_order_filter, null);
        ImageButton itnClose = view.findViewById(R.id.itn_close);
        TextView tvConfirm = view.findViewById(R.id.tv_next);
        RecyclerView rvOrderFilter = view.findViewById(R.id.rv_order_filter);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        rvOrderFilter.setLayoutManager(layoutManager);

        //0为待支付 1为已支付 2为商家已接单 7为订单制作完成 3为配送中 4为订单已完成 5为订单已申请退款 6订单被拒绝退款 8为订单已退款 9为订单已取消 -1为支付超时 -2订单拒绝接单
        List<OrderStatusInfo> statusInfos = new ArrayList<>();
        OrderStatusInfo statusInfo = new OrderStatusInfo();
        statusInfo.name = "待接单";
        statusInfo.status = "1";
        statusInfos.add(statusInfo);
        OrderStatusInfo statusInfo1 = new OrderStatusInfo();
        statusInfo1.name = "已接单";
        statusInfo1.status = "2";
        statusInfos.add(statusInfo1);
        OrderStatusInfo statusInfo2 = new OrderStatusInfo();
        statusInfo2.name = "待配送";
        statusInfo2.status = "7";
        statusInfos.add(statusInfo2);
        OrderStatusInfo statusInfo3 = new OrderStatusInfo();
        statusInfo3.name = "配送中";
        statusInfo3.status = "3";
        statusInfos.add(statusInfo3);
        OrderStatusInfo statusInfo4 = new OrderStatusInfo();
        statusInfo4.name = "已完成";
        statusInfo4.status = "4";
        statusInfos.add(statusInfo4);
        OrderStatusInfo statusInfo5 = new OrderStatusInfo();
        statusInfo5.name = "申请退款";
        statusInfo5.status = "5";
        statusInfos.add(statusInfo5);


        StringBuilder stringBuilder = new StringBuilder();

        OrderStatusAdapter adapter = new OrderStatusAdapter(statusInfos);
        rvOrderFilter.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter1, View view, int position) {
                adapter.choosePosition(position);
                stringBuilder.delete(0, stringBuilder.length());
                for (int i = 0; i < adapter.getData().size(); i++) {
                    if (adapter.getData().get(i).isSelect) {
                        stringBuilder.append(adapter.getData().get(i).status);
                        stringBuilder.append("/");
                    }
                }


            }
        });

        itnClose.setOnClickListener(view1 -> {
            mOrderFilterDialog.dismiss();
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 1;
//                mPresenter.orderList(stringBuilder.substring(0,stringBuilder.length()-1),page);
//waimaifragment.set
                //  waimaifragment = () getFragmentManager().findFragmentById(R.id.example_fragment);
                //waimaifragment.setRefresh(stringBuilder.substring(0,stringBuilder.length()-1),pag);
                //构建 Bundle
                //绑定 Fragment
                Map<String, String> map = new HashMap<>();

                map.put("name", stringBuilder.substring(0, stringBuilder.length() - 1));
                map.put("page", "1");

                EventBus.getDefault().post(map);

                mOrderFilterDialog.dismiss();
            }
        });
        if (mOrderFilterDialog == null) {
            mOrderFilterDialog = new BottomDialog(this, view);
        }
        mOrderFilterDialog.setIsCanceledOnTouchOutside(true);
        mOrderFilterDialog.show();
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
        if (position == 1) {

            TextView textView = findViewById(R.id.toolbar_iv_menu);
            textView.setVisibility(View.GONE);
        } else {
            TextView textView = findViewById(R.id.toolbar_iv_menu);
            textView.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
