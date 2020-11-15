package com.pai8.ke.activity.takeaway.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TakeawayFoodAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public TakeawayFoodAdapter(@Nullable List<String> data) {
        super(R.layout.item_takeway_manager_goods, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {


        helper.addOnClickListener(R.id.tv_edit_goods);

    }
}
