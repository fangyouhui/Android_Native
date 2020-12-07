package com.pai8.ke.activity.message.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;
import com.pai8.ke.activity.message.entity.resp.MessageResp;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author Created by zzf
 * @time 11:21
 * Description： 活动消息adapter
 */
public class ActiveMessageAdapter extends BaseQuickAdapter<MessageResp, BaseViewHolder> {

    public ActiveMessageAdapter(@Nullable List<MessageResp> data) {
        super(R.layout.item_active_message, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, MessageResp item) {
        if (item.read_status == 2) {
            holder.setGone(R.id.tv_new,true);
        } else {
            holder.setGone(R.id.tv_new,false);
        }
        holder.setText(R.id.tv_content,item.content);
        holder.setText(R.id.tv_time,item.add_time);
        holder.setText(R.id.tv_title,item.title);
    }
}
