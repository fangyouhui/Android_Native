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
import com.pai8.ke.activity.me.entity.resp.CouponResp;
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
public class ShopCouponListAdapter extends BaseRecyclerViewAdapter<CouponResp.CouponListBean> {

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
        CouponResp.CouponListBean coupons = mDataList.get(position);
        SpannableStringBuilder span = SpanUtils.getBuilder("")
                .append(mContext, "￥")
                .setProportion(0.7f)
                .append(mContext, coupons.getDis_price())
                .create(mContext);
        viewHolder.tvPrice.setText(span);

        String endTime = DateUtils.formatYYYYMMDD(String.valueOf(coupons.getEnd_time()));
        viewHolder.tvDate.setText(String.format("%s到期", endTime));
        if (coupons.getType() == 1) {
            viewHolder.tvTitle.setText(String.format("商品满减券:%s", coupons.getShop_name()));
            viewHolder.tvDiscountPrice.setText(String.format("满%s减%s", coupons.getTrig_price(), coupons.getDis_price()));
        } else {
            viewHolder.tvTitle.setText(String.format("运费抵扣券:%s", coupons.getShop_name()));
            viewHolder.tvDiscountPrice.setText("运费抵扣");
        }

        viewHolder.tvUseGuize.setText(coupons.getValue());
        viewHolder.tvBtnEdit.setOnClickListener(view -> mClick.onClick(coupons));
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
        @BindView(R.id.tv_btn_edit)
        TextView tvBtnEdit;
        @BindView(R.id.cl_bg)
        ConstraintLayout clBg;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface Click {

        void onClick(CouponResp.CouponListBean bean);
    }
}
