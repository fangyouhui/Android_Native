package com.pai8.ke.activity.takeaway.ui;

import android.graphics.Bitmap;

import com.lhs.library.base.BaseActivity;
import com.pai8.ke.databinding.ActivityShopQrCodeBinding;
import com.pai8.ke.entity.GroupShopInfoResult;
import com.pai8.ke.manager.AccountManager;
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
            mViewModel.getGroupShopInfo(AccountManager.getInstance().getShopId());
        });
        mViewModel.getGroupShopInfoData().observe(this, data -> bindViewData(data));
    }

    private void bindViewData(GroupShopInfoResult bean) {
        ImageLoadUtils.loadImage(bean.getShop_img(), mBinding.ivShopImg);
        mBinding.tvShopName.setText(bean.getShop_name());
        mBinding.tvAddress.setText("地址：" + bean.getDistrict() + bean.getAddress());
        mBinding.tvPhone.setText("电话：" + bean.getMobile());

        String cateids[] = bean.getCate_id().split(",");
        for (String cateid : cateids) {

        }
        Bitmap bitmap = QRCodeTools.createCode(bean.getId() + "", 300, false);
        mBinding.imageView.setImageBitmap(bitmap);
    }
}
