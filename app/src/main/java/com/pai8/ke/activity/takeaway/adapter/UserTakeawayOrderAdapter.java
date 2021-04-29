package com.pai8.ke.activity.takeaway.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lhs.library.base.BaseRecyclerViewAdapter;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.entity.OrderListResult;
import com.pai8.ke.databinding.ItemUserTakeawayOrderBinding;
import com.pai8.ke.utils.ImageLoadUtils;

import java.util.List;

public class UserTakeawayOrderAdapter extends BaseRecyclerViewAdapter<OrderListResult> {


    public UserTakeawayOrderAdapter(Context context, List<OrderListResult> list) {
        super(context, list);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        ItemUserTakeawayOrderBinding binding = ItemUserTakeawayOrderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new UserTakeawayOrderViewHolder(binding);
    }

    @Override
    protected void onBindNormalViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof UserTakeawayOrderViewHolder) {
            OrderListResult item = getItem(position);
            UserTakeawayOrderViewHolder holder = (UserTakeawayOrderViewHolder) viewHolder;

            //订单状态 0为待支付 1为已支付 2为商家已接单 7为订单制作完成 3为配送中 4为订单已完成 5为订单已申请退款 6订单被拒绝退款 8为订单已退款 9为订单已取消 -1为支付超时 -2订单拒绝接单
            holder.binding.tvReject.setVisibility(View.GONE);
            holder.binding.tvCancel.setVisibility(View.GONE);
            if (item.getOrder_status() == 0) {
                holder.binding.tvStatus.setText("待支付");
                holder.binding.tvCancel.setVisibility(View.VISIBLE);
                holder.binding.tvCancel.setText("取消订单");
                holder.binding.tvFoodStatus.setText("立即支付");
            } else if (item.getOrder_status() == 1) {
                holder.binding.tvStatus.setText("待商家接单");
                holder.binding.tvCancel.setVisibility(View.VISIBLE);
                holder.binding.tvCancel.setText("取消订单");
                holder.binding.tvFoodStatus.setText("联系商家");
            } else if (item.getOrder_status() == 2) {
                holder.binding.tvStatus.setText("商品准备中");
                holder.binding.tvCancel.setVisibility(View.GONE);
                holder.binding.tvFoodStatus.setVisibility(View.GONE);
            } else if (item.getOrder_status() == 3) {
                holder.binding.tvStatus.setText("商品配送中");
                holder.binding.tvCancel.setVisibility(View.GONE);
                holder.binding.tvFoodStatus.setVisibility(View.GONE);
            } else if (item.getOrder_status() == 4) {
                holder.binding.tvStatus.setText("订单已完成");
                holder.binding.tvCancel.setText("再来一单");
                holder.binding.tvFoodStatus.setText("立即评价");
            } else if (item.getOrder_status() == 5) {
                holder.binding.tvStatus.setText("退款申请中");
                holder.binding.tvCancel.setVisibility(View.GONE);
                holder.binding.tvFoodStatus.setVisibility(View.GONE);
            } else if (item.getOrder_status() == 6) {
                holder.binding.tvStatus.setText("拒绝退款");
                holder.binding.tvCancel.setVisibility(View.GONE);
                holder.binding.tvFoodStatus.setVisibility(View.GONE);
            } else if (item.getOrder_status() == 7) {
                holder.binding.tvStatus.setText("商品制作完成");
                holder.binding.tvCancel.setVisibility(View.GONE);
                holder.binding.tvFoodStatus.setVisibility(View.GONE);
            } else if (item.getOrder_status() == 8) {
                holder.binding.tvStatus.setText("订单已退款");
                holder.binding.tvCancel.setVisibility(View.GONE);
                holder.binding.tvFoodStatus.setVisibility(View.GONE);
            } else if (item.getOrder_status() == 9) {
                holder.binding.tvStatus.setText("订单已取消");
                holder.binding.tvCancel.setVisibility(View.GONE);
                holder.binding.tvFoodStatus.setText("重新下单");
            } else if (item.getOrder_status() == -1) {
                holder.binding.tvStatus.setText("订单超时");
                holder.binding.tvCancel.setVisibility(View.GONE);
                holder.binding.tvFoodStatus.setVisibility(View.GONE);
            } else if (item.getOrder_status() == -2) {
                holder.binding.tvStatus.setText("商家拒绝接单");
                holder.binding.tvCancel.setVisibility(View.GONE);
                holder.binding.tvFoodStatus.setVisibility(View.GONE);
            }

            ImageLoadUtils.loadImage(item.getShop_img(), holder.binding.shopImg);
            holder.binding.shopName.setText(item.getShop_name());
            holder.binding.tvPrice.setText("¥" + item.getOrder_price());
            holder.binding.tvTotal.setText("共" + item.getCount() + "件");
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            holder.binding.recyclerView.setLayoutManager(linearLayoutManager);

            holder.binding.getRoot().setOnClickListener(v -> itemChildClickListener.onItemClick(item, position));
            holder.binding.tvCancel.setOnClickListener(v -> itemChildClickListener.onItemChildCancelClick(item, position));
            holder.binding.tvReject.setOnClickListener(v -> itemChildClickListener.onItemChildRejectClick(item, position));
            holder.binding.recyclerView.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //响应父rv的item的点击事件
                    holder.itemView.performClick();
                }
                return false;
            });

//                if (item.getGoods_info().size() > 3)
//                    item.getGoods_info().subList(0, 2);
            UserTakeawayOrderFoodAdapter adapter = new UserTakeawayOrderFoodAdapter(mContext, item.getGoods_info());
            holder.binding.recyclerView.setAdapter(adapter);
        }

    }

    class UserTakeawayOrderViewHolder extends com.lhs.library.base.BaseViewHolder<ItemUserTakeawayOrderBinding> {

        public UserTakeawayOrderViewHolder(@NonNull ItemUserTakeawayOrderBinding viewBinding) {
            super(viewBinding);
        }
    }

    private ItemChildClickListener itemChildClickListener;

    public void setItemChildClickListener(ItemChildClickListener itemChildClickListener) {
        this.itemChildClickListener = itemChildClickListener;
    }

    public interface ItemChildClickListener extends BaseItemTouchListener<OrderListResult> {
        void onItemChildCancelClick(OrderListResult item, int position);

        void onItemChildRejectClick(OrderListResult item, int position);
    }
}
