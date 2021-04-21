package com.pai8.ke.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lhs.library.base.BaseRecyclerViewAdapter;
import com.lhs.library.base.BaseViewHolder;
import com.pai8.ke.R;
import com.pai8.ke.databinding.ItemHomeVideoBinding;
import com.pai8.ke.entity.Video;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.utils.StringUtils;

import java.util.List;

public class HomeAdapter extends BaseRecyclerViewAdapter<Video> {

    public HomeAdapter(Context context, List<Video> list) {
        super(context, list, false);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        ItemHomeVideoBinding binding = ItemHomeVideoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new VideoViewHolder(binding);
    }

    @Override
    protected void onBindNormalViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof VideoViewHolder) {
            Video video = mData.get(position);
            VideoViewHolder holder = (VideoViewHolder) viewHolder;
            ImageLoadUtils.loadImage(video.getCover_path(), holder.binding.ivCover);
            holder.binding.tvLookCount.setText(video.getLook_counts() + "");
            if (video.getUser() != null) {
                holder.binding.tvName.setText(video.getUser().getNickname());
                ImageLoadUtils.loadImage(mContext, video.getUser().getAvatar(), holder.binding.civAvatar, R.mipmap.img_head_def);
            }
            holder.binding.tvTitle.setText(video.getVideo_desc());
            if (StringUtils.isNotEmpty(video.getDistance())) {
                holder.binding.tvTagDist.setVisibility(View.VISIBLE);
                holder.binding.tvTagDist.setText(video.getDistance());
            } else {
                holder.binding.tvTagDist.setVisibility(View.GONE);
            }
            if (video.getShop() == null) {
                holder.binding.tvTagOnsale.setVisibility(View.GONE);
            } else {
                holder.binding.tvTagOnsale.setVisibility(View.VISIBLE);
            }
            holder.binding.tvType.setText(video.getShop_type());

        }

    }

    static class VideoViewHolder extends BaseViewHolder<ItemHomeVideoBinding> {
        public VideoViewHolder(@NonNull ItemHomeVideoBinding viewBinding) {
            super(viewBinding);
        }
    }
}
