package com.pai8.ke.adapter;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pai8.ke.R;
import com.pai8.ke.base.BaseRecyclerViewAdapter;
import com.pai8.ke.base.BaseViewHolder;
import com.pai8.ke.entity.CouponInfoEntity;
import com.pai8.ke.entity.resp.CouponGetListResp;
import com.pai8.ke.entity.resp.CouponListResp;
import com.pai8.ke.utils.DateUtils;
import com.pai8.ke.utils.SpanUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 优惠券领取Adapter
 */
public class CouponGetListAdapter extends BaseRecyclerViewAdapter<CouponGetListResp> {

    public CouponGetListAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_coupon_get_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        ViewHolder viewHolder = (ViewHolder) holder;
        CouponGetListResp coupons = mDataList.get(position);
        int type = coupons.getType();
        SpannableStringBuilder span = SpanUtils.getBuilder("")
                .append(mContext, "￥")
                .setProportion(0.7f)
                .append(mContext, coupons.getDis_price())
                .create(mContext);
        viewHolder.tvPrice.setText(span);
        viewHolder.tvTitle.setText(coupons.getCoupon_name());

        switch (type) {
            case 1:
                viewHolder.tvDiscountPrice.setText(String.format("满%s减%s", coupons.getTrig_price(), coupons.getDis_price()));
                break;
            case 2:
                viewHolder.tvDiscountPrice.setText("运费抵扣券");
                break;
            default:
                break;
        }
    }

    public static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.tv_discount_price)
        TextView tvDiscountPrice;
        @BindView(R.id.tv_title)
        TextView tvTitle;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
