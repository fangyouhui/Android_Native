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
import com.pai8.ke.activity.takeaway.entity.OrderInfo;
import com.pai8.ke.databinding.ItemShopTakeawayOrderBinding;
import com.pai8.ke.utils.ImageLoadUtils;

import java.util.List;

public class ShopTakeawayOrderAdapter extends BaseRecyclerViewAdapter<OrderInfo> {

    public ShopTakeawayOrderAdapter(Context context, List<OrderInfo> list) {
        super(context, list);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        ItemShopTakeawayOrderBinding binding = ItemShopTakeawayOrderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ShopTakeawayOrderViewHolder(binding);
    }

    @Override
    protected void onBindNormalViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ShopTakeawayOrderViewHolder) {
            OrderInfo item = getItem(position);
            ShopTakeawayOrderViewHolder holder = (ShopTakeawayOrderViewHolder) viewHolder;
            // 0为待支付 1为已支付 2为商家已接单 7为订单制作完成 3为配送中 4为订单已完成 5为订单已申请退款 6订单被拒绝退款 8为订单已退款 9为订单已取消 -1为支付超时 -2订单拒绝接单
            //订单状态 0为待支付 1为已支付 2为商家已接单 3为配送中 4为订单已完成 5为订单已申请退款 6订单被拒绝退款 8为订单已退款 9为订单已取消 -1为支付超时 -2订单拒绝接单

            if (item.order_status == 0) {
                holder.binding.tvStatus.setText("待支付");
                //holder.binding.tvCancel.setVisibility(View.VISIBLE);
            } else if (item.order_status == 1) {
                holder.binding.tvStatus.setText("待接单");
                holder.binding.btnRight.setText("立即接单");
                holder.binding.btnLeft.setText("拒绝接单");
                holder.binding.btnLeft.setVisibility(View.VISIBLE);
                holder.binding.btnRight.setVisibility(View.VISIBLE);
            } else if (item.order_status == 2) {
                holder.binding.tvStatus.setText("已接单");
                holder.binding.btnRight.setText("商品制作完成");
                holder.binding.btnRight.setVisibility(View.VISIBLE);
                holder.binding.btnLeft.setVisibility(View.GONE);
            } else if (item.order_status == 3) {
                holder.binding.tvStatus.setText("商品配送中");
                holder.binding.btnRight.setVisibility(View.GONE);
                holder.binding.btnLeft.setVisibility(View.GONE);
            } else if (item.order_status == 4) {
                holder.binding.tvStatus.setText("订单已完成");
                holder.binding.btnLeft.setVisibility(View.GONE);
                holder.binding.btnRight.setVisibility(View.GONE);
            } else if (item.order_status == 5) {
                holder.binding.tvStatus.setText("申请退款");
                holder.binding.btnRight.setText("同意退款");
                holder.binding.btnLeft.setText("拒绝退款");
                holder.binding.btnRight.setVisibility(View.VISIBLE);
                holder.binding.btnLeft.setVisibility(View.VISIBLE);
            } else if (item.order_status == 6) {
                holder.binding.tvStatus.setText("拒绝退款");
                holder.binding.btnRight.setVisibility(View.GONE);
                holder.binding.btnLeft.setVisibility(View.GONE);
            } else if (item.order_status == 7) {
                holder.binding.tvStatus.setText("待配送");
                holder.binding.btnRight.setText("送出商品");
                holder.binding.btnRight.setVisibility(View.VISIBLE);
                holder.binding.btnLeft.setVisibility(View.GONE);
            } else if (item.order_status == 8) {
                holder.binding.tvStatus.setText("订单已退款");
                holder.binding.btnRight.setVisibility(View.GONE);
                holder.binding.btnLeft.setVisibility(View.GONE);
            } else if (item.order_status == 9) {
                holder.binding.tvStatus.setText("订单已取消");
                holder.binding.btnRight.setVisibility(View.GONE);
                holder.binding.btnLeft.setVisibility(View.GONE);
            } else if (item.order_status == -1) {
                holder.binding.tvStatus.setText("订单超时");
                holder.binding.btnRight.setVisibility(View.GONE);
                holder.binding.btnLeft.setVisibility(View.GONE);
            } else if (item.order_status == -2) {
                holder.binding.tvStatus.setText("商家拒绝接单");
                holder.binding.btnLeft.setVisibility(View.GONE);
                holder.binding.btnRight.setVisibility(View.GONE);
            }

            //    ImageLoadUtils.setCircularImage(mContext, item.shop_img, holder.binding.ivStore, R.mipmap.ic_launcher);
            ImageLoadUtils.loadImage(item.buyer_avatar, holder.binding.buyerAvatar, R.mipmap.img_head_def);
            holder.binding.buyerNickName.setText(item.buyer_nickname);
            holder.binding.tvPrice.setText("¥" + item.order_price);
            holder.binding.tvTotal.setText("共" + item.count + "件");
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            holder.binding.rvFoods.setLayoutManager(linearLayoutManager);
            OrderFoodAdapter adapter = new OrderFoodAdapter(item.goods_info);
            holder.binding.rvFoods.setAdapter(adapter);


            holder.binding.getRoot().setOnClickListener(v -> itemListener.onItemClick(item, position));
            holder.binding.btnRight.setOnClickListener(v -> itemListener.onRightListener(item, position));
            holder.binding.btnLeft.setOnClickListener(v -> itemListener.onLeftListener(item, position));
            holder.binding.rvFoods.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //响应父rv的item的点击事件
                    holder.itemView.performClick();
                }

                return false;
            });
        }
    }

    class ShopTakeawayOrderViewHolder extends com.lhs.library.base.BaseViewHolder<ItemShopTakeawayOrderBinding> {

        public ShopTakeawayOrderViewHolder(@NonNull ItemShopTakeawayOrderBinding viewBinding) {
            super(viewBinding);
        }
    }

    private ItemListener itemListener;

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public interface ItemListener extends BaseItemTouchListener<OrderInfo> {
        void onRightListener(OrderInfo item, int position);

        void onLeftListener(OrderInfo item, int position);
    }


}
