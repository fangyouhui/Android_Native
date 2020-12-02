package com.pai8.ke.activity.takeaway.adapter;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.pai8.ke.R;
import com.pai8.ke.base.BaseRecyclerViewAdapter;
import com.pai8.ke.base.BaseViewHolder;
import com.pai8.ke.entity.CouponInfoEntity;
import com.pai8.ke.entity.resp.CouponGetListResp;
import com.pai8.ke.entity.resp.CouponListResp;
import com.pai8.ke.utils.DateUtils;
import com.pai8.ke.utils.ResUtils;
import com.pai8.ke.utils.SpanUtils;
import com.pai8.ke.utils.TextViewUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 商家优惠券Adapter
 */
public class ShopCouponListAdapter extends BaseRecyclerViewAdapter<CouponGetListResp> {

    private Click mClick;

    public void setClick(Click click) {
        mClick = click;
    }

    public ShopCouponListAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_shop_coupon_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        ViewHolder viewHolder = (ViewHolder) holder;
        CouponGetListResp coupons = mDataList.get(position);
        SpannableStringBuilder span = SpanUtils.getBuilder("")
                .append(mContext, "￥")
                .setProportion(0.7f)
                .append(mContext, coupons.getDis_price())
                .create(mContext);
        viewHolder.tvPrice.setText(span);

        viewHolder.tvTitle.setText(coupons.getShop_name());
        String startTime =
                DateUtils.formatYYYYMMDD(String.valueOf(coupons.getStart_time()));
        String endTime = DateUtils.formatYYYYMMDD(String.valueOf(coupons.getEnd_time()));
        viewHolder.tvDate.setText(startTime + " ~ " + endTime);
        viewHolder.tvDiscountPrice.setText("满" + coupons.getTrig_price() + "减" + coupons.getDis_price());

        TextView tvUseGuize = viewHolder.tvUseGuize;
        if (!coupons.isGuize()) {
            TextViewUtils.drawableRight(tvUseGuize, R.mipmap.icc_gray_down);
            viewHolder.tvGuize.setVisibility(View.GONE);
            viewHolder.clBg.setBackground(ResUtils.getDrawable(R.drawable.shape_white_radius8));
        } else {
            viewHolder.tvGuize.setVisibility(View.VISIBLE);
            TextViewUtils.drawableRight(tvUseGuize, R.mipmap.ic_gray_up);
            viewHolder.clBg.setBackground(ResUtils.getDrawable(R.drawable.shape_white_top_radius8));
            viewHolder.tvGuize.setBackground(ResUtils.getDrawable(R.drawable.shape_gray_bottom_radius8));
        }
        tvUseGuize.setOnClickListener(view -> {
            if (coupons.isGuize()) {
                TextViewUtils.drawableRight(tvUseGuize, R.mipmap.icc_gray_down);
                coupons.setGuize(false);
                viewHolder.clBg.setBackground(ResUtils.getDrawable(R.drawable.shape_white_radius8));
                viewHolder.tvGuize.setVisibility(View.GONE);
            } else {
                TextViewUtils.drawableRight(tvUseGuize, R.mipmap.ic_gray_up);
                coupons.setGuize(true);
                viewHolder.clBg.setBackground(ResUtils.getDrawable(R.drawable.shape_white_top_radius8));
                viewHolder.tvGuize.setVisibility(View.VISIBLE);
                viewHolder.tvGuize.setBackground(ResUtils.getDrawable(R.drawable.shape_gray_bottom_radius8));
            }
        });
        viewHolder.ivBtnEdit.setOnClickListener(view -> mClick.onClick(coupons));
    }

    public static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.tv_discount_price)
        TextView tvDiscountPrice;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_use_guize)
        TextView tvUseGuize;
        @BindView(R.id.iv_btn_edit)
        ImageView ivBtnEdit;
        @BindView(R.id.tv_guize)
        TextView tvGuize;
        @BindView(R.id.cl_bg)
        ConstraintLayout clBg;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface Click {

        void onClick(CouponGetListResp couponGetListResp);
    }
}
