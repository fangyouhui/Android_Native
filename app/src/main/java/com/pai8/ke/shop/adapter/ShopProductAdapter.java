package com.pai8.ke.shop.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lhs.library.base.BaseRecyclerViewAdapter;
import com.lhs.library.base.BaseViewHolder;
import com.pai8.ke.databinding.ItemShopProductBinding;
import com.pai8.ke.entity.ShopTypeResult;
import com.pai8.ke.utils.ImageLoadUtils;

import java.util.List;

public class ShopProductAdapter extends BaseRecyclerViewAdapter<ShopTypeResult> {

    public ShopProductAdapter(Context mContext, List<ShopTypeResult> list, boolean isShowEmptyView) {
        super(mContext, list, isShowEmptyView);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        ItemShopProductBinding commentBinding = ItemShopProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ShopProductViewHolder(commentBinding);
    }

    @Override
    protected void onBindNormalViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ShopProductViewHolder) {
            ShopProductViewHolder holder = (ShopProductViewHolder) viewHolder;
            ShopTypeResult bean = mData.get(position);
            ImageLoadUtils.loadImage(bean.getCover(), holder.binding.imageView);
            holder.binding.tvTitle.setText(bean.getTitle());
            holder.binding.tvGroupBuyPrice.setText("¥" + bean.getSell_price());
            holder.binding.tvOriginPrice.setText("¥" + bean.getOrigin_price());
            holder.binding.tvOriginPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.binding.getRoot().setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onItemClick(bean, position);
                }
            });
        }
    }

    class ShopProductViewHolder extends BaseViewHolder<ItemShopProductBinding> {

        public ShopProductViewHolder(@NonNull ItemShopProductBinding viewbinding) {
            super(viewbinding);
        }
    }
}
