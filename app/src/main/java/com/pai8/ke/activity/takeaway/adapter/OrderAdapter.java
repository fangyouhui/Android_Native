package com.pai8.ke.activity.takeaway.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.entity.OrderInfo;
import com.pai8.ke.utils.ImageLoadUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrderAdapter extends BaseQuickAdapter<OrderInfo, BaseViewHolder> {
    public OrderAdapter(@Nullable List<OrderInfo> data) {
        super(R.layout.item_order, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, OrderInfo item) {

        ImageLoadUtils.setCircularImage(mContext,item.shop_img,helper.getView(R.id.iv_store),R.mipmap.ic_launcher);
        helper.setText(R.id.tv_name,item.shop_name);
        helper.setText(R.id.tv_price,"¥"+item.order_price);
        helper.setText(R.id.tv_total,"共"+item.count+"件");
        RecyclerView rvFood = helper.getView(R.id.rv_foods);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rvFood.setLayoutManager(linearLayoutManager);
        OrderFoodAdapter adapter = new OrderFoodAdapter(item.goods_info);
        rvFood.setAdapter(adapter);


    }
}
