package com.pai8.ke.activity.me.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;
import com.pai8.ke.activity.message.entity.resp.MessageResp;
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
public class HistoryWatchAdapter extends BaseQuickAdapter<MessageResp, BaseViewHolder> {

    public HistoryWatchAdapter(@Nullable List<MessageResp> data) {
        super(R.layout.item_attention,data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, MessageResp item) {
        holder.setText(R.id.tv_name, item.shop_name);

        {
            holder.setText(R.id.tv_type, "外卖");
        }
        {
            holder.setText(R.id.tv_type, "团购");
        }

        ImageLoadUtils.loadImage(mContext, item.video_cover, holder.getView(R.id.civ_cover), R.mipmap.img_head_def);

    }

}
