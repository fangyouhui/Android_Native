package com.pai8.ke.activity.takeaway.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lhs.library.base.BaseRecyclerViewAdapter;
import com.lhs.library.base.BaseViewHolder;
import com.pai8.ke.databinding.ItemCategoryBinding;
import com.pai8.ke.entity.BusinessTypeResult;

import java.util.List;

public class CategoryAdapter extends BaseRecyclerViewAdapter<BusinessTypeResult> {
    public CategoryAdapter(Context context, List<BusinessTypeResult> list) {
        super(context, list, false);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        ItemCategoryBinding binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CategoryViewHolder(binding);
    }

    @Override
    protected void onBindNormalViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof CategoryViewHolder) {
            CategoryViewHolder holder = (CategoryViewHolder) viewHolder;
            BusinessTypeResult bean = getItem(position);
            holder.binding.tvTypeName.setText(bean.getType_name());
            holder.binding.tvTypeName.setSelected(bean.isSelected());
            holder.binding.tvTypeName.setOnClickListener(v -> {
                bean.setSelected(!v.isSelected());
                notifyDataSetChanged();
            });
        }
    }

    class CategoryViewHolder extends BaseViewHolder<ItemCategoryBinding> {
        public CategoryViewHolder(@NonNull ItemCategoryBinding viewBinding) {
            super(viewBinding);
        }
    }

}
