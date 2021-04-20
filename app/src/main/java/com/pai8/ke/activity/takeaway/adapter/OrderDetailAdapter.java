package com.pai8.ke.activity.takeaway.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.entity.FoodGoodInfo;
import com.pai8.ke.activity.takeaway.entity.GoodsInfo;
import com.pai8.ke.utils.ImageLoadUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class OrderDetailAdapter extends BaseQuickAdapter<GoodsInfo, BaseViewHolder> {
    public OrderDetailAdapter(@Nullable List<GoodsInfo> data) {
        super(R.layout.item_shop_car, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, GoodsInfo item) {
        ImageLoadUtils.setCircularImage(mContext, item.getCover().get(0), helper.getView(R.id.iv_food), R.mipmap.ic_launcher);


        helper.setText(R.id.tv_food_name, item.getTitle());
        helper.setText(R.id.tv_num, "*" + item.getGoods_num());
        helper.setText(R.id.tv_price, "" + item.getGoods_price());
//        helper.setText(R.id.tv_discount, "" + item.goods_discount);
    }

}
