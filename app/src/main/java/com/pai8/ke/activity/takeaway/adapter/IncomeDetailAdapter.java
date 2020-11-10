package com.pai8.ke.activity.takeaway.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class IncomeDetailAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public IncomeDetailAdapter(@Nullable List<String> data) {
        super(R.layout.item_income_detail, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {

    }
}
