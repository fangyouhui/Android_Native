package com.pai8.ke.activity.takeaway.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.entity.FoodGoodInfo;
import com.pai8.ke.utils.ImageLoadUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ConfirmOrderAdapter extends BaseQuickAdapter<FoodGoodInfo, BaseViewHolder> {
    public ConfirmOrderAdapter(@Nullable List<FoodGoodInfo> data) {
        super(R.layout.item_confirm_order, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, FoodGoodInfo item) {
        ImageLoadUtils.setCircularImage(mContext, item.cover, helper.getView(R.id.iv_food), R.mipmap.ic_launcher);

        helper.setText(R.id.tv_food_name,item.title);
        helper.setText(R.id.tv_num,"*"+item.goods_num);
        helper.setText(R.id.tv_price,""+item.sell_price);
        helper.setText(R.id.tv_discount,""+item.discount);
    }
}
