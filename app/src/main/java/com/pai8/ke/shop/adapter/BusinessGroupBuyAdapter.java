package com.pai8.ke.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lhs.library.base.BaseRecyclerViewAdapter;
import com.lhs.library.base.BaseViewHolder;
import com.pai8.ke.databinding.ItemBusinessGroupBuyBinding;
import com.pai8.ke.entity.ShopGroupListResult;
import com.pai8.ke.utils.ImageLoadUtils;

import java.util.List;

public class BusinessGroupBuyAdapter extends BaseRecyclerViewAdapter<ShopGroupListResult> {

    public BusinessGroupBuyAdapter(Context mContext, List<ShopGroupListResult> list, boolean isShowEmptyView) {
        super(mContext, list, isShowEmptyView);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        ItemBusinessGroupBuyBinding binding = ItemBusinessGroupBuyBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new BusinessGroupBuyViewHolder(binding);
    }

    @Override
    protected void onBindNormalViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof BusinessGroupBuyViewHolder) {
            ShopGroupListResult bean = getItem(position);
            BusinessGroupBuyViewHolder holder = (BusinessGroupBuyViewHolder) viewHolder;
            ImageLoadUtils.loadImage(bean.getCover(), holder.binding.imageView);
            holder.binding.tvTitle.setText(bean.getTitle());
            holder.binding.tvGroupBuyPrice.setText("¥" + bean.getSell_price());
            holder.binding.tvOriginPrice.setText("¥" + bean.getOrigin_price());
            holder.binding.getRoot().setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onItemClick(bean, position);
                }
            });
        }
    }

    class BusinessGroupBuyViewHolder extends BaseViewHolder<ItemBusinessGroupBuyBinding> {
        public BusinessGroupBuyViewHolder(@NonNull ItemBusinessGroupBuyBinding viewBinding) {
            super(viewBinding);
        }
    }

}
