package com.pai8.ke.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lhs.library.base.BaseRecyclerViewAdapter;
import com.lhs.library.base.BaseViewHolder;
import com.pai8.ke.R;
import com.pai8.ke.activity.message.entity.MsgCountInfo;
import com.pai8.ke.databinding.ItemMsgCenterBinding;

import java.util.List;

public class MsgCenterAdapter extends BaseRecyclerViewAdapter<MsgCountInfo> {

    public MsgCenterAdapter(Context context, List<MsgCountInfo> list) {
        super(context, list, false);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        ItemMsgCenterBinding binding = ItemMsgCenterBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MsgViewHolder(binding);
    }

    @Override
    protected void onBindNormalViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof MsgViewHolder) {
            MsgViewHolder holder = (MsgViewHolder) viewHolder;
            MsgCountInfo str = getItem(position);
            int count = str.getCount();
            if (0 <= 0) {
                holder.binding.tvCount.setVisibility(View.GONE);
            } else {
                holder.binding.tvCount.setVisibility(View.VISIBLE);
                holder.binding.tvCount.setText(String.valueOf(count));
            }
            switch (position) {
                case 0:
                    holder.binding.tvMsgTitle.setText("订单消息");
                    holder.binding.tvMsgContent.setText(count <= 0 ? "暂无消息" : "您有一笔新的订单！");
                    holder.binding.ivMsgTypeOrder.setImageResource(R.mipmap.ic_msg_type_order);
                    break;
                case 1:
                    holder.binding.tvMsgTitle.setText("系统消息");
                    holder.binding.tvMsgContent.setText(count <= 0 ? "暂无消息" : "app上线咯");
                    holder.binding.ivMsgTypeOrder.setImageResource(R.mipmap.ic_msg_type_sys);
                    break;
                case 2:
                    holder.binding.tvMsgTitle.setText("活动消息");
                    holder.binding.tvMsgContent.setText(count <= 0 ? "暂无消息" : "美食节活动已上线，请查看详情");
                    holder.binding.ivMsgTypeOrder.setImageResource(R.mipmap.ic_msg_type_activie);
                    break;
                case 3:
                    holder.binding.tvMsgTitle.setText("一对一聊天消息");
                    holder.binding.tvMsgContent.setText(count <= 0 ? "暂无消息" : "邀请你参与视频通话");
                    holder.binding.ivMsgTypeOrder.setImageResource(R.mipmap.ic_msg_type_chat);
                    break;
            }
            holder.binding.getRoot().setOnClickListener(v -> mListener.onItemClick(str, position));
        }

    }

    static class MsgViewHolder extends BaseViewHolder<ItemMsgCenterBinding> {

        public MsgViewHolder(@NonNull ItemMsgCenterBinding viewBinding) {
            super(viewBinding);
        }
    }
}
