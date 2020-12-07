package com.pai8.ke.activity.message.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;
import com.pai8.ke.activity.message.entity.resp.MessageResp;
import com.pai8.ke.utils.DateUtils;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.utils.StringUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author Created by zzf
 * @time 11:21
 * Description： 一对一聊天记录adapter
 */
public class ChatRecordAdapter extends BaseQuickAdapter<MessageResp, BaseViewHolder> {

    public ChatRecordAdapter(@Nullable List<MessageResp> data) {
        super(R.layout.item_chat_record,data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, MessageResp item) {
        if ("1".equals(item.value)) {
//            viewHolder.tvChatType.setText("已接听");
            holder.setTextColor(R.id.tv_chat_type,mContext.getResources().getColor(R.color.color_light_font));
        } else if ("2".equals(item.value)) {
//            viewHolder.tvChatType.setText("未接听");
            holder.setTextColor(R.id.tv_chat_type,mContext.getResources().getColor(R.color.colorPrimary));
        } else if ("3".equals(item.value)) {
//            viewHolder.tvChatType.setText("已拒绝");
            holder.setTextColor(R.id.tv_chat_type,mContext.getResources().getColor(R.color.color_light_font));
        }
        holder.setText(R.id.tv_chat_type,item.call_status);
        if (!StringUtils.isEmpty(item.shop_name)) {
            holder.setGone(R.id.tv_shop_name,true);
            holder.setText(R.id.tv_shop_name,item.shop_name);
        } else {
            holder.setGone(R.id.tv_shop_name,false);
        }
        holder.setText(R.id.tv_time, DateUtils.millisToTime(DateUtils.FORMAT_YYYY_MM_DD_HHMMSS,item.add_time));
        holder.setText(R.id.tv_title,item.nickname);
        ImageLoadUtils.loadImage(mContext, item.avatar, holder.getView(R.id.civ_head), R.mipmap.img_head_def);
    }

}
