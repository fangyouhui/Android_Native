package com.pai8.ke.activity.takeaway.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.entity.resq.smallGoodsInfo;
import com.pai8.ke.utils.ImageLoadUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GroupBuyManagerAdapter extends BaseQuickAdapter<smallGoodsInfo, BaseViewHolder> {
    private int mSelectedPosition;


    public void setCheckedPosition(int checkedPosition) {
        this.mSelectedPosition = checkedPosition;
        notifyDataSetChanged();
    }

    public GroupBuyManagerAdapter( @Nullable List<smallGoodsInfo> data) {
        super(R.layout.item_group_buy_manager, data);
    }

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */

    @Override
    protected void convert(@NonNull BaseViewHolder helper, smallGoodsInfo item) {
        helper.setText(R.id.tv_name,item.title);
        helper.setText(R.id.tv_price,"团购价:￥"+item.sell_price);

        ImageLoadUtils.setCircularImage(mContext, item.cover, helper.getView(R.id.iv_food), R.mipmap.ic_launcher);

        helper.addOnClickListener(R.id.tv_edit);

    }
}
