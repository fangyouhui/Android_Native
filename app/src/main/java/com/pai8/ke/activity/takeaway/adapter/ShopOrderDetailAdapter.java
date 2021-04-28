package com.pai8.ke.activity.takeaway.adapter;

import android.graphics.Paint;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.entity.FoodGoodInfo;
import com.pai8.ke.utils.ImageLoadUtils;

import java.util.List;

public class ShopOrderDetailAdapter extends BaseQuickAdapter<FoodGoodInfo, BaseViewHolder> {
    public ShopOrderDetailAdapter(@Nullable List<FoodGoodInfo> data) {
        super(R.layout.item_shop_order, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, FoodGoodInfo item) {
        ImageLoadUtils.setCircularImage(mContext, item.cover, helper.getView(R.id.iv_food), R.mipmap.ic_launcher);
        helper.setText(R.id.tv_food_name, item.title);
        helper.setText(R.id.tv_num, "*" + item.goods_num);
        helper.setText(R.id.tv_price, "¥" + item.goods_price);
        TextView tvDiscount = helper.getView(R.id.tv_discount);

        tvDiscount.setText("¥" + item.goods_origin_price);
        tvDiscount.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

}
