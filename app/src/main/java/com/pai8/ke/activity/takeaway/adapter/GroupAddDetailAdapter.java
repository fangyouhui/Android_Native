package com.pai8.ke.activity.takeaway.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lhs.library.base.BaseRecyclerViewAdapter;
import com.lhs.library.base.BaseViewHolder;
import com.luck.picture.lib.entity.LocalMedia;
import com.pai8.ke.R;
import com.pai8.ke.databinding.ItemGroupImgBinding;
import com.pai8.ke.utils.ImageLoadUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GroupAddDetailAdapter extends BaseRecyclerViewAdapter<LocalMedia> {

    public GroupAddDetailAdapter(Context context, List<LocalMedia> list) {
        super(context, list, false);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        ItemGroupImgBinding binding = ItemGroupImgBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new GroupBannerHolder(binding);
    }

    @Override
    protected void onBindNormalViewHolder(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof GroupBannerHolder) {
            GroupBannerHolder holder = (GroupBannerHolder) viewHolder;
            LocalMedia bean = getItem(position);
            ImageLoadUtils.setCircularImage(mContext, bean.getRealPath(), holder.binding.imageView, R.mipmap.img_share_cover);
            holder.binding.ivDel.setOnClickListener(v -> {
                mListener.onItemClick(bean, position);
            });
        }
    }

    class GroupBannerHolder extends BaseViewHolder<ItemGroupImgBinding> {

        public GroupBannerHolder(@NonNull @NotNull ItemGroupImgBinding viewBinding) {
            super(viewBinding);
        }
    }
}


