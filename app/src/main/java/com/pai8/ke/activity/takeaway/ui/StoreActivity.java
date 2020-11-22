package com.pai8.ke.activity.takeaway.ui;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.Constants;
import com.pai8.ke.activity.takeaway.adapter.ViewPagerAdapter;
import com.pai8.ke.activity.takeaway.api.TakeawayApi;
import com.pai8.ke.activity.takeaway.contract.StoreContract;
import com.pai8.ke.activity.takeaway.entity.FoodGoodInfo;
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
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.utils.DensityUtils;
import com.pai8.ke.utils.ImageLoadUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class StoreActivity extends BaseMvpActivity<StorePresenter> implements View.OnClickListener, StoreContract.View {
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

    private int mStart = 0;
    private List<FoodGoodInfo> mGoodInfoList ;  //购物车

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
        mTvShopNum = findViewById(R.id.tv_shop_num);
        mTvPrice = findViewById(R.id.tv_price);
        mTvMonthSale = findViewById(R.id.tv_month_sale);
        mTvDesc = findViewById(R.id.item_tv_desc);
        mTvStoreDis = findViewById(R.id.tv_store_km);
        mTvTime = findViewById(R.id.item_tv_time);

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
                final View v = AddToCartUtil.addViewToAnimLayout(this, anim_mask_layout, animImg, startXY, true);
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
                AddToCartUtil.startAnimation(v, 0, 0, fx, fy, mx, my, tx, ty, new AddToCartUtil.AnimationListener() {
                    @Override
                    public void onAnimationEnd() {
                        //动画结束
                        if (event.number != 0) {
                            mIvShopCar.setBackground(getResources().getDrawable(R.mipmap.ic_shop_car));
                            mTvShopNum.setText(event.number+"");
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
        }else if(event.type == Constants.EVENT_TYPE_DELETE_CAR){
            mGoodInfoList = event.shopCarGoodsList;
            if (event.number != 0) {
                mIvShopCar.setBackground(getResources().getDrawable(R.mipmap.ic_shop_car));
                mTvShopNum.setText(event.number+"");
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
            if(!TextUtils.isEmpty(pro.sell_price)){
                toMoney += (Double.parseDouble(pro.sell_price)*pro.num) ;
            }
            shopNum = shopNum + pro.num;
        }
        mTvPrice.setText("￥" + toMoney);
        mTvShopNum.setText(""+shopNum);

        if(shopNum<=0){
            mTvOrder.setEnabled(false);
            mTvOrder.setBackgroundResource(R.drawable.shape_orgin_gradient_gray);
        }else{
            mTvOrder.setBackgroundResource(R.drawable.shape_orgin_gradient);
            mTvOrder.setEnabled(true);

        }

        //只要有优惠就跳出循环
    }


    @Override
    public void initData() {
        super.initData();

        EventBus.getDefault().register(this);

        mStoreInfo = (StoreInfo) getIntent().getSerializableExtra("storeInfo");
        ImageLoadUtils.setCircularImage(this,mStoreInfo.shop_img,mIvStorePic,R.mipmap.ic_launcher);
        mTvStoreName.setText(mStoreInfo.shop_name);
        mTvScore.setText(mStoreInfo.score+"");
        mTvMonthSale.setText("月售 "+mStoreInfo.monthly_sale);
        mTvDesc.setText(mStoreInfo.shop_desc);
        mTvTime.setText(mStoreInfo.delivery_time);
        String distance ;
//        if(mStoreInfo.distance>1000){
//            distance = mStoreInfo.distance/1000+"km";
//        }else{
//            distance = mStoreInfo.distance+"m";
//        }
        mTvStoreDis.setText(mStoreInfo.distance);

        ShopIdReq shopIdReq = new ShopIdReq();
        shopIdReq.shop_id = mStoreInfo.id+"";
        mPresenter.addGood(shopIdReq);


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back_all) {
            finish();
        } else if (v.getId() == R.id.iv_shop_car) {
            if (mGoodInfoList == null || mGoodInfoList.size() <= 0) {
                return;
            }
            ShopCarPop pop = new ShopCarPop(this,mTvShopNum.getText().toString(),mGoodInfoList);
            pop.showPopupWindow();
        } else if (v.getId() == R.id.tv_order) {
            Intent intent = new Intent(this,ConfirmOrderActivity.class);
            intent.putExtra("shopCar", (Serializable) mGoodInfoList);
            intent.putExtra("storeInfo",mStoreInfo);
            startActivity(intent);
        } else if(v.getId() == R.id.iv_store_collection){
            ShopIdReq addFoodReq = new ShopIdReq();
            addFoodReq.shop_id = AccountManager.getInstance().getShopId();
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
        if(data.shop_info.is_collect == 1){
            mIvCollection.setImageResource(R.mipmap.icon_rating_bar_normal);
        }else{
            mIvCollection.setImageResource(R.mipmap.icon_rating_bar_select);

        }

    }
}
