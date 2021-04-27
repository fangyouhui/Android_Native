package com.pai8.ke.activity.takeaway.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lhs.library.base.BaseRecyclerViewAdapter;
import com.pai8.ke.activity.takeaway.entity.req.OrderStatusInfo;
import com.pai8.ke.databinding.ItemOrderStatusBinding;

import java.util.List;

public class OrderStatusAdapter extends BaseRecyclerViewAdapter<OrderStatusInfo> {

    public OrderStatusAdapter(Context context, List<OrderStatusInfo> list) {
        super(context, list);
    }


    @Override
    protected RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        ItemOrderStatusBinding binding = ItemOrderStatusBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new OrderStatusViewHolder(binding);
    }

    @Override
    protected void onBindNormalViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof OrderStatusViewHolder) {
            OrderStatusViewHolder holder = (OrderStatusViewHolder) viewHolder;
            OrderStatusInfo bean = getItem(position);
            holder.binding.tvContent.setText(bean.name);
            holder.binding.tvContent.setSelected(bean.isSelect);
            holder.binding.tvContent.setTextColor(Color.parseColor(bean.isSelect ? "#FF7F47" : "#111111"));
            holder.binding.tvContent.setOnClickListener(v -> {
                bean.isSelect = !bean.isSelect;
                notifyDataSetChanged();
            });
        }

    }

    class OrderStatusViewHolder extends com.lhs.library.base.BaseViewHolder<ItemOrderStatusBinding> {

        public OrderStatusViewHolder(@NonNull ItemOrderStatusBinding viewBinding) {
            super(viewBinding);
        }
    }
}
