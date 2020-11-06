package com.pai8.ke.activity.takeaway.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.entity.FoodClassifyInfo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FoodClassifyAdapter extends BaseQuickAdapter<FoodClassifyInfo, BaseViewHolder> {

    private int mSelectedPosition;


    public FoodClassifyAdapter(@Nullable List<FoodClassifyInfo> data) {
        super(R.layout.item_food_classify, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, FoodClassifyInfo item) {
        helper.setText(R.id.tv_food_classify,item.bigSortName);
    }


    public void setSelectedPosition(int position) {
        getData().get(mSelectedPosition).isSelected = false;
        notifyItemChanged(mSelectedPosition);
        getData().get(position).isSelected = true;
        notifyItemChanged(position);
        mSelectedPosition = position;
    }
}
