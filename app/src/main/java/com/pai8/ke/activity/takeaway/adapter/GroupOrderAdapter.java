package com.pai8.ke.activity.takeaway.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lhs.library.base.BaseRecyclerViewAdapter;
import com.lhs.library.base.BaseViewHolder;
import com.pai8.ke.activity.takeaway.entity.OrderListResult;
import com.pai8.ke.databinding.ItemGroupOrderBinding;
import com.pai8.ke.utils.ImageLoadUtils;

import java.util.List;

public class GroupOrderAdapter extends BaseRecyclerViewAdapter<OrderListResult> {
    public GroupOrderAdapter(Context context, List<OrderListResult> list) {
        super(context, list);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        ItemGroupOrderBinding binding = ItemGroupOrderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new GroupOrderViewHolder(binding);
    }

    @Override
    protected void onBindNormalViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof GroupOrderViewHolder) {
            GroupOrderViewHolder holder = (GroupOrderViewHolder) viewHolder;
            OrderListResult bean = getItem(position);

            ImageLoadUtils.loadImage(bean.getShop_img(), holder.binding.ivShopLogo);
            holder.binding.tvShopName.setText(bean.getShop_name());
            if (bean.getOrder_status() == 9) {
                holder.binding.tvOrderStatus.setText("已取消");
                holder.binding.tvOrderStatus.setTextColor(Color.parseColor("#ff999999"));
                holder.binding.btnQuXiaoDingDan.setVisibility(View.GONE);
                holder.binding.btnChaKan.setVisibility(View.GONE);
                holder.binding.btnZaiCiGouMai.setVisibility(View.GONE);
                holder.binding.btnLiJiFuKuan.setVisibility(View.GONE);
                holder.binding.btnScan.setVisibility(View.GONE);
                holder.binding.btnEvaluation.setVisibility(View.GONE);
                holder.binding.btnChongXinXiaDan.setVisibility(View.VISIBLE);
            }

            OrderListResult.Goods_info goodInfo = bean.getGoods_info().get(0);
            ImageLoadUtils.loadImage(goodInfo.getCover().get(0), holder.binding.ivProductImg);
            holder.binding.tvProductName.setText(goodInfo.getTitle());
            holder.binding.tvDesc.setText(goodInfo.getDesc());
            holder.binding.tvCount2.setText("X" + bean.getGoods_info().size());
            holder.binding.tvSellPrice.setText("¥" + goodInfo.getSell_price());
            holder.binding.tvOriginPrice.setText("¥" + goodInfo.getOrigin_price());

            holder.binding.tvShiFuPrice.setText("实付：¥" + bean.getOrder_price());
            holder.binding.tvTotalPrice.setText("总价：¥" + bean.getOrder_price());
            holder.binding.tvDiscountPrice.setText("优惠：¥" + bean.getExpress_discount_price());


        }
    }


    class GroupOrderViewHolder extends BaseViewHolder<ItemGroupOrderBinding> {

        public GroupOrderViewHolder(@NonNull ItemGroupOrderBinding viewBinding) {
            super(viewBinding);
        }
    }
}
