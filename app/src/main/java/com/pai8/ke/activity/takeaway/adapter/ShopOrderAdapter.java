package com.pai8.ke.activity.takeaway.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.entity.OrderInfo;
import com.pai8.ke.utils.ImageLoadUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ShopOrderAdapter extends BaseQuickAdapter<OrderInfo, BaseViewHolder> {
    public ShopOrderAdapter(@Nullable List<OrderInfo> data) {
        super(R.layout.item_order, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, OrderInfo item) {

        TextView tvStatus = helper.getView(R.id.tv_status);
        TextView mTvReject = helper.getView(R.id.tv_reject);
        TextView mTvCancel = helper.getView(R.id.tv_cancel);
        helper.addOnClickListener(R.id.tv_cancel,R.id.tv_food_status);
        TextView mTvFoodStatus = helper.getView(R.id.tv_food_status);
       // 0为待支付 1为已支付 2为商家已接单 7为订单制作完成 3为配送中 4为订单已完成 5为订单已申请退款 6订单被拒绝退款 8为订单已退款 9为订单已取消 -1为支付超时 -2订单拒绝接单
       //订单状态 0为待支付 1为已支付 2为商家已接单 3为配送中 4为订单已完成 5为订单已申请退款 6订单被拒绝退款 8为订单已退款 9为订单已取消 -1为支付超时 -2订单拒绝接单
        mTvReject.setVisibility(View.GONE);
        mTvCancel.setVisibility(View.GONE);
        if(item.order_status == 0){
            tvStatus.setText("待支付");
            mTvCancel.setVisibility(View.VISIBLE);
        }else if(item.order_status == 1){
            tvStatus.setText("待商家接单");
            mTvCancel.setVisibility(View.VISIBLE);
            mTvCancel.setText("拒绝接单");
            mTvFoodStatus.setText("立即接单");
        }else if(item.order_status == 2){
            tvStatus.setText("商品准备中");
            mTvReject.setVisibility(View.GONE);
            mTvCancel.setVisibility(View.GONE);
            mTvFoodStatus.setVisibility(View.GONE);
        }else if(item.order_status == 3){

            tvStatus.setText("商品配送中");
            mTvReject.setVisibility(View.GONE);
            mTvCancel.setVisibility(View.GONE);
            mTvFoodStatus.setVisibility(View.GONE);
        }else if(item.order_status == 4){
            tvStatus.setText("订单已完成");
            mTvCancel.setVisibility(View.GONE);
            mTvFoodStatus.setVisibility(View.GONE);
        }else if(item.order_status == 5){
            tvStatus.setText("退款申请中");
            mTvCancel.setText("拒绝退款");
            mTvFoodStatus.setText("同意退款");
        }else if(item.order_status == 6){

            tvStatus.setText("拒绝退款");
            mTvReject.setVisibility(View.GONE);
            mTvCancel.setVisibility(View.GONE);
            mTvFoodStatus.setVisibility(View.GONE);
        }else if(item.order_status == 8){

            tvStatus.setText("订单已退款");
            mTvReject.setVisibility(View.GONE);
            mTvCancel.setVisibility(View.GONE);
            mTvFoodStatus.setVisibility(View.GONE);
        }else if(item.order_status == 9){

            tvStatus.setText("订单已取消");
            mTvReject.setVisibility(View.GONE);
            mTvCancel.setVisibility(View.GONE);
            mTvFoodStatus.setVisibility(View.GONE);
        }else if(item.order_status == -1){

            tvStatus.setText("订单超时");
            mTvReject.setVisibility(View.GONE);
            mTvCancel.setVisibility(View.GONE);
            mTvFoodStatus.setVisibility(View.GONE);
        }else if(item.order_status == -2){
            tvStatus.setText("商家拒绝接单");
            mTvReject.setVisibility(View.GONE);
            mTvCancel.setVisibility(View.GONE);
            mTvFoodStatus.setVisibility(View.GONE);
        }


        ImageLoadUtils.setCircularImage(mContext,item.shop_img,helper.getView(R.id.iv_store),R.mipmap.ic_launcher);
        helper.setText(R.id.tv_name,item.shop_name);
        helper.setText(R.id.tv_price,"¥"+item.order_price);
        helper.setText(R.id.tv_total,"共"+item.count+"件");
        RecyclerView rvFood = helper.getView(R.id.rv_foods);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rvFood.setLayoutManager(linearLayoutManager);
        OrderFoodAdapter adapter = new OrderFoodAdapter(item.goods_info);
        rvFood.setAdapter(adapter);


    }
}
