package com.pai8.ke.activity.me.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;
import com.pai8.ke.activity.me.entity.resp.FansResp;
import com.pai8.ke.activity.message.entity.resp.MessageResp;
import com.pai8.ke.entity.User;
import com.pai8.ke.entity.Video;
import com.pai8.ke.entity.resp.AttentionMine;
import com.pai8.ke.utils.DateUtils;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author Created by zzf
 * @time 11:21
 * Description： 点赞adapter
 */
public class FansAdapter extends BaseQuickAdapter<AttentionMine, BaseViewHolder> {

    public FansAdapter(@Nullable List<AttentionMine> data) {
        super(R.layout.item_attention,data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, AttentionMine item) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(item.getCreate_time());
        Calendar today = Calendar.getInstance();
        String date;
        if(calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)
                && calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH)
                && calendar.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)) {
            date = new SimpleDateFormat("今天 HH:mm").format(calendar.getTime());
        } else {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(calendar.getTime());
        }
        holder.setText(R.id.tv_time,  date);

        if (item.getUser() != null) {
            holder.setText(R.id.tv_title, StringUtils.isEmpty(item.getUser().getUser_nickname())
                    ? item.getUser().getPhone() : item.getUser().getUser_nickname());
            ImageLoadUtils.loadImage(mContext, item.getUser().getAvatar(),
                    holder.getView(R.id.civ_head), R.mipmap.img_head_def);
        } else {
            holder.setText(R.id.tv_title, "暂无");
            holder.setImageResource(R.id.civ_head, R.mipmap.img_head_def);
        }

        holder.addOnClickListener(R.id.tv_attention_status);
        if (item.getIs_follow() == 1) {
            holder.setText(R.id.tv_attention_status, "已关注");
            holder.setBackgroundRes(R.id.tv_attention_status, R.drawable.shape_orgin_gradient_gray);
        } else {
            holder.setText(R.id.tv_attention_status,"关注");
            holder.setBackgroundRes(R.id.tv_attention_status,R.drawable.shape_orgin_gradient);
        }
    }


}
