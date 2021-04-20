package com.pai8.ke.activity.takeaway.order;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.ShopOrderDetailAdapter;
import com.pai8.ke.activity.takeaway.contract.ShopOrderDetailContract;
import com.pai8.ke.activity.takeaway.entity.OrderInfo;
import com.pai8.ke.activity.takeaway.entity.resq.StoreInfoResult;
import com.pai8.ke.activity.takeaway.presenter.ShopOrderDetailPresenter;
import com.pai8.ke.activity.takeaway.ui.StoreActivity;
import com.pai8.ke.activity.takeaway.widget.CancelOrderPop;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.fragment.pay.PayDialogFragment;
import com.pai8.ke.utils.AppUtils;
import com.pai8.ke.utils.DateUtils;
import com.pai8.ke.utils.ImageLoadUtils;

import java.util.List;

import static com.pai8.ke.utils.DateUtils.FORMAT_YYYY_MM_DD_HHMM;

public class ShopOrderDetailActivity extends BaseMvpActivity<ShopOrderDetailPresenter> implements View.OnClickListener, ShopOrderDetailContract.View {


    private RecyclerView mRvOrderDetail;
    private ShopOrderDetailAdapter mAdapter;

    private View mViewHead, mViewFooter;

    private TextView mTvStoreName;
    private ImageView mIvStore;
    private TextView mTvStatus;
    private TextView mTvStatusName;
    private TextView mTvStatusPay;
    private TextView mTvCall;
    private TextView mTvReject, mTvAccept;
    private LinearLayout mLlAccept;


    private ImageView ivMore;

    private OrderInfo mOrderInfo;
    private TextView mTvRiderName, mTvRiderTime;


    private TextView mTvCoupon;
    private TextView mTvSendPrice;
    private TextView mTvPackPrice;
    private TextView mTvOrderTime;
    private TextView mTvOrderNum;
    private TextView mTvPrice;
    private TextView mTvDiscount;

    private TextView mTvPayWay;

    private TextView mTvAdderss;

    @Override
    public ShopOrderDetailPresenter initPresenter() {
        return new ShopOrderDetailPresenter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_shop_order_detail;
    }

    @Override
    public void initView() {
        setImmersionBar(R.id.base_tool_bar);
        findViewById(R.id.toolbar_back_all).setOnClickListener(this);
        ivMore = findViewById(R.id.toolbar_more);
        ivMore.setOnClickListener(this);
        mRvOrderDetail = findViewById(R.id.rv_order_detail);
        mAdapter = new ShopOrderDetailAdapter(null);
        mRvOrderDetail.setAdapter(mAdapter);
        mViewHead = getLayoutInflater().inflate(R.layout.activity_order_detail_shop_head, (ViewGroup) mRvOrderDetail.getParent(), false);
        mViewFooter = getLayoutInflater().inflate(R.layout.activity_order_detail_footer, (ViewGroup) mRvOrderDetail.getParent(), false);
        mAdapter.addHeaderView(mViewHead);
        mAdapter.addFooterView(mViewFooter);
        mTvStoreName = mViewHead.findViewById(R.id.tv_store_name);
        mIvStore = mViewHead.findViewById(R.id.iv_store);
        mTvStatus = mViewHead.findViewById(R.id.tv_status);
        mTvStatusName = mViewHead.findViewById(R.id.tv_status_name);
        mTvStatusPay = mViewHead.findViewById(R.id.tv_status_pay);
        mTvStatusPay.setOnClickListener(this);
        mTvCall = mViewHead.findViewById(R.id.tv_call);
        mTvCall.setOnClickListener(this);
        mLlAccept = mViewHead.findViewById(R.id.ll_accept);
        mTvReject = mViewHead.findViewById(R.id.tv_reject);
        mTvReject.setOnClickListener(this);
        mTvAccept = mViewHead.findViewById(R.id.tv_accept);
        mTvAccept.setOnClickListener(this);
        //footer

        mTvPackPrice = mViewFooter.findViewById(R.id.tv_pack_price);
        mTvCoupon = mViewFooter.findViewById(R.id.tv_coupon);
        mTvSendPrice = mViewFooter.findViewById(R.id.tv_send_price);
        mTvOrderNum = mViewFooter.findViewById(R.id.tv_order_num);
        mTvOrderTime = mViewFooter.findViewById(R.id.tv_order_time);
        mTvAdderss = mViewFooter.findViewById(R.id.tv_address);
        mTvPrice = mViewFooter.findViewById(R.id.tv_price);
        mTvDiscount = mViewFooter.findViewById(R.id.tv_discount);
        mTvPayWay = mViewFooter.findViewById(R.id.tv_pay_type);
        mTvRiderName = mViewFooter.findViewById(R.id.tv_rider_name);
        mTvRiderTime = mViewFooter.findViewById(R.id.tv_rider_time);
    }


    @Override
    public void initData() {
        super.initData();
        mOrderInfo = (OrderInfo) getIntent().getSerializableExtra("order");
        mAdapter.setNewData(mOrderInfo.goods_info);
        setData(mOrderInfo);
        mPresenter.orderDetail(mOrderInfo.order_no);

        if (mOrderInfo.order_status == 0 || mOrderInfo.order_status == 4) {
            ivMore.setVisibility(View.VISIBLE);
        } else {
            ivMore.setVisibility(View.GONE);
        }

    }


    private void setData(OrderInfo orderInfo) {
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


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toolbar_back_all) {
            finish();
        } else if (v.getId() == R.id.tv_call) {
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

    @Override
    public void orderDetailSuccess(OrderInfo data) {
        mOrderInfo = data;
        setData(data);
    }

    @Override
    public void orderCancelSuccess(List<String> data) {
        mPresenter.orderDetail(mOrderInfo.order_no);
    }

    @Override
    public void getStatusSuccess(List<String> data) {
        {
            mPresenter.orderDetail(mOrderInfo.order_no);
        }
    }
}
