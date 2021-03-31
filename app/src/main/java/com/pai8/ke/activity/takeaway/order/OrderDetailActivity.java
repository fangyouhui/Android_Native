package com.pai8.ke.activity.takeaway.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.lhs.library.base.BaseActivity;
import com.lhs.library.base.BaseAppConstants;
import com.pai8.ke.activity.takeaway.entity.OrderDetailResult;
import com.pai8.ke.databinding.ActivityOrderDetailBinding;
import com.pai8.ke.groupBuy.viewmodel.OrderDetailViewModel;
import com.pai8.ke.shop.ui.ShopProductDetailActivity;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.utils.TimeUtil;

import org.jetbrains.annotations.Nullable;

public class OrderDetailActivity extends BaseActivity<OrderDetailViewModel, ActivityOrderDetailBinding> {

    private String orderNo;

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        orderNo = getIntent().getStringExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0);
        mBinding.btnCall.setOnClickListener(v -> {
            if (mBinding.btnCall.getTag() != null) {
                String phone = (String) mBinding.btnCall.getTag();
                PhoneUtils.dial(phone);
            }
        });

    }

    @Override
    public void addObserve() {
        mViewModel.getOrderDetailData().observe(this, data -> bindViewData(data));
    }

    @Override
    public void initData() {
        mViewModel.orderList(orderNo);
    }

    private void bindViewData(OrderDetailResult bean) {
        mBinding.btnRight.setVisibility(View.GONE);
        mBinding.btnLeft.setVisibility(View.GONE);
        if (bean.getOrder_status() == 0) {
            mBinding.tvOrderStatus.setText("待支付");
            mBinding.tvStatusName.setText("请在29:59s内进行付款，否则订单讲自动取消");
            mBinding.btnLeft.setVisibility(View.GONE);
            mBinding.btnRight.setText("立即支付");
            mBinding.btnRight.setVisibility(View.VISIBLE);
        } else if (bean.getOrder_status() == 1) {
            mBinding.tvOrderStatus.setText("待商家接单");
            mBinding.tvStatusName.setText("支付成功，请等待商家接单");
        } else if (bean.getOrder_status() == 2) {
            mBinding.tvOrderStatus.setText("商品准备中");
        } else if (bean.getOrder_status() == 3) {
            mBinding.tvOrderStatus.setText("商品配送中");
        } else if (bean.getOrder_status() == 4) {
            mBinding.tvOrderStatus.setText("已完成");
            mBinding.tvStatusName.setText("您已成功使用本次团购服务，请给个评价吧");
            mBinding.btnLeft.setText("再次购买");
            mBinding.btnRight.setText("立即评价");
            mBinding.btnRight.setVisibility(View.VISIBLE);
            mBinding.btnLeft.setVisibility(View.VISIBLE);
        } else if (bean.getOrder_status() == 5) {
            mBinding.tvOrderStatus.setText("退款申请中");
            mBinding.tvStatusName.setText("你发起的退款正在申请审批中，审批成功将为您发起退款");
        } else if (bean.getOrder_status() == 6) {
            mBinding.tvOrderStatus.setText("拒绝退款");
            mBinding.tvStatusName.setText("您发起的退款申请审批未通过，请与商家进行联系");
        } else if (bean.getOrder_status() == 8) {
            mBinding.tvOrderStatus.setText("退款成功");
            mBinding.tvStatusName.setText("退款完成，在5~7个工作日内欠款将原路退回到你支付时的账户");
        } else if (bean.getOrder_status() == 9) {
            mBinding.tvOrderStatus.setText("已取消");
            mBinding.tvStatusName.setText("您的订单已经取消，可重新选购下单");
            mBinding.btnLeft.setVisibility(View.GONE);
            mBinding.btnRight.setText("重新下单");
            mBinding.btnRight.setVisibility(View.VISIBLE);
        } else if (bean.getOrder_status() == -1) {
            mBinding.tvOrderStatus.setText("订单超时");
            mBinding.btnLeft.setVisibility(View.GONE);
            mBinding.btnRight.setText("重新下单");
            mBinding.btnRight.setVisibility(View.VISIBLE);
        } else if (bean.getOrder_status() == -2) {
            mBinding.tvOrderStatus.setText("商家拒绝接单");
        }

        ImageLoadUtils.loadImage(bean.getShop_info().getShop_img(), mBinding.ivLogo);
        mBinding.tvShopName.setText(bean.getShop_info().getShop_name());
        mBinding.tvFraction.setText(bean.getShop_info().getScore() + "");
        mBinding.tvTotalSaleCount.setText("总销量：" + bean.getShop_info().getSale_count());
        mBinding.tvAddress.setText("地址：" + bean.getShop_info().getAddress());
        mBinding.tvPhone.setText("电话：" + bean.getShop_info().getMobile());
        mBinding.btnCall.setTag(bean.getShop_info().getMobile());

        OrderDetailResult.Goods_info info = bean.getGoods_info().get(0);
        if (info != null) {
            ImageLoadUtils.loadImage(info.getGoods_img().get(0), mBinding.ivProductImg);
            mBinding.tvProductName.setText(info.getGoods_title());
            mBinding.tvDesc.setText(info.getDesc());
            mBinding.tvCount2.setText("X" + bean.getGoods_info().size());
            mBinding.tvGroupBuyPrice.setText("¥" + info.getGoods_sell_price());
            mBinding.tvOriginPrice.setText("¥" + info.getGoods_origin_price());
        }

        mBinding.tvProductPrice.setText("¥" + bean.getOrder_price());
        mBinding.tvFullDiscountPrice.setText("- ¥ " + bean.getOrder_discount_price());
        mBinding.tvDiscountPrice.setText("优惠 ¥" + bean.getOrder_discount_price());
        mBinding.tvTotalPrice.setText("¥" + bean.getOrder_price());
        String startTime = "", endTime = "";
        if (bean.getTerm() != null) {
            startTime = TimeUtil.timeStampToString(bean.getTerm().getStart_time());
            endTime = TimeUtil.timeStampToString(bean.getTerm().getEnd_time());
        }
        mBinding.tvTermTime.setText(String.format("%s至%s （周末、法定节假日通用）", startTime, endTime));
        mBinding.tvMatter.setText(bean.getMatter());
        mBinding.tvContent.setText(bean.getBuyer_name());
        mBinding.tvPhone2.setText(bean.getBuyer_phone());
        mBinding.tvOrderNo.setText(bean.getOrder_no());
        long mill = bean.getAdd_time() * 1000L;
        mBinding.tvOrderTime.setText(TimeUtils.millis2String(mill));

        mBinding.btnRight.setOnClickListener(v -> {
            if (bean.getOrder_status() == 9 || -1 == bean.getOrder_status()) {
                Intent intent = new Intent(this, ShopProductDetailActivity.class);
                intent.putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0, bean.getShop_id() + "");
                intent.putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_1, bean.getId() + "");
                startActivity(intent);
            }
        });


    }


}
