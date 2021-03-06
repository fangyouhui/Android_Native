package com.pai8.ke.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lhs.library.base.BaseRecyclerViewAdapter;
import com.lhs.library.base.BaseViewHolder;
import com.pai8.ke.databinding.ItemProductDetailImgBinding;
import com.pai8.ke.utils.GlideHelper;

import java.util.List;

public class ProductImgDetailAdapter extends BaseRecyclerViewAdapter<String> {
    public ProductImgDetailAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        ItemProductDetailImgBinding binding = ItemProductDetailImgBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ProductImgDetailViewHolder(binding);
    }

    @Override
    protected void onBindNormalViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ProductImgDetailViewHolder) {
            ProductImgDetailViewHolder holder = (ProductImgDetailViewHolder) viewHolder;
            //   ImageLoadUtils.loadImage(mData.get(position), holder.binding.imageView);
//            Glide.with(holder.binding.imageView.getContext())
//                    .load(mData.get(position))
//                    .error(R.color.colorPrimary)
//                    .into(holder.binding.imageView);
            //    ImageLoadUtils.loadImage(mData.get(position),holder.binding.imageView,R.color.colorPrimary);
            GlideHelper.loadImageWithNormal(mData.get(position), holder.binding.imageView);
        }
    }

    class ProductImgDetailViewHolder extends BaseViewHolder<ItemProductDetailImgBinding> {
        public ProductImgDetailViewHolder(@NonNull ItemProductDetailImgBinding viewBinding) {
            super(viewBinding);
        }
    }

}
