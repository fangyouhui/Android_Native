package com.pai8.ke.adapter;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lhs.library.base.BaseRecyclerViewAdapter;
import com.lhs.library.base.BaseViewHolder;
import com.pai8.ke.activity.me.entity.resp.ShopCouponListResult;
import com.pai8.ke.databinding.ItemCouponGetListBinding;
import com.pai8.ke.utils.SpanUtils;

import java.util.List;

/**
 * 优惠券领取Adapter
 */
public class CouponGetListAdapter extends BaseRecyclerViewAdapter<ShopCouponListResult.CouponListBean> {

    public CouponGetListAdapter(Context mContext, List<ShopCouponListResult.CouponListBean> list) {
        super(mContext, list, false);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        ItemCouponGetListBinding binding = ItemCouponGetListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CouponGetListViewHolder(binding);
    }

    @Override
    protected void onBindNormalViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof CouponGetListViewHolder) {
            CouponGetListViewHolder holder = (CouponGetListViewHolder) viewHolder;
            ShopCouponListResult.CouponListBean coupons = mData.get(position);
            SpannableStringBuilder span = SpanUtils.getBuilder("")
                    .append(mContext, "￥")
                    .setProportion(0.7f)
                    .append(mContext, coupons.getDis_price())
                    .create(mContext);
            holder.binding.tvPrice.setText(span);
            holder.binding.tvTitle.setText(coupons.getCoupon_name());
            switch (coupons.getType()) {
                case 1:
                    holder.binding.tvDiscountPrice.setText(String.format("满%s减%s", coupons.getTrig_price(), coupons.getDis_price()));
                    break;
                case 2:
                    holder.binding.tvDiscountPrice.setText("运费抵扣券");
                    break;
                default:
                    break;
            }
        }
    }

    public static class CouponGetListViewHolder extends BaseViewHolder<ItemCouponGetListBinding> {

        public CouponGetListViewHolder(@NonNull ItemCouponGetListBinding viewBinding) {
            super(viewBinding);
        }
    }

}
