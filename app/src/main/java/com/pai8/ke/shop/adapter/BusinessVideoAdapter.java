package com.pai8.ke.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lhs.library.base.BaseRecyclerViewAdapter;
import com.lhs.library.base.BaseViewHolder;
import com.pai8.ke.databinding.ItemBusinessVideoBinding;
import com.pai8.ke.entity.Video;
import com.pai8.ke.utils.ImageLoadUtils;

import java.util.List;

public class BusinessVideoAdapter extends BaseRecyclerViewAdapter<Video> {

    public BusinessVideoAdapter(Context mContext, List<Video> list, boolean isShowEmptyView) {
        super(mContext, list, isShowEmptyView);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        ItemBusinessVideoBinding binding = ItemBusinessVideoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new BusinessVideoViewHolder(binding);
    }

    @Override
    protected void onBindNormalViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof BusinessVideoViewHolder) {
            Video bean = getItem(position);
            BusinessVideoViewHolder holder = (BusinessVideoViewHolder) viewHolder;
            ImageLoadUtils.loadImage(bean.getCover_path(), holder.binding.imageView);
            holder.binding.tvEvaluation.setText(bean.getVideo_desc());
            ImageLoadUtils.loadImage(bean.getAvatar(), holder.binding.ivHead);
            holder.binding.tvName.setText(bean.getUser_nickname());
            holder.binding.tvPlayCount.setText(bean.getLike_counts() + "");
            holder.binding.getRoot().setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onItemClick(bean, position);
                }
            });
        }
    }

    class BusinessVideoViewHolder extends BaseViewHolder<ItemBusinessVideoBinding> {
        public BusinessVideoViewHolder(@NonNull ItemBusinessVideoBinding viewBinding) {
            super(viewBinding);
        }
    }

}
