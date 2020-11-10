package com.pai8.ke.activity.takeaway.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.entity.FoodGoodInfo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MerchantFoodGoodAdapter extends BaseQuickAdapter<FoodGoodInfo, BaseViewHolder> {
    public MerchantFoodGoodAdapter(@Nullable List<FoodGoodInfo> data) {
        super(R.layout.item_store_goods, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, FoodGoodInfo item) {

    }
}
