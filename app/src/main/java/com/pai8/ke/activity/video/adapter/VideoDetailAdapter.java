package com.pai8.ke.activity.video.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pai8.ke.R;
import com.pai8.ke.activity.video.VideoControllerView;
import com.pai8.ke.base.BaseRecyclerViewAdapter;
import com.pai8.ke.base.BaseViewHolder;
import com.pai8.ke.entity.resp.VideoEntity;
import com.pai8.ke.utils.ImageLoadUtils;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class VideoDetailAdapter extends BaseRecyclerViewAdapter<VideoEntity> {

    public VideoDetailAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_video, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        VideoEntity videoEntity = mDataList.get(position);
        viewHolder.videoControllerView.setVideoData(videoEntity);
        ImageLoadUtils.loadImageFitCenter(mContext, videoEntity.getCover_path(), viewHolder.ivCover,
                R.color.color_black);
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_cover)
        ImageView ivCover;
        @BindView(R.id.video_controller_view)
        VideoControllerView videoControllerView;

        public ViewHolder(View itemView) {
            super(itemView);
        }

    }
}
