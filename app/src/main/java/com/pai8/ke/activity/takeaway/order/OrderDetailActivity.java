package com.pai8.ke.activity.takeaway.order;

import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.OrderDetailAdapter;
import com.pai8.ke.activity.takeaway.contract.OrderDetailContract;
import com.pai8.ke.activity.takeaway.entity.OrderInfo;
import com.pai8.ke.activity.takeaway.entity.event.NotifyEvent;
import com.pai8.ke.activity.takeaway.entity.resq.StoreInfo;
import com.pai8.ke.activity.takeaway.presenter.OrderDetailPresenter;
import com.pai8.ke.activity.takeaway.ui.StoreActivity;
import com.pai8.ke.activity.takeaway.utils.TimeCount;
import com.pai8.ke.activity.takeaway.widget.CancelOrderPop;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.fragment.pay.PayDialogFragment;
import com.pai8.ke.utils.AppUtils;
import com.pai8.ke.utils.DateUtils;
import com.pai8.ke.utils.ImageLoadUtils;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.pai8.ke.activity.takeaway.Constants.EVENT_TYPE_REFRESH_SHOP_GOOD;
import static com.pai8.ke.utils.DateUtils.FORMAT_YYYY_MM_DD_HHMM;

public class OrderDetailActivity extends BaseMvpActivity<OrderDetailPresenter> implements View.OnClickListener, OrderDetailContract.View {


    private RecyclerView mRvOrderDetail;
    private OrderDetailAdapter mAdapter;

    private View mViewHead,mViewFooter;

    private TextView mTvStoreName;
    private ImageView mIvStore;
    private TextView mTvStatus;
    private TextView mTvStatusName;
    private TextView mTvStatusPay;
    private TextView mTvCall;

    ImageView ivMore;

    private OrderInfo mOrderInfo;


    private TextView mTvCoupon;
    private TextView mTvSendPrice;
    private TextView mTvPackPrice;
    private TextView mTvOrderTime;
    private TextView mTvOrderNum;
    private TextView mTvPrice;
    private TextView mTvDiscount;
    private TextView mTvRiderName,mTvRiderTime;

    private TextView mTvPayWay;

    private TextView mTvAdderss;

