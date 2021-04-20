package com.pai8.ke.activity.takeaway.order;

import android.graphics.Paint;
import android.os.Bundle;

import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.lhs.library.base.BaseActivity;
import com.lhs.library.base.BaseAppConstants;
import com.pai8.ke.activity.takeaway.entity.GoodsInfo;
import com.pai8.ke.activity.takeaway.entity.OrderDetailResult;
import com.pai8.ke.activity.takeaway.fragment.OrderDetailHeadFragment;
import com.pai8.ke.databinding.ActivityUserTakeawayOrderDetailBinding;
import com.pai8.ke.groupBuy.viewmodel.UserOrderDetailViewModel;
import com.pai8.ke.utils.ImageLoadUtils;

import org.jetbrains.annotations.Nullable;

public class UserTakeawayOrderDetailActivity extends BaseActivity<UserOrderDetailViewModel, ActivityUserTakeawayOrderDetailBinding> {

    private String order_no;

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        order_no = getIntent().getStringExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0);
    }

    @Override
    public void addObserve() {
        mViewModel.getOrderDetailData().observe(this, data -> {
            setData(data);
        });
    }

    @Override
    public void initData() {
        mViewModel.orderDetail(order_no);
    }

    private void setData(OrderDetailResult orderInfo) {
        if (orderInfo.getOrder_status() == 0 || orderInfo.getOrder_status() == 4) {

        } else {
            //  ivMore.setVisibility(View.GONE);
        }

        OrderDetailHeadFragment orderDetailHeadFragment = OrderDetailHeadFragment.newInstance(orderInfo);
        getSupportFragmentManager().beginTransaction().replace(mBinding.headContent.getId(), orderDetailHeadFragment).commit();

        ImageLoadUtils.loadImage(orderInfo.getShop_info().getShop_img(), mBinding.ivStore);
        if (orderInfo.getShop_info() != null) {
            mBinding.tvStoreName.setText(orderInfo.getShop_info().getShop_name());
        }

        GoodsInfo info = orderInfo.getGoods_info().get(0);
        if (info != null) {
            ImageLoadUtils.loadImage(info.getGoods_img().get(0), mBinding.ivGoodImg);
            mBinding.tvGoodTitle.setText(info.getGoods_title());
            mBinding.tvSellPrice.setText("¥ " + info.getGoods_sell_price());
            mBinding.tvOriginPrice.setText("¥ " + info.getGoods_origin_price());
            mBinding.tvOriginPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            mBinding.tvGoodNum.setText("X" + info.getGoods_num());
        }

        mBinding.tvPackPrice.setText("¥ " + orderInfo.getBox_price());
        mBinding.tvSendPrice.setText("¥ " + orderInfo.getExpress_price());
        mBinding.tvCoupon.setText("- ¥ " + orderInfo.getOrder_discount_price());
        mBinding.tvDiscount.setText("优惠 ¥" + orderInfo.getOrder_discount_price());
        mBinding.tvPrice.setText("¥ " + orderInfo.getOrder_price());

        //配送信息
        if (orderInfo.getRider_info() != null) {
            mBinding.tvRiderName.setText("商品准备完成后，为你配送");
        }
        mBinding.tvRiderTime.setText("尽快送达");
        if (orderInfo.getAddress_info() != null) {
            mBinding.tvAddress.setText(orderInfo.getAddress_info().getAddress());
        }

        //订单信息
        mBinding.tvOrderNum.setText(orderInfo.getOrder_no());
        long mill = orderInfo.getAdd_time() * 1000L;
        mBinding.tvOrderTime.setText(TimeUtils.millis2String(mill));
        mBinding.tvPayType.setText(orderInfo.getPay_type() == 1 ? "微信支付" : "支付宝");

        mBinding.tvCall.setOnClickListener(v -> {
            if (orderInfo.getShop_info() != null) {
                PhoneUtils.dial(orderInfo.getShop_info().getMobile());
            }
        });


    }


}
