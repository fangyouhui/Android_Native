package com.pai8.ke.shop.ui;

import android.content.Intent;
import android.os.Bundle;

import com.blankj.utilcode.util.PhoneUtils;
import com.lhs.library.base.BaseActivity;
import com.lhs.library.base.BaseAppConstants;
import com.pai8.ke.databinding.ActivityShopProductDetailBinding;
import com.pai8.ke.entity.GroupShopInfoResult;
import com.pai8.ke.shop.viewmodel.ShopProductDetailViewModel;
import com.pai8.ke.utils.ImageLoadUtils;

import org.jetbrains.annotations.Nullable;

public class ShopProductDetailActivity extends BaseActivity<ShopProductDetailViewModel, ActivityShopProductDetailBinding> {
    private String shopId;

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        shopId = getIntent().getStringExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0);
        mBinding.btnBuy.setOnClickListener(v -> {
            startActivity(new Intent(getBaseContext(), ConfirmOrderActivity.class));
        });
        mBinding.btnShop.setOnClickListener(v -> {
            Intent intent = new Intent(this, BusinessHomeActivity.class);
            intent.putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0, shopId + "");
            startActivity(intent);

        });
        mBinding.btnCall.setOnClickListener(v -> {
            if (mBinding.btnCall.getTag() != null) {
                String phone = (String) mBinding.btnCall.getTag();
                PhoneUtils.dial(phone);
            }
        });
    }

    @Override
    public void initData() {
        mViewModel.getGroupShopInfo(shopId);
    }

    @Override
    public void addObserve() {
        mViewModel.getGetGroupShopInfoData().observe(this, data -> {
            bindGroupShopInfo(data);
        });
    }

    private void bindGroupShopInfo(GroupShopInfoResult bean) {
        ImageLoadUtils.loadImage(bean.getShop_img(), mBinding.ivLogo);
        mBinding.tvName.setText(bean.getShop_name());
        mBinding.tvFraction.setText(bean.getScore() + "");
        mBinding.tvTotalSales.setText(String.format("总销量 %d", bean.getMonth_sale_count()));
        mBinding.tvAddress.setText("地址：" + bean.getCity() + bean.getDistrict() + bean.getAddress());
        mBinding.tvPhone.setText("电话：" + bean.getMobile());
        mBinding.btnCall.setTag(bean.getMobile());
    }

}
