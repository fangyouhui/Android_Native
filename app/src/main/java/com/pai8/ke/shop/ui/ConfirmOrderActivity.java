package com.pai8.ke.shop.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;

import com.blankj.utilcode.util.GsonUtils;
import com.google.gson.Gson;
import com.lhs.library.base.BaseActivity;
import com.pai8.ke.activity.me.CouponListActivity;
import com.pai8.ke.activity.takeaway.entity.FoodGoodInfo;
import com.pai8.ke.activity.takeaway.entity.OrderGoodInfo;
import com.pai8.ke.activity.takeaway.entity.resq.StoreInfo;
import com.pai8.ke.databinding.ActivityConfirmOrderBinding;
import com.pai8.ke.shop.viewmodel.ConfirmOrderViewModel;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ConfirmOrderActivity extends BaseActivity<ConfirmOrderViewModel, ActivityConfirmOrderBinding> {
    private StoreInfo mStoreInfo;
    private List<FoodGoodInfo> mFoodInfoList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   mStoreInfo = (StoreInfo) getIntent().getSerializableExtra("storeInfo");
        //   mFoodInfoList = (List<FoodGoodInfo>) getIntent().getSerializableExtra("shopCar");
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
    }

    private void pay() {
        List<OrderGoodInfo> goodList = new ArrayList<>();
        for (int i = 0; i < mFoodInfoList.size(); i++) {
            OrderGoodInfo goodInfo = new OrderGoodInfo();
            goodInfo.goods_id = mFoodInfoList.get(i).id;
            goodInfo.goods_num = mFoodInfoList.get(i).goods_num;
            goodInfo.goods_price = mFoodInfoList.get(i).sell_price;
            goodList.add(goodInfo);
        }
        String json = GsonUtils.toJson(goodList);
    }


}
