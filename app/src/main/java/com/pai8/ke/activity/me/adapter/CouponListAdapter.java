package com.pai8.ke.activity.me.adapter;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.pai8.ke.R;
import com.pai8.ke.base.BaseRecyclerViewAdapter;
import com.pai8.ke.base.BaseViewHolder;
import com.pai8.ke.entity.CouponInfoEntity;
import com.pai8.ke.entity.resp.CouponListResp;
import com.pai8.ke.utils.DateUtils;
import com.pai8.ke.utils.ResUtils;
import com.pai8.ke.utils.SpanUtils;
import com.pai8.ke.utils.TextViewUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 优惠券Adapter
 */
public class CouponListAdapter extends BaseRecyclerViewAdapter<CouponListResp> {

    private int type = 1;
    private Click mClick;

    public void setType(int type) {
        this.type = type;
    }

    public void setClick(Click click) {
        mClick = click;
    }

    public CouponListAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_coupon_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        CouponListResp coupons = mDataList.get(position);
        CouponInfoEntity coupon_info = coupons.getCoupon_info();
        if (coupon_info != null) {
            SpannableStringBuilder span = SpanUtils.getBuilder("")
                    .append(mContext, "￥")
                    .setProportion(0.7f)
                    .append(mContext, coupons.getCoupon_info().getDis_price())
                    .create(mContext);
            viewHolder.tvPrice.setText(span);

            viewHolder.tvTitle.setText(coupons.getShop_info().getShop_name());
            String startTime = DateUtils.formatYYYYMMDD(String.valueOf(coupons.getCoupon_info().getStart_time()));
            String endTime = DateUtils.formatYYYYMMDD(String.valueOf(coupons.getCoupon_info().getEnd_time()));
            viewHolder.tvDate.setText(startTime + " ~ " + endTime);
            if (coupons.getType() == 1) {
                viewHolder.tvDiscountPrice.setText("满" + coupons.getCoupon_info().getTrig_price() + "减" + coupons.getCoupon_info().getDis_price());
            } else {
                viewHolder.tvDiscountPrice.setText("运费抵扣");
            }
            viewHolder.tvGuize.setText(String.format("优惠券使用日期：%s 至 %s", startTime, endTime));
        } else {
            viewHolder.tvPrice.setText("- -");
            viewHolder.tvTitle.setText("- -");
            viewHolder.tvDate.setText("- -");
            viewHolder.tvDiscountPrice.setText("- -");
        }
        if (type == 1) {
            viewHolder.tvUse.setVisibility(View.VISIBLE);
            viewHolder.ivCouponTimeOut.setVisibility(View.GONE);
            viewHolder.cb.setVisibility(View.GONE);
            viewHolder.tvUse.setOnClickListener(view -> mClick.onUseClick(coupons));

        } else if (type == 2) {
            viewHolder.tvUse.setVisibility(View.GONE);
            viewHolder.ivCouponTimeOut.setVisibility(View.VISIBLE);
            viewHolder.cb.setVisibility(View.GONE);

            int color = ResUtils.getColor(R.color.color_light_font);
            viewHolder.tvPrice.setTextColor(color);
            viewHolder.tvTitle.setTextColor(color);
            viewHolder.tvDate.setTextColor(color);
            viewHolder.tvDiscountPrice.setTextColor(color);

        } else if (type == 3) {
            viewHolder.tvUse.setVisibility(View.GONE);
            viewHolder.ivCouponTimeOut.setVisibility(View.GONE);
            viewHolder.cb.setVisibility(View.VISIBLE);

            viewHolder.cb.setChecked(coupons.isChecked());
            viewHolder.cb.setOnClickListener(view -> {
                if (coupons.isChecked()) {
                    coupons.setChecked(false);
                } else {
                    coupons.setChecked(true);
                }
                mClick.onSelect();
            });
        }

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
    }

    public List<CouponListResp> getSelect() {
        List<CouponListResp> m = new ArrayList<>();
        for (CouponListResp couponListResp : mDataList) {
            if (couponListResp.isChecked()) {
                m.add(couponListResp);
            }
        }
        return m;
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
        @BindView(R.id.tv_use)
        TextView tvUse;
        @BindView(R.id.iv_coupon_time_out)
        ImageView ivCouponTimeOut;
        @BindView(R.id.cb)
        CheckBox cb;
        @BindView(R.id.tv_guize)
        TextView tvGuize;
        @BindView(R.id.cl_bg)
        ConstraintLayout clBg;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface Click {

        void onUseClick(CouponListResp couponListResp);

        void onSelect();
    }
}
