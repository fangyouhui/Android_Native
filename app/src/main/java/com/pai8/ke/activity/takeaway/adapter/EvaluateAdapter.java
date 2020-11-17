package com.pai8.ke.activity.takeaway.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * 商品评价
 */
public class EvaluateAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public EvaluateAdapter(@Nullable List<String> data) {
        super(R.layout.item_evaluate, data);
    }
    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }
}