    @Override
    public OrderDetailPresenter initPresenter() {
        return new OrderDetailPresenter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    public void initView() {
        setImmersionBar(R.id.base_tool_bar);
        findViewById(R.id.toolbar_back_all).setOnClickListener(this);
     //   ivMore = findViewById(R.id.toolbar_more);
        ivMore.setOnClickListener(this);
        mRvOrderDetail = findViewById(R.id.rv_order_detail);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvOrderDetail.setLayoutManager(layoutManager);
        mAdapter = new OrderDetailAdapter(null);
        mRvOrderDetail.setAdapter(mAdapter);
        mViewHead = getLayoutInflater().inflate(R.layout.activity_order_detail_head, (ViewGroup) mRvOrderDetail.getParent(), false);
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

    }



    private void setData(OrderInfo orderInfo){
        ImageLoadUtils.setCircularImage(this, orderInfo.shop_img, mIvStore, R.mipmap.ic_launcher);
        mTvPrice.setText(orderInfo.order_price);
        mTvPackPrice.setText(orderInfo.box_price);
        mTvCoupon.setText(orderInfo.order_discount_price);
        mTvSendPrice.setText(orderInfo.express_price);
        mTvOrderNum.setText(orderInfo.order_no);
        mTvOrderTime.setText(DateUtils.millisToTime(FORMAT_YYYY_MM_DD_HHMM,orderInfo.add_time*1000));
        mAdapter.setNewData(orderInfo.goods_info);
        mTvDiscount.setText("优惠 ¥"+orderInfo.order_discount_price);
        if(orderInfo.address_info!=null){
            mTvAdderss.setText(orderInfo.address_info.address);
        }
        if(orderInfo.shop_info!=null){
            mTvStoreName.setText(orderInfo.shop_info.shop_name);
        }

        mTvPayWay.setText(orderInfo.pay_type == 1 ?"微信支付" : "支付宝");


        mTvStatusName.setText("");
        mTvStatusPay.setVisibility(View.GONE);
        if(orderInfo.order_status == 0){
            mTvStatus.setText("待支付");
            mTvStatusName.setText("请在29:59s内进行付款，否则订单讲自动取消");
            mTvStatusPay.setText("立即支付");
            mTvStatusPay.setVisibility(View.VISIBLE);
        }else if(orderInfo.order_status == 1){
            mTvStatus.setText("待商家接单");
            mTvStatusName.setText("支付成功，请等待商家接单");
            mTvStatusPay.setText("联系商家");
            mTvStatusPay.setVisibility(View.VISIBLE);
        }else if(orderInfo.order_status == 2){
            mTvStatus.setText("商品准备中");
        }else if(orderInfo.order_status == 3){
            mTvStatus.setText("商品配送中");
        }else if(orderInfo.order_status == 4){
            mTvStatus.setText("订单已完成");
            mTvStatusPay.setText("联系商家");
        }else if(orderInfo.order_status == 5){
            mTvStatus.setText("退款申请中");
            mTvStatusName.setText("你发起的退款正在申请审批中，审批成功将为您发起退款");
            mTvStatusPay.setVisibility(View.GONE);
        }else if(orderInfo.order_status == 6){

            mTvStatus.setText("拒绝退款");
            mTvStatusName.setText("您发起的退款申请审批未通过，请与商家进行联系");
            mTvStatusPay.setText("联系商家");
            mTvStatusPay.setVisibility(View.VISIBLE);
        }else if(orderInfo.order_status == 8){
            mTvStatus.setText("订单已退款");
            mTvStatusName.setText("退款完成，在5~7个工作日内欠款将原路退回到你支付时的账户");
            mTvStatusPay.setVisibility(View.GONE);
        }else if(orderInfo.order_status == 9){
            mTvStatus.setText("订单已取消");
            mTvStatusName.setText("您的订单已经取消，可重新选购下单");
            mTvStatusPay.setText("重新下单");
            mTvStatusPay.setVisibility(View.VISIBLE);
        }else if(orderInfo.order_status == -1){
            mTvStatus.setText("订单超时");

        }else if(orderInfo.order_status == -2){
            mTvStatus.setText("商家拒绝接单");
        }

        if(orderInfo.order_status == 0 || orderInfo.order_status == 4){
            ivMore.setVisibility(View.VISIBLE);
        }else{
            ivMore.setVisibility(View.GONE);
        }

        if(orderInfo.rider_info!=null){
            mTvRiderName.setText(orderInfo.rider_info.rider_name);
//            mTvRiderTime.setText("尽快送达");
        }
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.toolbar_back_all){
            finish();
        }else if(v.getId() == R.id.tv_call){
            if(mOrderInfo.shop_info!=null){
                AppUtils.intentCallPhone(this, mOrderInfo.shop_info.mobile);
            }
        }
//        else if(v.getId() == R.id.toolbar_more){
//            if(mOrderInfo.order_status == 0 || mOrderInfo.order_status == 4){
//                CancelOrderPop pop = new CancelOrderPop(this,mOrderInfo.order_status);
//                pop.setOnSelectListener(new CancelOrderPop.OnSelectListener() {
//                    @Override
//                    public void onSelect(int status) {
//                        if(status == 0){
//                            mPresenter.cancelOrder(mOrderInfo.order_no);
//                        }else{
//                            mPresenter.applyRefund(mOrderInfo.order_no);
//                        }
//
//                    }
//                });
//                pop.showPopupWindow();
//            }
//
//        }

        else if(v.getId() == R.id.tv_status_pay){


            if(mOrderInfo.order_status == 0){
                mTvStatus.setText("待支付");

                mTvStatusPay.setText("立即支付");
                PayDialogFragment payDialogFragment = PayDialogFragment.newInstance(mOrderInfo.order_price, mOrderInfo.order_no);
                payDialogFragment.show(getSupportFragmentManager(), "pay");
            }else if(mOrderInfo.order_status == 1){
                mTvStatus.setText("待商家接单");
                mTvStatusName.setText("支付成功，请等待商家接单");
                mTvStatusPay.setText("联系商家");
                if(mOrderInfo.shop_info!=null){
                    AppUtils.intentCallPhone(this, mOrderInfo.shop_info.mobile);
                }
            }else if(mOrderInfo.order_status == 2){
                mTvStatus.setText("商品准备中");

            }else if(mOrderInfo.order_status == 3){
                mTvStatus.setText("商品配送中");
            }else if(mOrderInfo.order_status == 4){
                mTvStatus.setText("订单已完成");
                mTvStatusPay.setText("联系商家");
                if(mOrderInfo.shop_info!=null){
                    AppUtils.intentCallPhone(this, mOrderInfo.shop_info.mobile);
                }
            }else if(mOrderInfo.order_status == 5){
                mTvStatus.setText("退款申请中");
                mTvStatusName.setText("你发起的退款正在申请审批中，审批成功将为您发起退款");
                mTvStatusPay.setVisibility(View.GONE);
            }else if(mOrderInfo.order_status == 6){

                mTvStatus.setText("拒绝退款");
                mTvStatusName.setText("您发起的退款申请审批未通过，请与商家进行联系");
                mTvStatusPay.setText("联系商家");
                if(mOrderInfo.shop_info!=null){
                    AppUtils.intentCallPhone(this, mOrderInfo.shop_info.mobile);
                }
                AppUtils.intentCallPhone(this, mOrderInfo.shop_info.mobile);
            }else if(mOrderInfo.order_status == 8){
                mTvStatus.setText("订单已退款");
                mTvStatusName.setText("退款完成，在5~7个工作日内欠款将原路退回到你支付时的账户");
                mTvStatusPay.setVisibility(View.GONE);
            }else if(mOrderInfo.order_status == 9){   //订单已取消
                mTvStatus.setText("订单已取消");
                mTvStatusName.setText("您的订单已经取消，可重新选购下单");
                mTvStatusPay.setText("重新下单");
                StoreInfo storeInfo = new StoreInfo();
                storeInfo.id = mOrderInfo.shop_id;
                Intent intent = new Intent(this, StoreActivity.class);
                intent.putExtra("storeInfo",storeInfo);
                intent.putExtra("orderNo",mOrderInfo.order_no);
                startActivity(intent);
            }else if(mOrderInfo.order_status == -1){
                mTvStatus.setText("订单超时");

            }else if(mOrderInfo.order_status == -2){
                mTvStatus.setText("商家拒绝接单");
            }

        }
    }


