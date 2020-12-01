package com.pai8.ke.fragment;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pai8.ke.R;
import com.pai8.ke.adapter.CouponGetListAdapter;
import com.pai8.ke.api.Api;
import com.pai8.ke.base.BaseCommDialogFragment;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.entity.resp.CouponGetListResp;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.utils.CollectionUtils;
import com.pai8.ke.utils.SpanUtils;
import com.pai8.ke.utils.StringUtils;
import com.pai8.ke.widget.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.VISIBLE;

/**
 * 优惠券领取对话框
 */
public class CouponGetDialogFragment extends BaseCommDialogFragment {

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.iv_btn_get)
    ImageView ivBtnGet;
    @BindView(R.id.iv_btn_close)
    ImageView ivBtnClose;
    @BindView(R.id.tv_tip)
    TextView tvTip;
    private String mShopId;

    private CouponGetListAdapter mAdapter;

    public static CouponGetDialogFragment newInstance() {
        CouponGetDialogFragment fragment = new CouponGetDialogFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static CouponGetDialogFragment newInstance(String shopId) {
        CouponGetDialogFragment fragment = new CouponGetDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("shopId", shopId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_dialog_fragment_coupon_get;
    }

    @Override
    public void initView(Bundle arguments) {
        mShopId = arguments.getString("shopId");
        mAdapter = new CouponGetListAdapter(getActivity());
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        if (StringUtils.isNotEmpty(mShopId)) {
            Api.getInstance().shopCouponList(mShopId, AccountManager.getInstance().getUid())
                    .doOnSubscribe(disposable -> {

                    })
                    .compose(RxSchedulers.io_main())
                    .subscribe(new BaseObserver<List<CouponGetListResp>>() {
                        @Override
                        protected void onSuccess(List<CouponGetListResp> list) {
                            if (CollectionUtils.isNotEmpty(list)) {
                                mAdapter.setDataList(list);
                                rv.setVisibility(VISIBLE);
                                tvTip.setVisibility(View.INVISIBLE);
                                ivBtnGet.setImageResource(R.mipmap.img_btn_get_coupon);
                            } else {
                                empty();
                            }
                        }

                        @Override
                        protected void onError(String msg, int errorCode) {
                            super.onError(msg, errorCode);
                        }
                    });
        }
    }

    @OnClick({R.id.iv_btn_get, R.id.iv_btn_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_btn_get:
                if (tvTip.getVisibility() == VISIBLE) {
                    dismiss();
                } else {
                    //领取
                    List<CouponGetListResp> dataList = mAdapter.getDataList();
                    List<String> a = new ArrayList<>();
                    for (CouponGetListResp couponGetListResp : dataList) {
                        a.add(couponGetListResp.getId());
                    }
                    String s = StringUtils.intJoinStr(a, "/");
                    Api.getInstance().getCoupon(AccountManager.getInstance().getUid(), s)
                            .doOnSubscribe(disposable -> {

                            })
                            .compose(RxSchedulers.io_main())
                            .subscribe(new BaseObserver() {
                                @Override
                                protected void onSuccess(Object o) {
                                    getSuccess();
                                }

                                @Override
                                protected void onError(String msg, int errorCode) {
                                    super.onError(msg, errorCode);
                                }
                            });
                }
                break;
            case R.id.iv_btn_close:
                dismiss();
                break;
        }
    }

    private void getSuccess() {
        SpannableStringBuilder span = SpanUtils.getBuilder("")
                .append(mContext, "恭喜您领取成功！\n")
                .setBold()
                .append(mContext, "详情请前往【我的】-【优惠券】页面查看！")
                .setProportion(0.7f)
                .create(mContext);
        tvTip.setText(span);
        rv.setVisibility(View.INVISIBLE);
        tvTip.setVisibility(VISIBLE);
        ivBtnGet.setImageResource(R.mipmap.img_btn_submit);
    }

    private void empty() {
        SpannableStringBuilder span = SpanUtils.getBuilder("")
                .append(mContext, "暂无可领取的优惠券！\n")
                .setBold()
                .append(mContext, "详情请前往【我的】-【优惠券】页面查看！")
                .setProportion(0.7f)
                .create(mContext);
        tvTip.setText(span);
        rv.setVisibility(View.INVISIBLE);
        tvTip.setVisibility(VISIBLE);
        ivBtnGet.setImageResource(R.mipmap.img_btn_submit);
    }
}
