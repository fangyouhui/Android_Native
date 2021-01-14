package com.pai8.ke.activity.takeaway.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gavin.com.library.StickyDecoration;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.pai8.ke.R;
import com.pai8.ke.activity.me.entity.resp.CouponResp;
import com.pai8.ke.activity.takeaway.adapter.ShopCouponListAdapter;
import com.pai8.ke.api.Api;
import com.pai8.ke.app.MyApp;
import com.pai8.ke.base.BaseActivity;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.utils.DensityUtils;
import com.pai8.ke.utils.ResUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.IntDef;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.OnClick;

import static com.pai8.ke.global.EventCode.EVENT_COUPON;

/**
 * 优惠券
 */
public class CouponActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.l_rv)
    LuRecyclerView lrv;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout srLayout;

    //营销管理
    public static final int INTENT_TYPE_SALE = 1;
    //获取优惠券
    public static final int INTENT_TYPE_COUPON = 2;

    private int mIntentType;

    @IntDef({INTENT_TYPE_SALE, INTENT_TYPE_COUPON})
    public @interface IntentType {

    }

    private LuRecyclerViewAdapter mLRvAdapter;

    private ShopCouponListAdapter mAdapter;

    public static void launch(Context context, @IntentType int intentType) {
        Intent intent = new Intent(context, CouponActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtra("intentType", intentType);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_coupon;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        mTitleBar.setTitle("营销管理");
        srLayout.setColorSchemeResources(R.color.colorPrimary);
        mAdapter = new ShopCouponListAdapter(this);
        mLRvAdapter = new LuRecyclerViewAdapter(mAdapter);
        lrv.setLayoutManager(new LinearLayoutManager(this));
        StickyDecoration stickyDecoration = StickyDecoration.Builder
                .init(position -> {
                    if (mAdapter.getDataList().get(position).getType() == 1) {
                        return "优惠券";
                    }else {
                        return "运费券";
                    }
                })
                .setGroupBackground(ResUtils.getColor(R.color.color_bg))
                .setGroupHeight(DensityUtils.dip2px(45))
                .setDivideHeight(DensityUtils.dip2px(0.6f))
                .setDivideColor(ResUtils.getColor(R.color.color_bg))
                .setGroupTextColor(ResUtils.getColor(R.color.color_dark_font))
                .setGroupTextSize(DensityUtils.dip2px(16))
                .setTextSideMargin(DensityUtils.dip2px(16))
                .build();
        lrv.addItemDecoration(stickyDecoration);
        lrv.setAdapter(mLRvAdapter);
        lrv.setLoadMoreEnabled(false);
//        mAdapter.setType(mIntentType);
        onRefresh();
    }

    @Override
    public void initListener() {
        srLayout.setOnRefreshListener(this);
        titleBar.setOnTitleBarListener(new OnTitleBarListener() {
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
        mAdapter.setClick(bean -> {
            AddCouponActivity.launchEdit(this, bean);
        });
    }

    @Override
    public void onRefresh() {
        srLayout.setRefreshing(true);
        MyApp.getMyAppHandler().postDelayed(() -> {
            if (mIntentType == 1) {
                getCouponList();
            } else {
                getCouponList();
            }
        }, 200);
    }

    public void refreshComplete() {
        srLayout.setRefreshing(false);
    }

    private void getCouponList() {
        Api.getInstance().shopCouponList(mAccountManager.getShopId(), mAccountManager.getUid())
                .doOnSubscribe(disposable -> {

                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<CouponResp>() {
                    @Override
                    protected void onSuccess(CouponResp resp) {
                        refreshComplete();
                        List<CouponResp.CouponListBean> list = new ArrayList<>();
                        list.addAll(resp.getOrder_coupon_list());
                        list.addAll(resp.getExpress_coupon_list());
                        mAdapter.setDataList(list);
                        lrv.refreshComplete(list.size());
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        refreshComplete();
                        super.onError(msg, errorCode);
                    }
                });
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(BaseEvent event) {
        super.receiveEvent(event);
        if (event.getCode() == EVENT_COUPON) {
            onRefresh();
        }
    }

    @OnClick(R.id.tv_add_coupon)
    public void onClick(View view) {
        AddCouponActivity.launchAdd(this);
    }

}
