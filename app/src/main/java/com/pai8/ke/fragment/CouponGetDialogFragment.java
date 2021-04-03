package com.pai8.ke.fragment;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;

import com.lhs.library.base.BaseDialogFragment;
import com.pai8.ke.R;
import com.pai8.ke.activity.me.entity.resp.ShopCouponListResult;
import com.pai8.ke.adapter.CouponGetListAdapter;
import com.pai8.ke.databinding.DialogCouponGetBinding;
import com.pai8.ke.shop.viewmodel.CouponGetViewModel;
import com.pai8.ke.utils.SpanUtils;
import com.pai8.ke.utils.StringUtils;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.VISIBLE;

/**
 * 优惠券领取对话框
 */
public class CouponGetDialogFragment extends BaseDialogFragment<CouponGetViewModel, DialogCouponGetBinding> {

    private String mShopId;
    private CouponGetListAdapter mAdapter;

    public static CouponGetDialogFragment newInstance(String shopId) {
        CouponGetDialogFragment fragment = new CouponGetDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("shopId", shopId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShopId = getArguments().getString("shopId");
    }

    @Override
    public void initView() {
        mAdapter = new CouponGetListAdapter(getActivity(), null);
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.ivBtnGet.setOnClickListener(v -> {
            if (mBinding.tvTip.getVisibility() == VISIBLE) {
                dismiss();
            } else { //领取
                List<ShopCouponListResult.CouponListBean> dataList = mAdapter.getData();
                List<String> a = new ArrayList<>();
                for (ShopCouponListResult.CouponListBean couponGetListResp : dataList) {
                    a.add(couponGetListResp.getId() + "");
                }
                String s = StringUtils.intJoinStr(a, "/");
                mViewModel.getCoupon(s);
            }
        });
        mBinding.ivBtnClose.setOnClickListener(v -> dismiss());
    }

    @Override
    public void addObserve() {
        mViewModel.getShopCouponListData().observe(this, data -> {
            List<ShopCouponListResult.CouponListBean> list = data.getExpress_coupon_list();
            list.addAll(data.getOrder_coupon_list());
            if (!list.isEmpty()) {
                mAdapter.setData(list);
                mBinding.recyclerView.setVisibility(VISIBLE);
                mBinding.tvTip.setVisibility(View.INVISIBLE);
                mBinding.ivBtnGet.setImageResource(R.mipmap.img_btn_get_coupon);
            } else {
                empty();
            }
        });

        mViewModel.getGetCouponData().observe(this, data -> {
            getSuccess();
        });
    }

    @Override
    public void initData() {
        mViewModel.shopCouponList(mShopId);
    }

    private void getSuccess() {
        SpannableStringBuilder span = SpanUtils.getBuilder("")
                .append(getContext(), "恭喜您领取成功！\n")
                .setBold()
                .append(getContext(), "详情请前往【我的】-【优惠券】页面查看！")
                .setProportion(0.7f)
                .create(getContext());
        mBinding.tvTip.setText(span);
        mBinding.recyclerView.setVisibility(View.INVISIBLE);
        mBinding.tvTip.setVisibility(VISIBLE);
        mBinding.ivBtnGet.setImageResource(R.mipmap.img_btn_submit);
    }

    private void empty() {
        SpannableStringBuilder span = SpanUtils.getBuilder("")
                .append(getContext(), "暂无可领取的优惠券！\n")
                .setBold()
                .append(getContext(), "详情请前往【我的】-【优惠券】页面查看！")
                .setProportion(0.7f)
                .create(getContext());
        mBinding.tvTip.setText(span);
        mBinding.recyclerView.setVisibility(View.INVISIBLE);
        mBinding.tvTip.setVisibility(VISIBLE);
        mBinding.ivBtnGet.setImageResource(R.mipmap.img_btn_submit);
    }
}
