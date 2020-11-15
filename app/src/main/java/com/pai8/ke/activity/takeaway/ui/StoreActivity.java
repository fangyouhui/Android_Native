package com.pai8.ke.activity.takeaway.ui;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.ViewPagerAdapter;
import com.pai8.ke.activity.takeaway.api.TakeawayApi;
import com.pai8.ke.activity.takeaway.entity.req.ShopIdReq;
import com.pai8.ke.activity.takeaway.entity.resq.StoreInfo;
import com.pai8.ke.activity.takeaway.fragment.StoreFragment;
import com.pai8.ke.activity.takeaway.order.ConfirmOrderActivity;
import com.pai8.ke.activity.takeaway.widget.ShopCarPop;
import com.pai8.ke.base.BaseActivity;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.utils.DensityUtils;
import com.pai8.ke.utils.ImageLoadUtils;

import java.util.ArrayList;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class StoreActivity extends BaseActivity implements View.OnClickListener {
    private ArrayList<Fragment> fragments;
    private AppBarLayout appbarlayout;
    private Toolbar toolbar;
    private ImageView mIvSearch;
    private int MAX_SCROLL;
    private TextView mTvTitle;
    private ImageView mIvBack, mIvCollection, mIvShare;
    private ImageView mIvShopCar;
    private TextView mTvOrder;
    private StoreInfo mStoreInfo;  //商家信息
    private ImageView mIvStorePic;
    private TextView mTvStoreName;

    @Override
    public int getLayoutId() {
        return R.layout.activity_store;
    }

    @Override
    public void initView() {
        setImmersionBar(R.id.iv_store);
        mIvBack = findViewById(R.id.back_all);
        mIvBack.setOnClickListener(this);
        toolbar = findViewById(R.id.toolbar);
        mTvTitle = findViewById(R.id.tv_title);
        SlidingTabLayout mTabLayout = findViewById(R.id.tabLayout);
        ViewPager mViewPager = findViewById(R.id.vp_balance);
        appbarlayout = findViewById(R.id.appbarlayout);
        mIvSearch = findViewById(R.id.iv_store_search);
        mIvSearch.setOnClickListener(this);
        mTvStoreName = findViewById(R.id.tv_store_name);
        mIvStorePic = findViewById(R.id.item_iv_pic);
        mIvCollection = findViewById(R.id.iv_store_collection);
        mIvCollection.setOnClickListener(this);
        mIvShare = findViewById(R.id.iv_store_share);
        mIvShare.setOnClickListener(this);
        mIvShopCar = findViewById(R.id.iv_shop_car);
        mIvShopCar.setOnClickListener(this);
        findViewById(R.id.collapsing_toolbar).setBackgroundColor(Color.parseColor("#FFFFFF"));
        fragments = new ArrayList<>();
        fragments.add(new StoreFragment());
        fragments.add(new StoreFragment());
        fragments.add(new StoreFragment());
        String[] mTitles = new String[]{"商品", "评价", "商家"};
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments, mTitles));
        mTabLayout.setViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setCurrentItem(0);
        mTvOrder = findViewById(R.id.tv_order);
        mTvOrder.setOnClickListener(this);
        setSupportActionBar(toolbar);
        MAX_SCROLL = DensityUtils.dp2px(this, 150);

        appbarlayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                int dy = Math.abs(i);
                toolbar.setSelected(dy > 10);
                float alpha = Math.min(MAX_SCROLL, dy) / (float) MAX_SCROLL;
                if (alpha > 0.5) {
                    mTvTitle.setText("外卖");
                }
                int backgroundAlpha = (int) (alpha * 255);
                int backgroundBlack = Color.argb(backgroundAlpha, 0, 0, 0);
                int backgroundColor = Color.argb(backgroundAlpha, 255, 255, 255);
                mTvTitle.setTextColor(backgroundBlack);
                mIvBack.setColorFilter(backgroundBlack);
                mIvSearch.setColorFilter(backgroundBlack);
                mIvCollection.setColorFilter(backgroundBlack);
                mIvShare.setColorFilter(backgroundBlack);
                toolbar.setBackgroundColor(backgroundColor);


            }
        });

    }


    @Override
    public void initData() {
        super.initData();

        mStoreInfo = (StoreInfo) getIntent().getSerializableExtra("storeInfo");
        ImageLoadUtils.setCircularImage(this,mStoreInfo.shop_img,mIvStorePic,R.mipmap.ic_launcher);
        mTvStoreName.setText(mStoreInfo.shop_name);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back_all) {
            finish();
        } else if (v.getId() == R.id.iv_shop_car) {
            ShopCarPop pop = new ShopCarPop(this);
            pop.showPopupWindow();
        } else if (v.getId() == R.id.tv_order) {

            startActivity(new Intent(this, ConfirmOrderActivity.class));
        } else if(v.getId() == R.id.iv_store_collection){
            ShopIdReq addFoodReq = new ShopIdReq();
            addFoodReq.shop_id = "6";
            TakeawayApi.getInstance().collection(addFoodReq)
                    .doOnSubscribe(disposable -> {
                    })
                    .compose(RxSchedulers.io_main())
                    .subscribe(new BaseObserver<String>() {
                        @Override
                        protected void onSuccess(String shopList) {


                        }

                        @Override
                        protected void onError(String msg, int errorCode) {
                            super.onError(msg, errorCode);
                        }
                    });


        }
    }
}
