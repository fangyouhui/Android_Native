package com.pai8.ke.activity.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gavin.com.library.StickyDecoration;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.pai8.ke.R;
import com.pai8.ke.activity.me.adapter.CouponListAdapter;
import com.pai8.ke.activity.takeaway.ui.StoreActivity;
import com.pai8.ke.api.Api;
import com.pai8.ke.app.MyApp;
import com.pai8.ke.base.BaseActivity;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.entity.resp.CouponListResp;
import com.pai8.ke.utils.CollectionUtils;
import com.pai8.ke.utils.DensityUtils;
import com.pai8.ke.utils.ResUtils;

import java.util.Collections;
import java.util.List;

import androidx.annotation.IntDef;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 优惠券列表
 * Created by gh on 2020/11/25.
 */
public class CouponListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.l_rv)
    LuRecyclerView lrv;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout srLayout;
    @BindView(R.id.tv_btn_use)
    TextView tvBtnUse;
    @BindView(R.id.ll_wrap_btn_use)
    LinearLayout llWrapBtnUse;

    //优惠券
    public static final int INTENT_TYPE_CAN_USE = 1;
    //过期
    public static final int INTENT_TYPE_TIME_OUT = 2;
    //选择
    public static final int INTENT_TYPE_SELECT = 3;

    private int mIntentType;

    @IntDef({INTENT_TYPE_CAN_USE, INTENT_TYPE_TIME_OUT, INTENT_TYPE_SELECT})
    public @interface IntentType {

    }

    private LuRecyclerViewAdapter mLRvAdapter;
    private CouponListAdapter mAdapter;

    public static void launch(Context context, @IntentType int intentType) {
        Intent intent = new Intent(context, CouponListActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtra("intentType", intentType);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_coupon_list;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        mIntentType = getIntent().getExtras().getInt("intentType");
        switch (mIntentType) {
            case INTENT_TYPE_CAN_USE:
                mTitleBar.setTitle("优惠券");
                llWrapBtnUse.setVisibility(View.GONE);
                break;
            case INTENT_TYPE_TIME_OUT:
                mTitleBar.setTitle("已过期优惠券");
                llWrapBtnUse.setVisibility(View.GONE);
                break;
            case INTENT_TYPE_SELECT:
                mTitleBar.setTitle("选择优惠券");
                llWrapBtnUse.setVisibility(View.VISIBLE);
                break;
        }

        srLayout.setColorSchemeResources(R.color.colorPrimary);
        mAdapter = new CouponListAdapter(this);
        mLRvAdapter = new LuRecyclerViewAdapter(mAdapter);
        lrv.setLayoutManager(new LinearLayoutManager(this));
        if (mIntentType == INTENT_TYPE_CAN_USE || mIntentType == INTENT_TYPE_TIME_OUT) {
            // StickyDecoration
            StickyDecoration stickyDecoration = StickyDecoration.Builder
                    .init(position -> {
                        if (mAdapter.getDataList().get(position).getCoupon_info() == null) {
                            return "商铺满减券";
                        }
                        return mAdapter.getDataList().get(position).getCoupon_info().getGroupName();
                    })
                    .setGroupBackground(ResUtils.getColor(R.color.color_bg))
                    .setGroupHeight(DensityUtils.dip2px(32))
                    .setDivideHeight(DensityUtils.dip2px(0.6f))
                    .setDivideColor(ResUtils.getColor(R.color.color_bg))
                    .setGroupTextColor(ResUtils.getColor(R.color.color_dark_font))
                    .setGroupTextSize(DensityUtils.dip2px(16))
                    .setTextSideMargin(DensityUtils.dip2px(16))
                    .build();
            lrv.addItemDecoration(stickyDecoration);
        }
        lrv.setAdapter(mLRvAdapter);
        lrv.setLoadMoreEnabled(false);

        mAdapter.setType(mIntentType);
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
        mAdapter.setClick(new CouponListAdapter.Click() {
            @Override
            public void onUseClick(CouponListResp couponListResp) {
                StoreActivity.launch(CouponListActivity.this,couponListResp.getShop_id());
            }

            @Override
            public void onSelect() {
                List<CouponListResp> select = mAdapter.getSelect();
                if (CollectionUtils.isNotEmpty(select)) {
                    tvBtnUse.setText("已选择" + select.size() + "张券 立即使用");
                } else {
                    tvBtnUse.setText("请选择优惠券");
                }
            }
        });
    }

    @OnClick(R.id.tv_btn_use)
    public void onViewClicked() {
        List<CouponListResp> select = mAdapter.getSelect();
        if (CollectionUtils.isEmpty(select)) {
            toast("请先选择优惠券");
            return;
        }
    }

    @Override
    public void onRefresh() {
        srLayout.setRefreshing(true);
        MyApp.getMyAppHandler().postDelayed(() -> {
            if (mIntentType == 1) {
                getCouponList(1);
            } else {
                getCouponList(2);
            }
        }, 200);
    }

    public void refreshComplete() {
        srLayout.setRefreshing(false);
    }

    private void getCouponList(int type) {
        Api.getInstance().allUserCouponList(type)
                .doOnSubscribe(disposable -> {

                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<List<CouponListResp>>() {
                    @Override
                    protected void onSuccess(List<CouponListResp> list) {
                        Collections.sort(list, (o1, o2) -> {
                            if (o1.getCoupon_info() == null) {
                                return -1;
                            }
                            return o1.getCoupon_info().getType() == 1 ? -1 : 1;
                        });
                        refreshComplete();
                        mAdapter.setDataList(list);
                        lrv.refreshComplete(list.size());
                        mLRvAdapter.notifyDataSetChanged();
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        refreshComplete();
                        super.onError(msg, errorCode);
                    }
                });
    }
}
