package com.pai8.ke.shop.ui;

import android.os.Bundle;

import com.lhs.library.base.BaseAppConstants;
import com.lhs.library.base.BaseBottomDialogFragment;
import com.lhs.library.base.NoViewModel;
import com.pai8.ke.databinding.DialogBottomPaySelectBinding;

import org.jetbrains.annotations.Nullable;

public class PaySelectBottomDialog extends BaseBottomDialogFragment<NoViewModel, DialogBottomPaySelectBinding> {
    private String totalPrice;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        totalPrice = getArguments().getString(BaseAppConstants.BundleConstant.ARG_PARAMS_0);
    }

    @Override
    public void initView() {
        mBinding.tvPrice.setText("¥ " + totalPrice);
        mBinding.btnPay.setText("¥ " + totalPrice + " 立即支付");
        mBinding.ivClose.setOnClickListener(v -> {
            dismiss();
            if (mDialogListener != null) {
                mDialogListener.onCloseClickListener();
            }
        });
        mBinding.clPayWeChat.setOnClickListener(v -> {
            mBinding.check.setChecked(true);
            mBinding.check2.setChecked(false);

        });
        mBinding.clPayAli.setOnClickListener(v -> {
            mBinding.check.setChecked(false);
            mBinding.check2.setChecked(true);
        });
        mBinding.btnPay.setOnClickListener(v -> {
            int payWay = 0;
            if (mBinding.check2.isChecked()) {
                payWay = 1;
            }
            if (mDialogListener != null) {
                mDialogListener.onConfirmClickListener(payWay);
            }
            dismiss();
        });
    }
}
