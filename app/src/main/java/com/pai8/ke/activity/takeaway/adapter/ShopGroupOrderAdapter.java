package com.pai8.ke.activity.takeaway.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lhs.library.base.BaseRecyclerViewAdapter;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.entity.FoodGoodInfo;
import com.pai8.ke.activity.takeaway.entity.OrderInfo;
import com.pai8.ke.databinding.ItemShopGroupOrderBinding;
import com.pai8.ke.utils.ImageLoadUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShopGroupOrderAdapter extends BaseRecyclerViewAdapter<OrderInfo> {


    public ShopGroupOrderAdapter(Context context, List<OrderInfo> list) {
        super(context, list);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        ItemShopGroupOrderBinding binding = ItemShopGroupOrderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ShopGroupOrderViewHolder(binding);
    }

    @Override
    protected void onBindNormalViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ShopGroupOrderViewHolder) {
            OrderInfo item = getItem(position);
            ShopGroupOrderViewHolder holder = (ShopGroupOrderViewHolder) viewHolder;
            ImageLoadUtils.loadImage(item.buyer_avatar, holder.binding.buyerAvatar, R.mipmap.img_head_def);

            holder.binding.buyerNickName.setText(item.buyer_name);
            holder.binding.tvOrderStatus.setText("已核销");

            FoodGoodInfo foodGoodInfo = item.goods_info.get(0);
            List<String> video_list = new ArrayList<>(Arrays.asList(foodGoodInfo.cover.split(",")));

            ImageLoadUtils.loadImage(video_list.get(0), holder.binding.ivProductImg);

            holder.binding.tvProductName.setText(foodGoodInfo.title);
            holder.binding.tvDesc.setText("");
            holder.binding.tvCount2.setText("x" + item.count);
            holder.binding.tvSellPrice.setText("");
            holder.binding.tvOriginPrice.setText("");
            holder.binding.tvShiFuPrice.setText("实付:￥" + item.order_price);
            holder.binding.tvTotalPrice.setText("总价:￥" + item.order_price);
            holder.binding.tvDiscountPrice.setText("优惠:￥" + item.order_discount_price);
            holder.binding.getRoot().setOnClickListener(v -> mListener.onItemClick(item, position));


        }
    }

    class ShopGroupOrderViewHolder extends com.lhs.library.base.BaseViewHolder<ItemShopGroupOrderBinding> {

        public ShopGroupOrderViewHolder(@NonNull ItemShopGroupOrderBinding viewBinding) {
            super(viewBinding);
        }
    }
}