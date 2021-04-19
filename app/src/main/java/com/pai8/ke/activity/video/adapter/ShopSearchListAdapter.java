package com.pai8.ke.activity.video.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lhs.library.base.BaseRecyclerViewAdapter;
import com.lhs.library.base.BaseViewHolder;
import com.pai8.ke.R;
import com.pai8.ke.databinding.ItemShopSearchListBinding;
import com.pai8.ke.entity.resp.ShopList;
import com.pai8.ke.utils.ImageLoadUtils;

import java.util.List;

public class ShopSearchListAdapter extends BaseRecyclerViewAdapter<ShopList> {


    public ShopSearchListAdapter(Context context, List<ShopList> list) {
        super(context, list);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        ItemShopSearchListBinding binding = ItemShopSearchListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new ItemViewHolder(binding);
    }

    @Override
    protected void onBindNormalViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ItemViewHolder) {
            ItemViewHolder holder = (ItemViewHolder) viewHolder;
            ShopList bean = getItem(position);
            ImageLoadUtils.loadImage(mContext, bean.getShop_img(), holder.binding.civCover, R.mipmap.ic_shop_def_react);
            holder.binding.tvShopName.setText(bean.getShop_name());
            holder.binding.tvShopAddress.setText(bean.getAddress());
            holder.binding.getRoot().setOnClickListener(v -> mListener.onItemClick(bean, position));
        }

    }

    static class ItemViewHolder extends BaseViewHolder<ItemShopSearchListBinding> {


        public ItemViewHolder(@NonNull ItemShopSearchListBinding viewBinding) {
            super(viewBinding);
        }
    }


}

