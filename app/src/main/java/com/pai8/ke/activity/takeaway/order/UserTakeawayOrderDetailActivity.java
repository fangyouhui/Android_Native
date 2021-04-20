package com.pai8.ke.activity.takeaway.order;

import android.os.Bundle;
import android.view.View;

import com.lhs.library.base.BaseActivity;
import com.lhs.library.base.BaseAppConstants;
import com.pai8.ke.activity.takeaway.entity.OrderDetailResult;
import com.pai8.ke.databinding.ActivityUserTakeawayOrderDetailBinding;
import com.pai8.ke.groupBuy.viewmodel.UserOrderDetailViewModel;
import com.pai8.ke.utils.AppUtils;
import com.pai8.ke.utils.ImageLoadUtils;

import org.jetbrains.annotations.Nullable;

import java.util.List;

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
        ImageLoadUtils.setCircularImage(this, orderInfo.shop_img, mIvStore, R.mipmap.ic_launcher);
        mTvPrice.setText(orderInfo.order_price);
        mTvPackPrice.setText(orderInfo.box_price);
        mTvCoupon.setText(orderInfo.order_discount_price);
        mTvSendPrice.setText(orderInfo.express_price);
        mTvOrderNum.setText(orderInfo.order_no);
        mTvOrderTime.setText(DateUtils.millisToTime(FORMAT_YYYY_MM_DD_HHMM, orderInfo.add_time * 1000));
        mAdapter.setNewData(orderInfo.goods_info);
        mTvDiscount.setText("优惠 ¥" + orderInfo.order_discount_price);
        if (orderInfo.address_info != null) {
            mTvAdderss.setText(orderInfo.address_info.address);
        }
        if (orderInfo.shop_info != null) {
            mTvStoreName.setText(orderInfo.shop_info.shop_name);
        }

        mTvPayWay.setText(orderInfo.pay_type == 1 ? "微信支付" : "支付宝");


        mTvStatusName.setText("");
        mTvStatusPay.setVisibility(View.GONE);
        mLlAccept.setVisibility(View.GONE);
        if (orderInfo.order_status == 0) {


        } else if (orderInfo.order_status == 1) {
            mLlAccept.setVisibility(View.VISIBLE);
            mTvStatus.setText("待接单");
            mTvStatusName.setText("请在29:59s内进行订单处理，否则订单将自动取消");
        } else if (orderInfo.order_status == 2) {
            mTvStatus.setText("商品准备中");
            mTvStatusName.setText("请在29:59s内进行订单处理，否则订单将自动取消");
            mTvStatusName.setText("请尽快准备商品，准备完成点击下放按钮并安排配送");
            mTvStatusPay.setText("商品制作完成");
            mTvStatusPay.setVisibility(View.VISIBLE);
        } else if (orderInfo.order_status == 3) {
            mTvStatus.setText("商品已送出");
            mTvStatusName.setText("商品送出，若商品已送达则点击下方送达按钮");
            mTvStatusPay.setVisibility(View.GONE);
        } else if (orderInfo.order_status == 4) {
            mTvStatus.setText("订单已完成");
            mTvStatusName.setText("商品已送达，订单已完成");
        } else if (orderInfo.order_status == 5) {
            mTvStatus.setText("退款申请");
            mTvStatusName.setText("用户发起退款申请，请联系客户并及时处理");
            mLlAccept.setVisibility(View.VISIBLE);
            mTvReject.setText("拒绝退款");
            mTvAccept.setText("同意退款");

        } else if (orderInfo.order_status == 6) {
            mTvStatus.setText("拒绝退款");
            mTvStatusName.setText("您发起的退款申请审批未通过，请与商家进行联系");
            mTvStatusPay.setText("联系商家");
            mTvStatusPay.setVisibility(View.VISIBLE);
        } else if (orderInfo.order_status == 8) {
            mTvStatus.setText("订单已退款");
            mTvStatusName.setText("退款完成，在5~7个工作日内欠款将原路退回到你支付时的账户");
            mTvStatusPay.setVisibility(View.GONE);
        } else if (orderInfo.order_status == 9) {
            mTvStatus.setText("订单已取消");
            mTvStatusName.setText("您的订单已经取消，可重新选购下单");
            mTvStatusPay.setText("重新下单");
            mTvStatusPay.setVisibility(View.VISIBLE);
        } else if (orderInfo.order_status == -1) {
            mTvStatus.setText("订单超时");

        } else if (orderInfo.order_status == -2) {
            mTvStatus.setText("商家拒绝接单");
        }

        if (orderInfo.rider_info != null) {
            mTvRiderName.setText(orderInfo.rider_info.rider_name);
//            mTvRiderTime.setText("尽快送达")
        }
    }


    private void onClick(View v) {
        if (v.getId() == R.id.tv_call) {
            if (mOrderInfo.shop_info != null) {
                AppUtils.intentCallPhone(this, mOrderInfo.shop_info.mobile);
            }
        } else if (v.getId() == R.id.toolbar_more) {
            if (mOrderInfo.order_status == 0 || mOrderInfo.order_status == 4) {
                CancelOrderPop pop = new CancelOrderPop(this, mOrderInfo.order_status);
                pop.setOnSelectListener(new CancelOrderPop.OnSelectListener() {
                    @Override
                    public void onSelect(int status) {
                        if (status == 0) {
                            mPresenter.cancelOrder(mOrderInfo.order_no);
                        } else {
                            mPresenter.applyRefund(mOrderInfo.order_no);
                        }

                    }
                });
                pop.showPopupWindow();
            }

        } else if (v.getId() == R.id.tv_status_pay) {

            if (mOrderInfo.order_status == 0) {
                mTvStatus.setText("待支付");
                mTvStatusName.setText("请在29:59s内进行付款，否则订单讲自动取消");
                mTvStatusPay.setText("立即支付");
                PayDialogFragment payDialogFragment = PayDialogFragment.newInstance(mOrderInfo.order_price, mOrderInfo.order_no);
                payDialogFragment.show(getSupportFragmentManager(), "pay");
            } else if (mOrderInfo.order_status == 1) {
                mTvStatus.setText("待商家接单");
                mTvStatusName.setText("支付成功，请等待商家接单");
                mTvStatusPay.setText("联系商家");
                if (mOrderInfo.shop_info != null) {
                    AppUtils.intentCallPhone(this, mOrderInfo.shop_info.mobile);
                }
            } else if (mOrderInfo.order_status == 2) {
                mTvStatus.setText("商品准备中");

            } else if (mOrderInfo.order_status == 3) {
                mTvStatus.setText("商品配送中");
            } else if (mOrderInfo.order_status == 4) {
                mTvStatus.setText("订单已完成");
                mTvStatusPay.setText("联系商家");
                if (mOrderInfo.shop_info != null) {
                    AppUtils.intentCallPhone(this, mOrderInfo.shop_info.mobile);
                }
            } else if (mOrderInfo.order_status == 5) {
                mTvStatus.setText("退款申请中");
                mTvStatusName.setText("你发起的退款正在申请审批中，审批成功将为您发起退款");
                mTvStatusPay.setVisibility(View.GONE);
            } else if (mOrderInfo.order_status == 6) {

                mTvStatus.setText("拒绝退款");
                mTvStatusName.setText("您发起的退款申请审批未通过，请与商家进行联系");
                mTvStatusPay.setText("联系商家");
                if (mOrderInfo.shop_info != null) {
                    AppUtils.intentCallPhone(this, mOrderInfo.shop_info.mobile);
                }
                AppUtils.intentCallPhone(this, mOrderInfo.shop_info.mobile);
            } else if (mOrderInfo.order_status == 8) {
                mTvStatus.setText("订单已退款");
                mTvStatusName.setText("退款完成，在5~7个工作日内欠款将原路退回到你支付时的账户");
                mTvStatusPay.setVisibility(View.GONE);
            } else if (mOrderInfo.order_status == 9) {   //订单已取消
                mTvStatus.setText("订单已取消");
                mTvStatusName.setText("您的订单已经取消，可重新选购下单");
                mTvStatusPay.setText("重新下单");
                StoreInfoResult storeInfo = new StoreInfoResult();
                storeInfo.id = mOrderInfo.shop_id;
                Intent intent = new Intent(this, StoreActivity.class);
                intent.putExtra("storeInfo", storeInfo);
                intent.putExtra("orderNo", mOrderInfo.order_no);
                startActivity(intent);
            } else if (mOrderInfo.order_status == -1) {
                mTvStatus.setText("订单超时");

            } else if (mOrderInfo.order_status == -2) {
                mTvStatus.setText("商家拒绝接单");
            }
            //0为接单 1为拒绝订单 2为同意退款申请 3为拒绝退款申请 4为订单制作完成 5为订单配送操作

        } else if (v.getId() == R.id.tv_reject) {

            if (mOrderInfo.order_status == 1) {  //带接单

                mPresenter.shopDealOrder(mOrderInfo.order_no, 1);
            } else if (mOrderInfo.order_status == 5) {
                mPresenter.shopDealOrder(mOrderInfo.order_no, 3);
            }


        } else if (v.getId() == R.id.tv_accept) {

            if (mOrderInfo.order_status == 1) {  //带接单

                mPresenter.shopDealOrder(mOrderInfo.order_no, 0);
            } else if (mOrderInfo.order_status == 5) {
                mPresenter.shopDealOrder(mOrderInfo.order_no, 2);
            }

        }
    }

    public void orderDetailSuccess(OrderInfo data) {
        mOrderInfo = data;
        setData(data);
    }

    public void orderCancelSuccess(List<String> data) {
        mPresenter.orderDetail(mOrderInfo.order_no);
    }

    public void getStatusSuccess(List<String> data) {

        mPresenter.orderDetail(mOrderInfo.order_no);

    }
}
