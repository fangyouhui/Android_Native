package com.pai8.ke.activity.takeaway.adapter;

import android.graphics.Color;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.entity.resq.ShopInfo;

import java.util.List;

import androidx.annotation.Nullable;

public class GoodCategoryAdapter extends BaseQuickAdapter<ShopInfo, BaseViewHolder> {
    public GoodCategoryAdapter(@Nullable List<ShopInfo> data) {
        super(R.layout.item_order_status, data);
    }

    private int lastClickPosition = -1;

    @Override
    protected void convert(BaseViewHolder helper, ShopInfo item) {

        RelativeLayout rlStatus = helper.getView(R.id.rl_status);
        helper.setText(R.id.tv_content, item.name);
        if (lastClickPosition == helper.getLayoutPosition()) {
            rlStatus.setBackgroundResource(R.drawable.shape_store_business_radius16);
            helper.setTextColor(R.id.tv_content, Color.parseColor("#FF7F47"));
        } else {
            rlStatus.setBackgroundResource(R.drawable.shape_gray_radius16);
            helper.setTextColor(R.id.tv_content, Color.parseColor("#111111"));
        }

    }

    public void singleChoose(int position) {
        lastClickPosition = position;
        notifyDataSetChanged();
    }
}
