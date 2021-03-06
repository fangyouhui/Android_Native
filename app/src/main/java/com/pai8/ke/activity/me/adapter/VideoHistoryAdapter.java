package com.pai8.ke.activity.me.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;
import com.pai8.ke.entity.Video;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.utils.StringUtils;

import android.view.View;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author Created by zzf
 * @time 11:21
 * Description： 足迹adapter
 */
public class VideoHistoryAdapter extends BaseQuickAdapter<Video, BaseViewHolder> {

    public VideoHistoryAdapter(@Nullable List<Video> data) {
        super(R.layout.item_video_history,data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, Video item) {
        ImageLoadUtils.loadImage(mContext, item.getCover_path(), holder.getView(R.id.iv_cover), R.color.colorPrimary);
        holder.setText(R.id.tv_look_count,item.getLook_counts() + "");
        if(item.getUser() != null){
            holder.setText(R.id.tv_name,item.getUser().getNickname());
            ImageLoadUtils.loadImage(mContext, item.getUser().getAvatar(), holder.getView(R.id.civ_avatar),
                    R.mipmap.img_head_def);
        }else {
            holder.setText(R.id.tv_name,"暂无");
            holder.setImageResource(R.id.civ_avatar, R.mipmap.img_head_def);
        }
        holder.setText(R.id.tv_title,item.getVideo_desc());

        if (StringUtils.isNotEmpty(item.getDistance())) {
            holder.setGone(R.id.tv_tag_dist,true);
            holder.setText(R.id.tv_tag_dist,item.getDistance());
        } else {
            holder.setGone(R.id.tv_tag_dist,false);
        }
        if (item.getShop() == null) {
            holder.setGone(R.id.tv_tag_onsale,false);
        } else {
            holder.setGone(R.id.tv_tag_onsale,true);
        }
    }

}
