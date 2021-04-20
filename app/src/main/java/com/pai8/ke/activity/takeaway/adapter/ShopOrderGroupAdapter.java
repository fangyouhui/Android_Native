package com.pai8.ke.activity.takeaway.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.entity.FoodGoodInfo;
import com.pai8.ke.activity.takeaway.entity.OrderInfo;
import com.pai8.ke.utils.ImageLoadUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShopOrderGroupAdapter extends BaseQuickAdapter<OrderInfo, BaseViewHolder> {
    public ShopOrderGroupAdapter(@Nullable List<OrderInfo> data) {
        super(R.layout.item_group_shop_order, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, OrderInfo item) {

        ImageLoadUtils.setCircularImage(mContext, item.buyer_avatar, helper.getView(R.id.ivShopLogo), R.mipmap.ic_launcher);

        helper.setText(R.id.tvShopName, item.shop_name);
        helper.setText(R.id.tvOrderStatus, "已核销");
        FoodGoodInfo foodGoodInfo = item.goods_info.get(0);
        List<String> video_list = new ArrayList<String>(Arrays.asList(foodGoodInfo.cover.split(",")));

        ImageLoadUtils.setCircularImage(mContext, video_list.get(0), helper.getView(R.id.ivProductImg), R.mipmap.ic_launcher);

        helper.setText(R.id.tvProductName, foodGoodInfo.title);
        helper.setText(R.id.tvDesc, "");
        helper.setText(R.id.tvCount2, "x" + item.count);

        helper.setText(R.id.tvSellPrice, "");
        helper.setText(R.id.tvOriginPrice, "");
        helper.setText(R.id.tvShiFuPrice, "实付:￥" + item.order_price);
        helper.setText(R.id.tvTotalPrice, "总价:￥" + item.order_price);
        helper.setText(R.id.tvDiscountPrice, "优惠:￥" + item.order_discount_price);


    }
}