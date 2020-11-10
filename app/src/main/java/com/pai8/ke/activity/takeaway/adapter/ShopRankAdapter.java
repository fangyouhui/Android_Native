package com.pai8.ke.activity.takeaway.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ShopRankAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public ShopRankAdapter(@Nullable List<String> data) {
        super(R.layout.item_shop_rank, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {


        ImageView ivPosition = helper.getView(R.id.iv_position);
        TextView tvPosition = helper.getView(R.id.tv_position);
        TextView tvPrice = helper.getView(R.id.tv_price);
        tvPrice.setTextColor(Color.parseColor("#FF9808"));  //黄色
        tvPosition.setText("");
        if (helper.getLayoutPosition() == 0) {
            ivPosition.setVisibility(View.VISIBLE);
            ivPosition.setImageResource(R.mipmap.ic_rank_one);
        } else if (helper.getLayoutPosition() == 1) {
            ivPosition.setVisibility(View.VISIBLE);
            ivPosition.setImageResource(R.mipmap.ic_ran_tow);
        } else if (helper.getLayoutPosition() == 2) {
            ivPosition.setVisibility(View.VISIBLE);
            ivPosition.setImageResource(R.mipmap.ic_rank_three);
        } else {
            ivPosition.setVisibility(View.INVISIBLE);
            tvPrice.setTextColor(Color.parseColor("#111111"));  //黑色
            tvPosition.setTextColor(Color.parseColor("#999999"));
            tvPosition.setBackgroundResource(0);
            tvPosition.setText(helper.getLayoutPosition() < 9 ? ("0" + (helper.getLayoutPosition() + 1)) : "" + (helper.getLayoutPosition() + 1));
        }


    }
}
