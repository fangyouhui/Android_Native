package com.pai8.ke.activity.takeaway.ui;

import android.graphics.Bitmap;

import com.lhs.library.base.BaseActivity;
import com.pai8.ke.activity.takeaway.entity.resq.StoreInfoResult;
import com.pai8.ke.databinding.ActivityShopQrCodeBinding;
import com.pai8.ke.qrcode.QRCodeTools;
import com.pai8.ke.shop.viewmodel.ShopQrCodeViewModel;
import com.pai8.ke.utils.ImageLoadUtils;

public class ShopQrCodeActivity extends BaseActivity<ShopQrCodeViewModel, ActivityShopQrCodeBinding> {

    @Override
    public void initData() {
        mViewModel.videoType();
    }

    @Override
    public void addObserve() {
        mViewModel.getVideoTypeData().observe(this, data -> {
            mViewModel.shopIndex();
        });
        mViewModel.getShopIndexData().observe(this, data -> bindViewData(data));
    }

    private void bindViewData(StoreInfoResult bean) {
        ImageLoadUtils.loadImage(bean.shop_img_url, mBinding.ivShopImg);
        mBinding.tvShopName.setText(bean.shop_name);

        mBinding.labelsView.setLabels(bean.cate_arr);
        mBinding.tvAddress.setText("地址：" + bean.shop_address);
        mBinding.tvPhone.setText("电话：" + bean.shop_mobile);

        Bitmap bitmap = QRCodeTools.createCode(bean.shop_code + "", 300, false);
        mBinding.imageView.setImageBitmap(bitmap);
    }
}
