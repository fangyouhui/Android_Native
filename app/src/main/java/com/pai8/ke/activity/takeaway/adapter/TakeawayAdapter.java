package com.pai8.ke.activity.takeaway.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TakeawayAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public TakeawayAdapter(@Nullable List<String> data) {
        super(R.layout.item_takeaway, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {

    }
}
