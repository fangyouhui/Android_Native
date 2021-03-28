package com.pai8.ke.shop.ui;

import android.content.Intent;
import android.os.Bundle;

import com.lhs.library.base.BaseActivity;
import com.lhs.library.base.BaseAppConstants;
import com.pai8.ke.databinding.ActivityShopDetailBinding;
import com.pai8.ke.entity.ShopTypeResult;
import com.pai8.ke.shop.viewmodel.ShopMainViewModel;

import org.jetbrains.annotations.Nullable;

public class ShopDetailActivity extends BaseActivity<ShopMainViewModel, ActivityShopDetailBinding> {
    private ShopTypeResult bean;

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        bean = (ShopTypeResult) getIntent().getSerializableExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0);
        mBinding.btnBuy.setOnClickListener(v -> {
            startActivity(new Intent(getBaseContext(), ConfirmOrderActivity.class));
        });
    }

    @Override
    public void initData() {

    }
}
