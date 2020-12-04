package com.pai8.ke.activity.takeaway.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.Constants;
import com.pai8.ke.activity.takeaway.adapter.ViewPagerAdapter;
import com.pai8.ke.activity.takeaway.contract.StoreContract;
import com.pai8.ke.activity.takeaway.entity.FoodGoodInfo;
import com.pai8.ke.activity.takeaway.entity.ShopFoodGoodInfo;
import com.pai8.ke.activity.takeaway.entity.event.AddGoodEvent;
import com.pai8.ke.activity.takeaway.entity.req.ShopIdReq;
import com.pai8.ke.activity.takeaway.entity.resq.ShopContent;
import com.pai8.ke.activity.takeaway.entity.resq.StoreInfo;
import com.pai8.ke.activity.takeaway.fragment.EvaluateFragment;
import com.pai8.ke.activity.takeaway.fragment.GoodFragment;
import com.pai8.ke.activity.takeaway.fragment.StoreFragment;
import com.pai8.ke.activity.takeaway.order.ConfirmOrderActivity;
import com.pai8.ke.activity.takeaway.presenter.StorePresenter;
import com.pai8.ke.activity.takeaway.utils.AddToCartUtil;
import com.pai8.ke.activity.takeaway.widget.ShopCarPop;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.fragment.CouponGetDialogFragment;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.utils.DensityUtils;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StoreActivity extends BaseMvpActivity<StorePresenter> implements View.OnClickListener,
        StoreContract.View {
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
    private TextView mTvShopNum;
    private TextView mTvPrice;
    private TextView mTvScore;
    private TextView mTvMonthSale;
    private TextView mTvDesc;
    private TextView mTvStoreDis;
    private TextView mTvTime;
    private TextView mTvlogisticsDiscounts;
    private ImageView ivStore;

    private int mStart = 0;
    private List<FoodGoodInfo> mGoodInfoList;  //购物车
    private ShopIdReq mShopIdReq;


    private String orderNo;


    public static void launch(Context context, String shopId) {
        if (StringUtils.isEmpty(shopId)) return;
        StoreInfo storeInfo = new StoreInfo();
        storeInfo.id = Integer.parseInt(shopId);
        Intent intent = new Intent(context, StoreActivity.class);
        intent.putExtra("storeInfo", storeInfo);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_store;
    }

    @Override
    public void initView() {
        ivStore = findViewById(R.id.iv_store);
        ImmersionBar.with(this)
                .transparentStatusBar()
                .statusBarDarkFont(true)
                .init();
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
        mTvShopNum = findViewById(R.id.tv_shop_num);
        mTvPrice = findViewById(R.id.tv_price);
        mTvMonthSale = findViewById(R.id.tv_month_sale);
        mTvDesc = findViewById(R.id.item_tv_desc);
        mTvStoreDis = findViewById(R.id.tv_store_km);
        mTvTime = findViewById(R.id.item_tv_time);
        mTvlogisticsDiscounts = findViewById(R.id.seller_goods_tv_logistics_discounts);

        mTvScore = findViewById(R.id.tv_store_score);
        mIvShopCar.setOnClickListener(this);
        findViewById(R.id.collapsing_toolbar).setBackgroundColor(Color.parseColor("#FFFFFF"));
        fragments = new ArrayList<>();
        fragments.add(new GoodFragment());
        fragments.add(new EvaluateFragment());
        fragments.add(new StoreFragment());
        String[] mTitles = new String[]{"商品", "评价", "商家"};
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments, mTitles));
        mTabLayout.setViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setCurrentItem(0);
        mTvOrder = findViewById(R.id.tv_order);
        mTvOrder.setOnClickListener(this);
        setSupportActionBar(toolbar);
        MAX_SCROLL = DensityUtils.dp2px(this, 250);

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AddGoodEvent event) {
        if (event.type == Constants.EVENT_TYPE_ADD_CAR) {
            if (event.xy0 != 0 && event.xy1 != 0) {
                mGoodInfoList = event.shopCarGoodsList;
                //添加的时候
                if (mStart == 0) {
                    mStart = DensityUtils.dip2px(11);
                }
                /* 起点 */
                int[] startXY = new int[2];
                int fx = event.xy0 + mStart;
                int fy = event.xy1 + mStart;
                startXY[0] = fx;
                startXY[1] = fy;
                final ImageView animImg = new ImageView(this);
                animImg.setImageResource(R.mipmap.icon_red_bg);
                ViewGroup anim_mask_layout = AddToCartUtil.createAnimLayout(this);
                anim_mask_layout.addView(animImg);
                final View v = AddToCartUtil.addViewToAnimLayout(this, anim_mask_layout, animImg, startXY,
                        true);
                if (v == null) {
                    return;
                }
                /* 终点 */
                int[] endXY = new int[2];
                mTvShopNum.getLocationInWindow(endXY);
                int tx = endXY[0];
                int ty = endXY[1];
                /* 终点 */
                int mx = (tx + fx) / 2;
                int my = (ty + fy) / 2;
                AddToCartUtil.startAnimation(v, 0, 0, fx, fy, mx, my, tx, ty,
                        new AddToCartUtil.AnimationListener() {
                            @Override
                            public void onAnimationEnd() {
                                //动画结束
                                if (event.number != 0) {
                                    mIvShopCar.setBackground(getResources().getDrawable(R.mipmap.ic_shop_car));
                                    mTvShopNum.setText(event.number + "");
                                    mTvShopNum.setVisibility(View.VISIBLE);
                                } else {
                                    mIvShopCar.setBackground(getResources().getDrawable(R.mipmap.ic_shop_car_gray));
                                    mTvShopNum.setVisibility(View.INVISIBLE);

                                }
                                mGoodInfoList = event.shopCarGoodsList;
                                setPrice(mGoodInfoList);
                            }
                        });
            }
        } else if (event.type == Constants.EVENT_TYPE_DELETE_CAR) {
            mGoodInfoList = event.shopCarGoodsList;
            if (event.number != 0) {
                mIvShopCar.setBackground(getResources().getDrawable(R.mipmap.ic_shop_car));
                mTvShopNum.setText(event.number + "");
                mTvShopNum.setVisibility(View.VISIBLE);
            } else {
                mIvShopCar.setBackground(getResources().getDrawable(R.mipmap.ic_shop_car_gray));
                mTvShopNum.setVisibility(View.INVISIBLE);

            }
            setPrice(mGoodInfoList);
        }

    }


    public void setPrice(List<FoodGoodInfo> mShopCarGoods) {
        int shopNum = 0;
        double toMoney = 0;
        boolean discount = false;
        double originalTotlMoney = 0;
        for (FoodGoodInfo pro : mShopCarGoods) {
            if (!TextUtils.isEmpty(pro.sell_price)) {
                toMoney += (Double.parseDouble(pro.sell_price) * pro.goods_num);
            }
            shopNum = shopNum + pro.goods_num;
        }
        mTvPrice.setText("￥" + toMoney);
        mTvShopNum.setText("" + shopNum);

        if (shopNum <= 0) {
            mIvShopCar.setBackground(getResources().getDrawable(R.mipmap.ic_shop_car_gray));
            mTvShopNum.setVisibility(View.INVISIBLE);
            mTvOrder.setEnabled(false);
            mTvOrder.setBackgroundResource(R.drawable.shape_orgin_gradient_gray);
        } else {
            mIvShopCar.setBackground(getResources().getDrawable(R.mipmap.ic_shop_car));
            mTvShopNum.setText(shopNum + "");
            mTvShopNum.setVisibility(View.VISIBLE);
            mTvOrder.setBackgroundResource(R.drawable.shape_orgin_gradient);
            mTvOrder.setEnabled(true);

        }

        //只要有优惠就跳出循环
    }


    @Override
    public void initData() {
        super.initData();

        EventBus.getDefault().register(this);
        orderNo  = getIntent().getStringExtra("orderNo");
        mStoreInfo = (StoreInfo) getIntent().getSerializableExtra("storeInfo");
        setData(mStoreInfo);
        mShopIdReq = new ShopIdReq();
        mShopIdReq.shop_id = mStoreInfo.id + "";
        mPresenter.shopContent(mShopIdReq);
        mPresenter.getCart(mStoreInfo.id + "");

        ShopIdReq shopIdReq = new ShopIdReq();
        shopIdReq.shop_id = mStoreInfo.id + "";
        mPresenter.shopContent(shopIdReq);
        mPresenter.getCart( mStoreInfo.id + "");
        //再来一旦
        if(!TextUtils.isEmpty(orderNo)){
            mPresenter.reAddCart(orderNo);
        }

    }

    public void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                //设置状态栏为透明，否则在部分手机上会呈现系统默认的浅灰色
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以考虑设置为透明色
                //window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }
    private void setData(StoreInfo mStoreInfo) {
        if (!TextUtils.isEmpty(mStoreInfo.shop_name)) {
            ImageLoadUtils.setCircularImage(this, mStoreInfo.shop_img, mIvStorePic, R.mipmap.ic_launcher);
            mTvStoreName.setText(mStoreInfo.shop_name);
            mTvScore.setText(mStoreInfo.score + "");
            mTvMonthSale.setText("月售 " + mStoreInfo.month_sale_count);
            mTvDesc.setText(mStoreInfo.shop_desc);
            mTvTime.setText(mStoreInfo.delivery_time);
            mTvlogisticsDiscounts.setText("另需配送费￥" + mStoreInfo.send_cost);
            mTvStoreDis.setText(mStoreInfo.distance);
            ImageLoadUtils.setRectImage(this, mStoreInfo.shop_img, ivStore);

        }

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back_all) {
            finish();
        } else if(v.getId() == R.id.iv_store_search){
            startActivity(new Intent(this,ShopGoodSearchActivity.class)
            .putExtra("shopId",mStoreInfo.id));
        }else if (v.getId() == R.id.iv_shop_car) {
        } else if (v.getId() == R.id.iv_store_search) {
            startActivity(new Intent(this, ShopGoodSearchActivity.class));
        } else if (v.getId() == R.id.iv_shop_car) {
            if (mGoodInfoList == null || mGoodInfoList.size() <= 0) {
                return;
            }
            ShopCarPop pop = new ShopCarPop(this, mTvShopNum.getText().toString(), mGoodInfoList);
            pop.setOnSelectListener(new ShopCarPop.OnSelectListener() {
                @Override
                public void onSelect() {
                    Intent intent = new Intent(StoreActivity.this, ConfirmOrderActivity.class);
                    intent.putExtra("shopCar", (Serializable) mGoodInfoList);
                    intent.putExtra("storeInfo", mStoreInfo);
                    startActivity(intent);
                }
            });
            pop.showPopupWindow();
        } else if (v.getId() == R.id.tv_order) {
            Intent intent = new Intent(this, ConfirmOrderActivity.class);
            intent.putExtra("shopCar", (Serializable) mGoodInfoList);
            intent.putExtra("storeInfo", mStoreInfo);
            startActivity(intent);
        } else if (v.getId() == R.id.iv_store_collection) {
            ShopIdReq addFoodReq = new ShopIdReq();
            addFoodReq.shop_id = AccountManager.getInstance().getShopId();
            if(mStoreInfo.is_collect == 1){
               mPresenter.unCollection(addFoodReq);
            }else{
                mPresenter.collection(addFoodReq);
            }

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public StorePresenter initPresenter() {
        return new StorePresenter(this);
    }

    @Override
    public void getShopContentSuccess(ShopContent data) {
        setData(data.shop_info);
        if (data.shop_info.is_collect == 0) {
            mIvCollection.setImageResource(R.mipmap.icon_rating_bar_normal);
        } else {
            mIvCollection.setImageResource(R.mipmap.icon_rating_bar_select);

        }

    }

    @Override
    public void collectionSuccess(String data) {
        mStoreInfo.is_collect = 1;
        mIvCollection.setImageResource(R.mipmap.icon_rating_bar_select);

    }

    @Override
    public void unCollectionSuccess(String data) {
        mStoreInfo.is_collect = 0;
        mIvCollection.setImageResource(R.mipmap.icon_rating_bar_normal);
    }

    @Override
    public void getCarSuccess(ShopFoodGoodInfo data) {
        mGoodInfoList = data.goods_info;
        setPrice(mGoodInfoList);
    }

    @OnClick(R.id.tv_coupon)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_coupon:
                CouponGetDialogFragment newInstance = CouponGetDialogFragment.newInstance(mShopIdReq.shop_id);
                newInstance.show(getSupportFragmentManager(), "CouponGetDialog");
                break;
        }
    }
}
