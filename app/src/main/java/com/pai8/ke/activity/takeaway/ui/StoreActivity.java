package com.pai8.ke.activity.takeaway.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.pai8.ke.R;
import com.pai8.ke.activity.common.NaviActivity;
import com.pai8.ke.activity.takeaway.Constants;
import com.pai8.ke.activity.takeaway.adapter.ViewPagerAdapter;
import com.pai8.ke.activity.takeaway.contract.StoreContract;
import com.pai8.ke.activity.takeaway.entity.FoodGoodInfo;
import com.pai8.ke.activity.takeaway.entity.ShopFoodGoodInfo;
import com.pai8.ke.activity.takeaway.entity.event.AddGoodEvent;
import com.pai8.ke.activity.takeaway.entity.event.CartNumEvent;
import com.pai8.ke.activity.takeaway.entity.event.ShopCarEvent;
import com.pai8.ke.activity.takeaway.entity.req.ShopIdReq;
import com.pai8.ke.activity.takeaway.entity.resq.ShopContent;
import com.pai8.ke.activity.takeaway.entity.resq.StoreInfoResult;
import com.pai8.ke.activity.takeaway.fragment.EvaluateFragment;
import com.pai8.ke.activity.takeaway.fragment.GoodFragment;
import com.pai8.ke.activity.takeaway.fragment.StoreFragment;
import com.pai8.ke.activity.takeaway.order.OrderConfirmActivity;
import com.pai8.ke.activity.takeaway.presenter.StorePresenter;
import com.pai8.ke.activity.takeaway.utils.AddToCartUtil;
import com.pai8.ke.activity.takeaway.widget.ShopCarPop;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.entity.Address;
import com.pai8.ke.entity.resp.ShareMiniResp;
import com.pai8.ke.fragment.CouponGetDialogFragment;
import com.pai8.ke.interfaces.contract.ShareContract;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.presenter.SharePresenter;
import com.pai8.ke.utils.AMapLocationUtils;
import com.pai8.ke.utils.DensityUtils;
import com.pai8.ke.utils.EventBusUtils;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.utils.PreferencesUtils;
import com.pai8.ke.utils.StringUtils;
import com.pai8.ke.utils.WxShareUtils;
import com.pai8.ke.widget.BottomDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

import static com.pai8.ke.global.EventCode.EVENT_CHOOSE_ADDRESS;
import static com.pai8.ke.utils.AppUtils.isWeChatClientValid;

public class StoreActivity extends BaseMvpActivity<StorePresenter> implements View.OnClickListener, StoreContract.View, ShareContract.View {
    private ArrayList<Fragment> fragments;
    private AppBarLayout appbarlayout;
    private Toolbar toolbar;
    private ImageView mIvSearch;
    private int MAX_SCROLL;
    private TextView mTvTitle;
    private ImageView mIvBack, mIvCollection, mIvShare;
    private ImageView mIvShopCar;
    private TextView mTvOrder;
    private StoreInfoResult mStoreInfo;  //商家信息
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
    private TextView tv_btn_navi;

    private int mStart = 0;
    private List<FoodGoodInfo> mGoodInfoList;  //购物车
    private ShopIdReq mShopIdReq;

    private String orderNo;
    private SharePresenter mSharePresenter;
    private BottomDialog mShareBottomDialog;
    private ShopCarPop mPop;
    private BottomDialog mBottomDialog;
    private ShopContent mData;

