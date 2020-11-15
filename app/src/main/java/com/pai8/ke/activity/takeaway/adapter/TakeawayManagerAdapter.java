package com.pai8.ke.activity.takeaway.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.entity.resq.ShopInfo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TakeawayManagerAdapter extends BaseQuickAdapter<ShopInfo, BaseViewHolder> {
    public TakeawayManagerAdapter(@Nullable List<ShopInfo> data) {
        super(R.layout.item_takeway_manager, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ShopInfo item) {

        helper.setText(R.id.tv_classify,item.name);

        helper.addOnClickListener(R.id.iv_edit,R.id.iv_del);

    }
}
