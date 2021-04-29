package com.pai8.ke.activity.takeaway.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.lhs.library.base.BaseActivity;
import com.lhs.library.base.BaseAppConstants;
import com.pai8.ke.activity.takeaway.adapter.ShopOrderDetailAdapter;
import com.pai8.ke.activity.takeaway.entity.OrderDetailResult;
import com.pai8.ke.activity.takeaway.entity.resq.StoreInfoResult;
import com.pai8.ke.activity.takeaway.ui.StoreActivity;
import com.pai8.ke.activity.takeaway.widget.CancelOrderPop;
import com.pai8.ke.databinding.ActivityShopOrderDetailBinding;
import com.pai8.ke.fragment.pay.PayDialogFragment;
import com.pai8.ke.viewmodel.ShopOrderViewModel;

import org.jetbrains.annotations.Nullable;

public class ShopOrderDetailActivity extends BaseActivity<ShopOrderViewModel, ActivityShopOrderDetailBinding> {

    private ShopOrderDetailAdapter mAdapter;
    private OrderDetailResult mOrderInfo;

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mAdapter = new ShopOrderDetailAdapter(this, null);
        mBinding.recyclerView.setAdapter(mAdapter);
        initListener();
    }


    @Override
    public void initData() {
        String orderNo = getIntent().getStringExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0);
        mViewModel.orderDetail(orderNo);
    }

    @Override
    public void addObserve() {
        mViewModel.getOrderDetailData().observe(this, data -> {
            mOrderInfo = data;
            bindViewData(data);
        });

        mViewModel.getCancelOrderData().observe(this, data -> mViewModel.orderDetail(mOrderInfo.getOrder_no()));
        mViewModel.getShopDealOrderData().observe(this, data -> mViewModel.orderDetail(mOrderInfo.getOrder_no()));
        mViewModel.getApplyRefundData().observe(this, data -> mViewModel.orderDetail(mOrderInfo.getOrder_no()));
    }

    private void bindViewData(OrderDetailResult orderInfo) {
        if (orderInfo.getOrder_status() == 0 || orderInfo.getOrder_status() == 4) {
            mBinding.toolbarMore.setVisibility(View.VISIBLE);
        } else {
            mBinding.toolbarMore.setVisibility(View.GONE);
        }
        //     ImageLoadUtils.setCircularImage(this, orderInfo.get,  mBinding.buyerAvatar, R.mipmap.img_head_def);
        mBinding.tvPrice.setText(orderInfo.getOrder_price());
        mBinding.tvPackPrice.setText(orderInfo.getBox_price());
        mBinding.tvCoupon.setText(orderInfo.getOrder_discount_price());
        mBinding.tvSendPrice.setText(orderInfo.getExpress_price());
        mBinding.tvOrderNum.setText(orderInfo.getOrder_no());
        mBinding.tvOrderTime.setText(TimeUtils.millis2String(orderInfo.getAdd_time() * 1000L));

        mAdapter.setData(orderInfo.getGoods_info());

        mBinding.tvDiscount.setText("优惠 ¥" + orderInfo.getOrder_discount_price());
        if (orderInfo.getAddress_info() != null) {
            mBinding.tvAddress.setText(orderInfo.getAddress_info().getAddress() + orderInfo.getAddress_info().getHouse_number());
        }
        if (orderInfo.getShop_info() != null) {
            mBinding.tvStoreName.setText(orderInfo.getShop_info().getShop_name());
        }

        mBinding.tvPayType.setText(orderInfo.getPay_type() == 1 ? "微信支付" : "支付宝");

        mBinding.tvStatusName.setText("");
        mBinding.tvStatusPay.setVisibility(View.GONE);
        mBinding.llAccept.setVisibility(View.GONE);
        if (orderInfo.getOrder_status() == 0) {


        } else if (orderInfo.getOrder_status() == 1) {
            mBinding.llAccept.setVisibility(View.VISIBLE);
            mBinding.tvStatus.setText("待接单");
            mBinding.tvStatusName.setText("请在29:59s内进行订单处理，否则订单将自动取消");
        } else if (orderInfo.getOrder_status() == 2) {
            mBinding.tvStatus.setText("商品准备中");
            mBinding.tvStatusName.setText("请在29:59s内进行订单处理，否则订单将自动取消");
            mBinding.tvStatusName.setText("请尽快准备商品，准备完成点击下放按钮并安排配送");
            mBinding.tvStatusPay.setText("商品制作完成");
            mBinding.tvStatusPay.setVisibility(View.VISIBLE);
        } else if (orderInfo.getOrder_status() == 3) {
            mBinding.tvStatus.setText("商品已送出");
            mBinding.tvStatusName.setText("商品送出，若商品已送达则点击下方送达按钮");
            mBinding.tvStatusPay.setVisibility(View.GONE);
        } else if (orderInfo.getOrder_status() == 4) {
            mBinding.tvStatus.setText("订单已完成");
            mBinding.tvStatusName.setText("商品已送达，订单已完成");
        } else if (orderInfo.getOrder_status() == 5) {
            mBinding.tvStatus.setText("退款申请");
            mBinding.tvStatusName.setText("用户发起退款申请，请联系客户并及时处理");
            mBinding.llAccept.setVisibility(View.VISIBLE);
            mBinding.btnLeft.setText("拒绝退款");
            mBinding.btnRight.setText("同意退款");
        } else if (orderInfo.getOrder_status() == 6) {
            mBinding.tvStatus.setText("拒绝退款");
            mBinding.tvStatusName.setText("您发起的退款申请审批未通过，请与商家进行联系");
            mBinding.tvStatusPay.setText("联系商家");
            mBinding.tvStatusPay.setVisibility(View.VISIBLE);
        } else if (orderInfo.getOrder_status() == 8) {
            mBinding.tvStatus.setText("订单已退款");
            mBinding.tvStatusName.setText("退款完成，在5~7个工作日内欠款将原路退回到你支付时的账户");
            mBinding.tvStatusPay.setVisibility(View.GONE);
        } else if (orderInfo.getOrder_status() == 9) {
            mBinding.tvStatus.setText("订单已取消");
            mBinding.tvStatusName.setText("您的订单已经取消，可重新选购下单");
            mBinding.tvStatusPay.setText("重新下单");
            mBinding.tvStatusPay.setVisibility(View.VISIBLE);
        } else if (orderInfo.getOrder_status() == -1) {
            mBinding.tvStatus.setText("订单超时");
        } else if (orderInfo.getOrder_status() == -2) {
            mBinding.tvStatus.setText("商家拒绝接单");
        }

        if (orderInfo.getRider_info() != null) {
            mBinding.tvRiderName.setText(orderInfo.getRider_info().getRider_name());
//            mTvRiderTime.setText("尽快送达")
        }
    }

    private void initListener() {
        mBinding.toolbarBackAll.setOnClickListener(v -> finish());
        mBinding.tvCall.setOnClickListener(v -> {
            if (mOrderInfo.getShop_info() != null) {
                PhoneUtils.dial(mOrderInfo.getShop_info().getMobile());
            }
        });
        mBinding.toolbarMore.setOnClickListener(v -> {
            if (mOrderInfo.getOrder_status() == 0 || mOrderInfo.getOrder_status() == 4) {
                CancelOrderPop pop = new CancelOrderPop(this, mOrderInfo.getOrder_status());
                pop.setOnSelectListener(new CancelOrderPop.OnSelectListener() {
                    @Override
                    public void onSelect(int status) {
                        if (status == 0) {
                            mViewModel.cancelOrder(mOrderInfo.getOrder_no());
                        } else {
                            mViewModel.applyRefund(mOrderInfo.getOrder_no());
                        }

                    }
                });
                pop.showPopupWindow();
            }
        });

        mBinding.tvStatusPay.setOnClickListener(v -> {
            if (mOrderInfo.getOrder_status() == 0) {
                mBinding.tvStatus.setText("待支付");
                mBinding.tvStatusName.setText("请在29:59s内进行付款，否则订单讲自动取消");
                mBinding.tvStatusPay.setText("立即支付");
                PayDialogFragment payDialogFragment = PayDialogFragment.newInstance(mOrderInfo.getOrder_price(), mOrderInfo.getOrder_no());
                payDialogFragment.show(getSupportFragmentManager(), "pay");
            } else if (mOrderInfo.getOrder_status() == 1) {
                mBinding.tvStatus.setText("待商家接单");
                mBinding.tvStatusName.setText("支付成功，请等待商家接单");
                mBinding.tvStatusPay.setText("联系商家");
                if (mOrderInfo.getShop_info() != null) {
                    PhoneUtils.dial(mOrderInfo.getShop_info().getMobile());
                }
            } else if (mOrderInfo.getOrder_status() == 2) {
                mBinding.tvStatus.setText("商品准备中");
            } else if (mOrderInfo.getOrder_status() == 3) {
                mBinding.tvStatus.setText("商品配送中");
            } else if (mOrderInfo.getOrder_status() == 4) {
                mBinding.tvStatus.setText("订单已完成");
                mBinding.tvStatusPay.setText("联系商家");
                if (mOrderInfo.getShop_info() != null) {
                    PhoneUtils.dial(mOrderInfo.getShop_info().getMobile());
                }
            } else if (mOrderInfo.getOrder_status() == 5) {
                mBinding.tvStatus.setText("退款申请中");
                mBinding.tvStatusName.setText("你发起的退款正在申请审批中，审批成功将为您发起退款");
                mBinding.tvStatusPay.setVisibility(View.GONE);
            } else if (mOrderInfo.getOrder_status() == 6) {
                mBinding.tvStatus.setText("拒绝退款");
                mBinding.tvStatusName.setText("您发起的退款申请审批未通过，请与商家进行联系");
                mBinding.tvStatusPay.setText("联系商家");
                if (mOrderInfo.getShop_info() != null) {
                    PhoneUtils.dial(mOrderInfo.getShop_info().getMobile());
                }
            } else if (mOrderInfo.getOrder_status() == 8) {
                mBinding.tvStatus.setText("订单已退款");
                mBinding.tvStatusName.setText("退款完成，在5~7个工作日内欠款将原路退回到你支付时的账户");
                mBinding.tvStatusPay.setVisibility(View.GONE);
            } else if (mOrderInfo.getOrder_status() == 9) {   //订单已取消
                mBinding.tvStatus.setText("订单已取消");
                mBinding.tvStatusName.setText("您的订单已经取消，可重新选购下单");
                mBinding.tvStatusPay.setText("重新下单");
                StoreInfoResult storeInfo = new StoreInfoResult();
                storeInfo.id = mOrderInfo.getShop_id();
                Intent intent = new Intent(this, StoreActivity.class);
                intent.putExtra("storeInfo", storeInfo);
                intent.putExtra("orderNo", mOrderInfo.getOrder_no());
                startActivity(intent);
            } else if (mOrderInfo.getOrder_status() == -1) {
                mBinding.tvStatus.setText("订单超时");
            } else if (mOrderInfo.getOrder_status() == -2) {
                mBinding.tvStatus.setText("商家拒绝接单");
            }
            //0为接单 1为拒绝订单 2为同意退款申请 3为拒绝退款申请 4为订单制作完成 5为订单配送操作

        });

        mBinding.btnLeft.setOnClickListener(v -> {
            if (mOrderInfo.getOrder_status() == 1) {  //带接单
                mViewModel.shopDealOrder(mOrderInfo.getOrder_no(), 1);
            } else if (mOrderInfo.getOrder_status() == 5) {
                mViewModel.shopDealOrder(mOrderInfo.getOrder_no(), 3);
            }
        });

        mBinding.btnRight.setOnClickListener(v -> {
            if (mOrderInfo.getOrder_status() == 1) {  //带接单
                mViewModel.shopDealOrder(mOrderInfo.getOrder_no(), 0);
            } else if (mOrderInfo.getOrder_status() == 5) {
                mViewModel.shopDealOrder(mOrderInfo.getOrder_no(), 2);
            }
        });
    }

}
