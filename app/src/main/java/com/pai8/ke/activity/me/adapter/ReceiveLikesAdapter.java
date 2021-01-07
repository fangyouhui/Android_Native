package com.pai8.ke.activity.me.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;
import com.pai8.ke.activity.message.entity.resp.MessageResp;
import com.pai8.ke.entity.Video;
import com.pai8.ke.entity.resp.LikeInfo;
import com.pai8.ke.utils.DateUtils;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author Created by zzf
 * @time 11:21
 * Description： 点赞adapter
 */
public class ReceiveLikesAdapter extends BaseQuickAdapter<LikeInfo, BaseViewHolder> {

    public ReceiveLikesAdapter(@Nullable List<LikeInfo> data) {
        super(R.layout.item_likes, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, LikeInfo item) {
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
        holder.setText(R.id.tv_time, date);
        if (item.getUser() != null) {
            holder.setText(R.id.tv_title, StringUtils.isEmpty(item.getUser().getNickname())
                    ? item.getUser().getPhone() : item.getUser().getNickname());
            ImageLoadUtils.loadImage(mContext, item.getUser().getAvatar(),
                    holder.getView(R.id.civ_head), R.mipmap.img_head_def);
        } else {
            holder.setText(R.id.tv_title, "暂无");
            holder.setImageResource(R.id.civ_head, R.mipmap.img_head_def);
        }

        ImageLoadUtils.loadImage(mContext, item.getVideo().getVideo_path(),
                holder.getView(R.id.civ_works_img), R.mipmap.img_head_def);
    }

}
