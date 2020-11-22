package com.pai8.ke.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pai8.ke.R;
import com.pai8.ke.activity.video.VideoControllerView;
import com.pai8.ke.base.BaseRecyclerViewAdapter;
import com.pai8.ke.base.BaseViewHolder;
import com.pai8.ke.entity.resp.VideoResp;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.utils.StringUtils;
import com.pai8.ke.widget.CircleImageView;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class HomeAdapter extends BaseRecyclerViewAdapter<VideoResp> {

    private boolean isNearby;

    public void setNearby(boolean nearby) {
        isNearby = nearby;
    }

    public HomeAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_home_video, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        VideoResp video = mDataList.get(position);
        ImageLoadUtils.loadImage(mContext, video.getCover_path(), viewHolder.ivCover, R.color.colorPrimary);
        viewHolder.tvLookCount.setText(video.getLook_counts() + "");
        viewHolder.tvName.setText(video.getUser_nickname());
        ImageLoadUtils.loadImage(mContext, video.getAvatar(), viewHolder.civAvatar, R.mipmap.img_head_def);
        viewHolder.tvTitle.setText(video.getVideo_desc());
        if (isNearby && StringUtils.isNotEmpty(video.getDistance())) {
            viewHolder.tvTagDistance.setVisibility(View.VISIBLE);
            viewHolder.tvTagDistance.setText(video.getDistance());
        } else {
            viewHolder.tvTagDistance.setVisibility(View.GONE);
        }
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_cover)
        ImageView ivCover;
        @BindView(R.id.ibtn_play)
        ImageButton ibtnPlay;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.civ_avatar)
        CircleImageView civAvatar;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_look_count)
        TextView tvLookCount;
        @BindView(R.id.tv_tag_dist)
        TextView tvTagDistance;

        public ViewHolder(View itemView) {
            super(itemView);
        }

    }
}