    public static void launch(Context context, String shopId) {
        if (StringUtils.isEmpty(shopId)) return;
        StoreInfoResult storeInfo = new StoreInfoResult();
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
        tv_btn_navi = findViewById(R.id.tv_btn_navi);
        tv_btn_navi.setOnClickListener(this);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCartEvent(CartNumEvent event) {
        if (event.getType() == Constants.EVENT_TYPE_ADD_CAR) {
            for (FoodGoodInfo pro : mGoodInfoList) {
                if (pro.id == event.getGoods_id()) {
                    pro.goods_num = event.getNumber();
                    break;
                }
            }
            ShopFoodGoodInfo data = new ShopFoodGoodInfo();
            data.goods_info = mGoodInfoList;
            getCarSuccess(data);
            mPop.setPrice(mGoodInfoList);
        } else if (event.getType() == Constants.EVENT_TYPE_DELETE_CAR) {
            for (FoodGoodInfo pro : mGoodInfoList) {
                if (pro.id == event.getGoods_id()) {
                    if (event.getNumber() == 0) {
                        mGoodInfoList.remove(pro);
                    } else {
                        pro.goods_num = event.getNumber();
                    }
                    break;
                }
            }
            if (mGoodInfoList.size() == 0) {
                if (mPop != null && mPop.isShowing()) {
                    mPop.dismiss();
                }
            }
            ShopFoodGoodInfo data = new ShopFoodGoodInfo();
            data.goods_info = mGoodInfoList;
            getCarSuccess(data);
            mPop.setPrice(mGoodInfoList);
            mPop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
            if (data != null) {
                saveCurLocation(data.getStringExtra("lat"), data.getStringExtra("lng"),
                        data.getStringExtra("address"));
                mPresenter.outDistance(mStoreInfo.id + "", data.getIntExtra("id", -1) + "");
            }
        }
    }

    void saveCurLocation(String latitude, String longitude, String address) {
        PreferencesUtils.put(this, "latitude", latitude);
        PreferencesUtils.put(this, "longitude", longitude);
        PreferencesUtils.put(this, "address", address);
    }

    void showOutDistancePop(String msg) {
        View view;
        if (mBottomDialog == null) {
            view = View.inflate(this, R.layout.pop_out_distance, null);
            mBottomDialog = new BottomDialog(this, view);
        } else {
            view = mBottomDialog.getView();
        }
        ViewHolder holder = new ViewHolder(view);
        holder.ivClose.setOnClickListener(view1 -> {
            mBottomDialog.dismiss();
            finish();
        });
        holder.tvAddress.setText(PreferencesUtils.getObjectFromString(this, "address"));
        holder.tvSeeAround.setOnClickListener(view1 -> {
            AMapLocationUtils.getLocation(location -> {
                saveCurLocation(location.getLatitude() + "",
                        location.getLongitude() + "", location.getAddress());
                Address mAddress = new Address();
                mAddress.setAddress(location.getAddress());
                mAddress.setLat(location.getLatitude());
                mAddress.setLon(location.getLongitude());
                EventBusUtils.sendEvent(new BaseEvent(EVENT_CHOOSE_ADDRESS, mAddress));
                finish();
                mBottomDialog.dismiss();
            }, true);
        });
        holder.tvChangeAddress.setOnClickListener(view1 -> {
            Intent intent = new Intent(this, DeliveryAddressActivity.class);
            intent.putExtra("TYPE", 2);
            startActivityForResult(intent, 100);
        });
        mBottomDialog.setIsCanceledOnTouchOutside(true);
        mBottomDialog.setOnCancelListener(dialogInterface -> {
            finish();
        });
        mBottomDialog.show();
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
        mTvPrice.setText(String.format("￥%s", new DecimalFormat("##.##").format(toMoney)));
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
        mSharePresenter = new SharePresenter(this);
        EventBus.getDefault().register(this);
        orderNo = getIntent().getStringExtra("orderNo");
        mStoreInfo = (StoreInfoResult) getIntent().getSerializableExtra("storeInfo");
        setData(mStoreInfo);
        mShopIdReq = new ShopIdReq();
        mShopIdReq.shop_id = mStoreInfo.id + "";
        mPresenter.shopContent(mShopIdReq);

        //再来一旦
        if (!TextUtils.isEmpty(orderNo)) {
            mPresenter.reAddCart(orderNo);
        }
    }


    private void setData(StoreInfoResult mStoreInfo) {
        if (mStoreInfo != null && !TextUtils.isEmpty(mStoreInfo.shop_name)) {
            ImageLoadUtils.setCircularImage(this, mStoreInfo.shop_img, mIvStorePic, R.mipmap.ic_launcher);
            mTvStoreName.setText(mStoreInfo.shop_name);
            mTvScore.setText(mStoreInfo.score + "");
            mTvMonthSale.setText("月售 " + mStoreInfo.month_sale_count);
            mTvDesc.setText(StringUtils.strSafe(mStoreInfo.address));
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
        } else if (v.getId() == R.id.iv_store_search) {
            startActivity(new Intent(this, ShopGoodSearchActivity.class)
                    .putExtra("shopId", mStoreInfo.id));
        } else if (v.getId() == R.id.iv_shop_car) {
            if (mGoodInfoList == null || mGoodInfoList.size() <= 0) {
                return;
            }
            mPop = new ShopCarPop(this, mTvShopNum.getText().toString(), mGoodInfoList, mStoreInfo.id);
            mPop.setOnSelectListener(new ShopCarPop.OnSelectListener() {
                @Override
                public void onSelect() {
                    Intent intent = new Intent(StoreActivity.this, OrderConfirmActivity.class);
                    intent.putExtra("shopCar", (Serializable) mGoodInfoList);
                    intent.putExtra("storeInfo", mStoreInfo);
                    startActivity(intent);
                }
            });
            mPop.showPopupWindow();
        } else if (v.getId() == R.id.tv_order) {
            Intent intent = new Intent(this, OrderConfirmActivity.class);
            intent.putExtra("shopCar", (Serializable) mGoodInfoList);
            intent.putExtra("storeInfo", mStoreInfo);
            startActivity(intent);
        } else if (v.getId() == R.id.iv_store_collection) {
            ShopIdReq addFoodReq = new ShopIdReq();
            addFoodReq.shop_id = AccountManager.getInstance().getShopId();
            if (mStoreInfo.is_collect == 1) {
                mPresenter.unCollection(addFoodReq);
            } else {
                mPresenter.collection(addFoodReq);
            }

        } else if (v.getId() == R.id.iv_store_share) { //分享
            View view = View.inflate(this, R.layout.view_dialog_share, null);
            ImageButton itnClose = view.findViewById(R.id.itn_close);
            TextView tvBtnCancel = view.findViewById(R.id.tv_btn_cancel);
            TextView tvBtnWechatFriend = view.findViewById(R.id.tv_btn_wechat_friend);
            TextView tvBtnWechatMoments = view.findViewById(R.id.tv_btn_wechat_moments);
            tvBtnWechatMoments.setVisibility(View.GONE);
            itnClose.setOnClickListener(view1 -> {
                mShareBottomDialog.dismiss();
            });
            tvBtnCancel.setOnClickListener(view1 -> {
                mShareBottomDialog.dismiss();
            });
            tvBtnWechatFriend.setOnClickListener(view1 -> {
                if (!isWeChatClientValid()) return;
                mSharePresenter.shareShop(mShopIdReq.shop_id);
            });
            if (mShareBottomDialog == null) {
                mShareBottomDialog = new BottomDialog(this, view);
            }
            mShareBottomDialog.setIsCanceledOnTouchOutside(true);
            mShareBottomDialog.show();
        } else if (v.getId() == R.id.tv_btn_navi) {//导航
            if (mData == null) return;
            try {
                String address = mData.shop_info.address;
                String distance = mData.shop_info.distance;
                String latitude = mData.shop_info.latitude;
                String longitude = mData.shop_info.longitude;
                NaviActivity.launch(this, address, distance, longitude, latitude);
            } catch (Exception e) {
                e.printStackTrace();
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
        mData = data;
        mPresenter.getCart(mStoreInfo.id + "");
        setData(data.shop_info);
        if (data.shop_info.is_collect == 0) {
            mIvCollection.setImageResource(R.mipmap.icon_rating_bar_normal);
        } else {
            mIvCollection.setImageResource(R.mipmap.icon_rating_bar_select);

        }

    }

    @Override
    public void collectionSuccess(JSONObject data) {
        mStoreInfo.is_collect = 1;
        mIvCollection.setImageResource(R.mipmap.icon_rating_bar_select);

    }

    @Override
    public void unCollectionSuccess(JSONObject data) {
        mStoreInfo.is_collect = 0;
        mIvCollection.setImageResource(R.mipmap.icon_rating_bar_normal);
    }

    @Override
    public void getCarSuccess(ShopFoodGoodInfo data) {
        mGoodInfoList = data.goods_info;
        setPrice(mGoodInfoList);
        EventBus.getDefault().post(new ShopCarEvent(data));


    }

    @Override
    public void onFail(String msg) {
        showOutDistancePop(msg);
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

    @Override
    public void shareMini(ShareMiniResp resp) {
        WxShareUtils.shareToWeChat(resp, new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                runOnUiThread(() -> {
                    if (mShareBottomDialog != null && mShareBottomDialog.isShowing())
                        mShareBottomDialog.dismiss();
                });
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                runOnUiThread(() -> {
                    toast("分享失败");
                });
            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
    }


    class ViewHolder {
        @BindView(R.id.iv_close)
        ImageButton ivClose;
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.tv_see_around)
        TextView tvSeeAround;
        @BindView(R.id.tv_change_address)
        TextView tvChangeAddress;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
