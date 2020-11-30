package com.pai8.ke.adapter;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pai8.ke.R;
import com.pai8.ke.base.BaseRecyclerViewAdapter;
import com.pai8.ke.base.BaseViewHolder;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.utils.ResUtils;
import com.pai8.ke.utils.SpanUtils;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class MsgCenterAdapter extends BaseRecyclerViewAdapter<String> {

    public MsgCenterAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_msg_center, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        String str = mDataList.get(position);
        switch (position) {
            case 0:
                viewHolder.tvMsgTitle.setText("订单消息");
                viewHolder.tvMsgContent.setText("您有一笔新的订单！");
                viewHolder.ivMsgTypeOrder.setImageResource(R.mipmap.ic_msg_type_order);
                break;
            case 1:
                viewHolder.tvMsgTitle.setText("系统消息");
                viewHolder.tvMsgContent.setText("app上线咯");
                viewHolder.ivMsgTypeOrder.setImageResource(R.mipmap.ic_msg_type_sys);
                break;
            case 2:
                viewHolder.tvMsgTitle.setText("活动消息");
                viewHolder.tvMsgContent.setText("美食节活动已上线，请查看详情");
                viewHolder.ivMsgTypeOrder.setImageResource(R.mipmap.ic_msg_type_activie);
                break;
            case 3:
                viewHolder.tvMsgTitle.setText("一对一聊天消息");
                viewHolder.tvMsgContent.setText("邀请你参与视频通话");
                viewHolder.ivMsgTypeOrder.setImageResource(R.mipmap.ic_msg_type_chat);
                break;
        }

    }

    static class ViewHolder extends BaseViewHolder {

        @BindView(R.id.iv_msg_type_order)
        ImageView ivMsgTypeOrder;
        @BindView(R.id.tv_msg_title)
        TextView tvMsgTitle;
        @BindView(R.id.tv_msg_content)
        TextView tvMsgContent;
        @BindView(R.id.tv_msg_date)
        TextView tvMsgDate;

        public ViewHolder(View itemView) {
            super(itemView);
        }

    }
}
