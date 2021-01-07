package com.pai8.ke.activity.me.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;
import com.pai8.ke.activity.me.entity.resp.AttentionMineResp;
import com.pai8.ke.entity.User;
import com.pai8.ke.entity.resp.AttentionMine;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.utils.StringUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author Created by zzf
 * @time 11:21
 * Description： 关注adapter
 */
public class AttentionMineAdapter extends BaseQuickAdapter<AttentionMine, BaseViewHolder> {

    public AttentionMineAdapter(@Nullable List<AttentionMine> data) {
        super(R.layout.item_attention_mine, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, AttentionMine item) {
        if (item.getUser().getShop() != null && !StringUtils.isEmpty(item.getUser().getShop().getName())) {
            holder.setGone(R.id.tv_shop_name, true);
            holder.setText(R.id.tv_shop_name, item.getUser().getShop().getName());
        } else {
            holder.setGone(R.id.tv_shop_name, false);
        }

        if (item.getUser().getIs_focus() == 1) {
            holder.setGone(R.id.tv_attention_status, true);
        } else {
            holder.setGone(R.id.tv_attention_status, false);
        }

        holder.setText(R.id.tv_title, item.getUser().getUser_nickname());
        ImageLoadUtils.loadImage(mContext, item.getUser().getAvatar(), holder.getView(R.id.civ_head), R.mipmap.img_head_def);
    }

}
