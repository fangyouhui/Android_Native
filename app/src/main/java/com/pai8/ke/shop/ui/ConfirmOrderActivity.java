package com.pai8.ke.shop.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.activity.result.contract.ActivityResultContracts;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lhs.library.base.BaseActivity;
import com.lhs.library.base.BaseAppConstants;
import com.pai8.ke.activity.me.CouponListActivity;
import com.pai8.ke.databinding.ActivityConfirmOrderBinding;
import com.pai8.ke.entity.GroupGoodsInfoResult;
import com.pai8.ke.shop.viewmodel.ConfirmOrderViewModel;

import org.jetbrains.annotations.Nullable;

/**
 * 确认订单
 */
public class ConfirmOrderActivity extends BaseActivity<ConfirmOrderViewModel, ActivityConfirmOrderBinding> {
    private GroupGoodsInfoResult bean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bean = (GroupGoodsInfoResult) getIntent().getSerializableExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0);
        bindView();
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.llCoupon.setOnClickListener(v -> {
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {

                }
            }).launch(new Intent(this, CouponListActivity.class));
        });
        mBinding.llBottom.setOnClickListener(v -> pay());
        mBinding.btnReduce.setOnClickListener(v -> {
            int count = Integer.parseInt(mBinding.tvCount.getText().toString());
            if (count > 1) {
                count--;
            }
            mBinding.tvCount.setText(String.valueOf(count));
        });

        mBinding.btnAdd.setOnClickListener(v -> {
            int count = Integer.parseInt(mBinding.tvCount.getText().toString());
            count++;
            mBinding.tvCount.setText(String.valueOf(count));
        });
    }

    private void bindView() {

    }

    private void pay() {
        if (TextUtils.isEmpty(mBinding.etNickname.getText())) {
            ToastUtils.showShort("请输入联系人称呼");
            return;
        }

        if (TextUtils.isEmpty(mBinding.etPhone.getText())) {
            ToastUtils.showShort("请填写收货人的手机号");
            return;
        }

        if (!RegexUtils.isMobileSimple(mBinding.etPhone.getText())) {
            ToastUtils.showShort("请检查手机号是否正确");
            return;
        }


    }


}
