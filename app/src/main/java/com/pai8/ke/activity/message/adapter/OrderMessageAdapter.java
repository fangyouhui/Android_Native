package com.pai8.ke.activity.message.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;
import com.pai8.ke.activity.message.entity.resp.MessageResp;
import com.pai8.ke.utils.DateUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
 * Description： 订单消息adapter
 */
public class OrderMessageAdapter extends BaseQuickAdapter<MessageResp, BaseViewHolder> {

    public OrderMessageAdapter(@Nullable List<MessageResp> data) {
        super(R.layout.item_order_message,data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, MessageResp item) {
        if (item.read_status == 1) {
            holder.setGone(R.id.tv_new,false);
        } else {
            holder.setGone(R.id.tv_new,true);
        }
        holder.setText(R.id.tv_content,item.content);
        holder.setText(R.id.tv_time,DateUtils.millisToTime(DateUtils.FORMAT_YYYY_MM_DD_HHMMSS,item.add_time));
        holder.setText(R.id.tv_title,item.title);
        holder.setText(R.id.tv_order_status,item.value);
    }
}
