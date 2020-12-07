package com.pai8.ke.activity.message.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;
import com.pai8.ke.activity.message.entity.resp.MessageResp;
import com.pai8.ke.entity.resp.CommentResp;
import com.pai8.ke.utils.DateUtils;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.widget.CircleImageView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Created by zzf
 * @time 11:21
 * Description： 评论adapter
 */
public class CommentAdapter extends BaseQuickAdapter<MessageResp, BaseViewHolder> {

    public CommentAdapter(@Nullable List<MessageResp> data) {
        super(R.layout.item_message_comment, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, MessageResp item) {
        holder.setText(R.id.tv_time, DateUtils.millisToTime(DateUtils.FORMAT_YYYY_MM_DD_HHMMSS,item.add_time));
        holder.setText(R.id.tv_title,item.title);
        holder.setText(R.id.tv_comment_content,item.content);
        ImageLoadUtils.loadImage(mContext, item.avatar, holder.getView(R.id.civ_head), R.mipmap.img_head_def);
        ImageLoadUtils.loadImage(mContext, item.video_cover, holder.getView(R.id.civ_works_img), R.mipmap.img_head_def);
    }

}
