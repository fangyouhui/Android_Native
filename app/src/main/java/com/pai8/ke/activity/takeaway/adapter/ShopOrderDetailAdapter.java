package com.pai8.ke.activity.takeaway.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lhs.library.base.BaseRecyclerViewAdapter;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.entity.GoodsInfo;
import com.pai8.ke.databinding.ItemShopOrderBinding;
import com.pai8.ke.utils.ImageLoadUtils;

import java.util.List;

public class ShopOrderDetailAdapter extends BaseRecyclerViewAdapter<GoodsInfo> {

    public ShopOrderDetailAdapter(Context context, List<GoodsInfo> list) {
        super(context, list);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        ItemShopOrderBinding binding = ItemShopOrderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new ShopOrderDetailViewHolder(binding);
    }

    @Override
    protected void onBindNormalViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ShopOrderDetailViewHolder) {
            GoodsInfo item = getItem(position);
            ShopOrderDetailViewHolder holder = (ShopOrderDetailViewHolder) viewHolder;
            ImageLoadUtils.setCircularImage(mContext, item.getGoods_img().get(0), holder.binding.ivFood, R.mipmap.ic_launcher);
            holder.binding.tvFoodName.setText(item.getTitle());
            holder.binding.tvNum.setText("*" + item.getGoods_num());
            holder.binding.tvPrice.setText("¥" + item.getGoods_price());
            holder.binding.tvDiscount.setText("¥" + item.getGoods_origin_price());
            holder.binding.tvDiscount.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }

    }

    class ShopOrderDetailViewHolder extends com.lhs.library.base.BaseViewHolder<ItemShopOrderBinding> {

        public ShopOrderDetailViewHolder(@NonNull ItemShopOrderBinding viewBinding) {
            super(viewBinding);
        }
    }

}
