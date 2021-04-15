package com.pai8.ke.activity.takeaway.adapter;

import android.graphics.Color;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.entity.req.OrderStatusInfo;

import java.util.List;

public class OrderStatusAdapter extends BaseQuickAdapter<OrderStatusInfo, BaseViewHolder> {
    public OrderStatusAdapter(@Nullable List<OrderStatusInfo> data) {
        super(R.layout.item_order_status, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderStatusInfo item) {
        RelativeLayout rlStatus = helper.getView(R.id.root);
        helper.setText(R.id.tv_content, item.name);

        if (item.isSelect) {
            rlStatus.setBackgroundResource(R.drawable.shape_store_business_radius16);
            helper.setTextColor(R.id.tv_content, Color.parseColor("#FF7F47"));
        } else {
            rlStatus.setBackgroundResource(R.drawable.shape_gray_radius16);
            helper.setTextColor(R.id.tv_content, Color.parseColor("#111111"));
        }

    }


    public void choosePosition(int position) {
        OrderStatusInfo filterInfo = getData().get(position);
        if (filterInfo.isSelect()) {
            filterInfo.setSelect(false);
        } else {
            filterInfo.setSelect(true);
        }
        notifyDataSetChanged();
    }
}
