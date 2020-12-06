package com.pai8.ke.activity.video.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.danikula.videocache.HttpProxyCacheServer;
import com.pai8.ke.R;
import com.pai8.ke.activity.video.VideoControllerView;
import com.pai8.ke.app.MyApp;
import com.pai8.ke.base.BaseRecyclerViewAdapter;
import com.pai8.ke.base.BaseViewHolder;
import com.pai8.ke.entity.resp.VideoResp;
import com.pai8.ke.utils.ImageLoadUtils;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class VideoDetailAdapter extends BaseRecyclerViewAdapter<VideoResp> {

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
        VideoResp videoEntity = mDataList.get(position);

        viewHolder.videoControllerView.setVideoData(videoEntity);

        ImageLoadUtils.loadPicsFitWidth(mContext, videoEntity.getCover_path(), viewHolder.ivCover);

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
