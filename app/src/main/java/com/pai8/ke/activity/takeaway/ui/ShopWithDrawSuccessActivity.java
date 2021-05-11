package com.pai8.ke.activity.takeaway.ui;

import android.os.Bundle;

import com.lhs.library.base.BaseActivity;
import com.lhs.library.base.NoViewModel;
import com.pai8.ke.databinding.ActivityShopWithDrawlSuccessBinding;

import org.jetbrains.annotations.Nullable;

public class ShopWithDrawSuccessActivity extends BaseActivity<NoViewModel, ActivityShopWithDrawlSuccessBinding> {

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.btnConfirm.setOnClickListener(v -> finish());
    }
}
