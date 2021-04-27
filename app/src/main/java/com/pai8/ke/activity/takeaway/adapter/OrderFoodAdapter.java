package com.pai8.ke.activity.takeaway.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.entity.FoodGoodInfo;
import com.pai8.ke.utils.ImageLoadUtils;

import java.util.List;

public class OrderFoodAdapter extends BaseQuickAdapter<FoodGoodInfo, BaseViewHolder> {
    public OrderFoodAdapter(@Nullable List<FoodGoodInfo> data) {
        super(R.layout.item_order_food, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, FoodGoodInfo item) {
        helper.setText(R.id.tv_name, item.title);
        //   ImageLoadUtils.setCircularImage(mContext, item.cover, helper.getView(R.id.iv_avatar), R.mipmap.ic_launcher);
        ImageLoadUtils.loadImage(item.cover, helper.getView(R.id.iv_avatar));
    }
}
