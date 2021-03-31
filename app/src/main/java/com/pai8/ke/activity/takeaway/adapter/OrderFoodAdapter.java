package com.pai8.ke.activity.takeaway.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.entity.FoodGoodInfo;
import com.pai8.ke.activity.takeaway.entity.OrderListResult;
import com.pai8.ke.utils.ImageLoadUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class OrderFoodAdapter extends BaseQuickAdapter<OrderListResult.Goods_info, BaseViewHolder> {
    public OrderFoodAdapter(@Nullable List<OrderListResult.Goods_info> data) {
        super(R.layout.item_order_food, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, OrderListResult.Goods_info item) {


        helper.setText(R.id.tv_name,item.getTitle());
     //   ImageLoadUtils.setCircularImage(mContext,item.cover,helper.getView(R.id.iv_avatar),R.mipmap.ic_launcher);



    }
}
