package com.pai8.ke.groupBuy.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.lhs.library.base.BaseRecyclerViewAdapter;
import com.lhs.library.base.BaseViewHolder;
import com.pai8.ke.R;
import com.pai8.ke.databinding.ItemGroupBuyTypeBinding;
import com.pai8.ke.entity.GetGroupShopListResult;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.utils.PreferencesUtils;

import java.util.List;

public class GroupBuyTypeAdapter extends BaseRecyclerViewAdapter<GetGroupShopListResult.ShopList> {

    public GroupBuyTypeAdapter(Context context, List<GetGroupShopListResult.ShopList> list) {
        super(context, list);

    }

    @Override
    protected RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        ItemGroupBuyTypeBinding commentBinding = ItemGroupBuyTypeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new GroupBuyTypeViewHolder(commentBinding);
    }

    @Override
    protected void onBindNormalViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof GroupBuyTypeViewHolder) {
            GroupBuyTypeViewHolder holder = (GroupBuyTypeViewHolder) viewHolder;
            GetGroupShopListResult.ShopList bean = mData.get(position);
            ImageLoadUtils.loadImage(bean.getShop_img(), holder.binding.imageView);
            holder.binding.tvTitle.setText(bean.getShop_name());
            holder.binding.tvScore.setText(bean.getScore() + "");
            holder.binding.tvMonthSaleCount.setText("月售 " + bean.getMonth_sale_count());
            holder.binding.tvType.setText(bean.getCate_name());
            holder.binding.tvAddress.setText(bean.getAddress());

//            String latitude = (String) PreferencesUtils.get(mContext, "latitude", "0");
//            String longitude = (String) PreferencesUtils.get(mContext, "longitude", "0");
//
//            LatLng latLng01 = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
//            LatLng latLng02 = new LatLng(Double.valueOf(bean.getLatitude()), Double.valueOf(bean.getLongitude()));
//            //单位米
//            float distance = AMapUtils.calculateLineDistance(latLng01, latLng02);
//            holder.binding.tvDistance.setText((distance / 1000) + "km");

            if (TextUtils.isEmpty(bean.getShop_desc())) {
                holder.binding.tvDesc.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
                holder.binding.tvDesc.setTextColor(Color.parseColor("#ff666666"));
                holder.binding.tvDesc.setText("暂无简介");
            } else {
                holder.binding.tvDesc.setBackgroundResource(R.drawable.bg_shop_desc);
                holder.binding.tvDesc.setTextColor(Color.parseColor("#fff23c6a"));
                holder.binding.tvDesc.setText(bean.getShop_desc());
            }

            holder.binding.getRoot().setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onItemClick(bean, position);
                }
            });

        }
    }


    class GroupBuyTypeViewHolder extends BaseViewHolder<ItemGroupBuyTypeBinding> {

        public GroupBuyTypeViewHolder(@NonNull ItemGroupBuyTypeBinding viewbinding) {
            super(viewbinding);
        }
    }
}
