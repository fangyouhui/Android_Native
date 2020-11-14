package com.pai8.ke.activity.takeaway.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.entity.resq.StoreInfo;
import com.pai8.ke.utils.ImageLoadUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TakeawayAdapter extends BaseQuickAdapter<StoreInfo, BaseViewHolder> {
    public TakeawayAdapter(@Nullable List<StoreInfo> data) {
        super(R.layout.item_takeaway, data);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, StoreInfo item) {

        helper.setText(R.id.item_tv_name,item.shop_name);
        ImageLoadUtils.setCircularImage(mContext,item.shop_img,helper.getView(R.id.item_iv_pic),R.mipmap.ic_launcher);

    }
}
