package com.pai8.ke.activity.takeaway.adapter;

import android.text.TextUtils;

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

        helper.setText(R.id.item_tv_time,item.delivery_time);
        helper.setText(R.id.item_tv_score,item.score+"");
        helper.setText(R.id.item_tv_month_sale,"月售 "+item.monthly_sale);
        helper.setText(R.id.item_tv_price,"起送价 ¥"+item.floor_send_cost);
        helper.setText(R.id.item_tv_deliver_fee,"| 配送费  ¥"+item.send_cost);
        helper.setText(R.id.item_tv_desc, TextUtils.isEmpty(item.shop_desc) ? "暂无简介" : item.shop_desc);
        String distance ;
//        if(item.distance>1000){
//            distance = item.distance/1000+"km";
//        }else{
//            distance = item.distance+"m";
//        }
        helper.setText(R.id.item_home_store_km,item.distance);
    }
}