    private TimeCount time;


    public void start(){
        if(mOrderInfo.order_status == 0 && !TextUtils.isEmpty(mOrderInfo.remain_pay_time)){
            long time1 = Long.parseLong(mOrderInfo.remain_pay_time) * 1000;
            SimpleDateFormat format = new SimpleDateFormat("mm", Locale.US);
            SimpleDateFormat format1 = new SimpleDateFormat("ss", Locale.US);
            time = new TimeCount(time1, 1000);
            time.setListener(new TimeCount.OnListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onTick(long millisUntilFinished) {
                    Date date = new Date(millisUntilFinished);
                    mTvStatusName.setText("请在"+  format.format(date) + ":" +
                            format1.format(date)+"内进行付款，否则订单讲自动取消");
                    EventBus.getDefault().post(new NotifyEvent(EVENT_TYPE_REFRESH_SHOP_GOOD));


                }

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onFinish() {
                    mPresenter.orderDetail(mOrderInfo.order_no);
                }
            });

            time.start();
        }


    }




    @Override
    public void orderDetailSuccess(OrderInfo data) {
        mOrderInfo = data;
        setData(data);
        start();
    }

    @Override
    public void orderCancelSuccess(String data) {
        EventBus.getDefault().post(new NotifyEvent(EVENT_TYPE_REFRESH_SHOP_GOOD));
        mPresenter.orderDetail(mOrderInfo.order_no);
    }

    @Override
    public void getStatusSuccess(String data) {

    }
}
