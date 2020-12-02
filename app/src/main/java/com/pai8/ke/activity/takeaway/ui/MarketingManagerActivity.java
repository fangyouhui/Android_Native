package com.pai8.ke.activity.takeaway.ui;

import android.view.View;
import android.widget.TextView;

import com.hjq.bar.OnTitleBarListener;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.ShopCouponListAdapter;
import com.pai8.ke.api.Api;
import com.pai8.ke.base.BaseActivity;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.entity.resp.CouponGetListResp;
import com.pai8.ke.entity.resp.CouponListResp;
import com.pai8.ke.manager.AccountManager;

import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import static com.pai8.ke.global.EventCode.EVENT_COUPON;

/**
 * 营销管理
 */
public class MarketingManagerActivity extends BaseActivity {

    @BindView(R.id.tv_add_coupon)
    TextView tvAddCoupon;
    @BindView(R.id.rv)
    RecyclerView rv;

    private ShopCouponListAdapter mAdapter;

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(BaseEvent event) {
        super.receiveEvent(event);
        switch (event.getCode()) {
            case EVENT_COUPON:
                initData();
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_marketing_manager;
    }

    @Override
    public void initView() {
        mTitleBar.setTitle("营销管理");
        mAdapter = new ShopCouponListAdapter(this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        rv.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {
        super.initListener();
        mTitleBar.setOnTitleBarListener(new OnTitleBarListener() {
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
        mAdapter.setClick(couponGetListResp -> {
            AddCouponActivity.launchEdit(this, couponGetListResp);
        });
    }

    @Override
    public void initData() {
        showLoadingDialog(null);
        Api.getInstance().shopCouponList(mAccountManager.getShopId(), mAccountManager.getUid())
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<List<CouponGetListResp>>() {
                    @Override
                    protected void onSuccess(List<CouponGetListResp> list) {
                        dismissLoadingDialog();
                        mAdapter.setDataList(list);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        dismissLoadingDialog();
                        super.onError(msg, errorCode);
                    }
                });
    }

    @OnClick(R.id.tv_add_coupon)
    public void onClick(View view) {
        AddCouponActivity.launchAdd(this);
    }

}
