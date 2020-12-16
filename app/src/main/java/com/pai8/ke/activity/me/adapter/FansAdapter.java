package com.pai8.ke.activity.me.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;
import com.pai8.ke.activity.me.entity.resp.FansResp;
import com.pai8.ke.activity.message.entity.resp.MessageResp;
import com.pai8.ke.entity.User;
import com.pai8.ke.utils.DateUtils;
import com.pai8.ke.utils.ImageLoadUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author Created by zzf
 * @time 11:21
 * Description： 点赞adapter
 */
public class FansAdapter extends BaseQuickAdapter<User, BaseViewHolder> {

    public FansAdapter(@Nullable List<User> data) {
        super(R.layout.item_attention,data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, User item) {
//        holder.setText(R.id.tv_time, DateUtils.millisToTime(DateUtils.FORMAT_YYYY_MM_DD_HHMMSS,item.add_time));
//        holder.setText(R.id.tv_title, item.nickname);
//        ImageLoadUtils.loadImage(mContext, item.avatar, holder.getView(R.id.civ_head), R.mipmap.img_head_def);
//        if (item.is_focus == 1) {
//            holder.setText(R.id.tv_attention_status, "已关注");
//            holder.setBackgroundRes(R.id.tv_attention_status, R.drawable.shape_orgin_gradient_gray);
//        } else {
//            holder.setText(R.id.tv_attention_status,"关注");
//            holder.setBackgroundRes(R.id.tv_attention_status,R.drawable.shape_orgin_gradient);
//        }
    }


}
