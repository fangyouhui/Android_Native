package com.pai8.ke.activity.me.adapter;

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
public class AttentionMineAdapter extends BaseQuickAdapter<MessageResp, BaseViewHolder> {

    public AttentionMineAdapter(@Nullable List<MessageResp> data) {
        super(R.layout.item_attention_mine,data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, MessageResp item) {
        if (!StringUtils.isEmpty(item.shop_name)) {
            holder.setGone(R.id.tv_shop_name,true);
            holder.setText(R.id.tv_shop_name,item.shop_name);
        } else {
            holder.setGone(R.id.tv_shop_name,false);
        }

        {
            holder.setGone(R.id.tv_attention_status,true);
        }
        {
            holder.setGone(R.id.tv_attention_status,false);
        }

        holder.setText(R.id.tv_title,item.nickname);
        ImageLoadUtils.loadImage(mContext, item.avatar, holder.getView(R.id.civ_head), R.mipmap.img_head_def);
    }

}
