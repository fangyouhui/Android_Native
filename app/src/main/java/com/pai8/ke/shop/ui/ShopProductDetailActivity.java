package com.pai8.ke.shop.ui;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import com.blankj.utilcode.util.PhoneUtils;
import com.lhs.library.base.BaseActivity;
import com.lhs.library.base.BaseAppConstants;
import com.pai8.ke.activity.takeaway.adapter.BannerAdapter;
import com.pai8.ke.databinding.ActivityShopProductDetailBinding;
import com.pai8.ke.entity.GroupGoodsInfoResult;
import com.pai8.ke.entity.GroupShopInfoResult;
import com.pai8.ke.shop.adapter.ProductImgDetailAdapter;
import com.pai8.ke.shop.viewmodel.ShopProductDetailViewModel;
import com.pai8.ke.utils.ImageLoadUtils;
import com.youth.banner.indicator.CircleIndicator;

import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ShopProductDetailActivity extends BaseActivity<ShopProductDetailViewModel, ActivityShopProductDetailBinding> {
    private String shopId;
    private String productId;

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        shopId = getIntent().getStringExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0);
        productId = getIntent().getStringExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_1);
        mBinding.btnBuy.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), ConfirmOrderActivity.class);
            intent.putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0, mViewModel.getGetGroupGoodsInfoData().getValue());
            startActivity(intent);
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
        mViewModel.getGroupGoodsInfo(productId);
    }

    @Override
    public void addObserve() {
        mViewModel.getGetGroupShopInfoData().observe(this, data -> bindGroupShopInfo(data));
        mViewModel.getGetGroupGoodsInfoData().observe(this, data -> {
            bindBanner(data);
            bindProductInfo(data);
        });
    }

    private void bindBanner(GroupGoodsInfoResult bean) {
        BannerAdapter bannerAdapter = new BannerAdapter(bean.getCover());
        mBinding.banner.setIndicator(new CircleIndicator(this))
                .setAdapter(bannerAdapter);
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

    private void bindProductInfo(GroupGoodsInfoResult bean) {
        mBinding.tvProductTitle.setText(bean.getTitle());
        mBinding.tvGroupBuyPrice.setText(bean.getSell_price());
        mBinding.tvOriginPrice.setText("¥" + bean.getOrigin_price());
        mBinding.tvOriginPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中间横线
        mBinding.tvSoldCount.setText("已售：" + bean.getSale_count());
        mBinding.tvStockCount.setText(String.format("库存：%d份", bean.getStock()));
        mBinding.tvProductDesc.setText("套餐包含：" + bean.getDesc());
        String startTime = timeStampToString(bean.getTerm().getStart_time());
        String endTime = timeStampToString(bean.getTerm().getEnd_time());
        mBinding.tvTermTime.setText(String.format("%s至%s （周末、法定节假日通用）", startTime, endTime));
        mBinding.tvMatter.setText(bean.getMatter());
        mBinding.recyclerView.setAdapter(new ProductImgDetailAdapter(this, bean.getDetails_img()));
    }

    /**
     * 10位int型的时间戳转换为String(yyyy-MM-dd HH:mm:ss)
     *
     * @param time
     * @return
     */
    private String timeStampToString(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd");
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;
    }

}
