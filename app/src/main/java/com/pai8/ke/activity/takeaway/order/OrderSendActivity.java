package com.pai8.ke.activity.takeaway.order;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.gyf.immersionbar.ImmersionBar;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.OrderDetailAdapter;
import com.pai8.ke.activity.takeaway.contract.OrderDetailContract;
import com.pai8.ke.activity.takeaway.entity.OrderInfo;
import com.pai8.ke.activity.takeaway.presenter.OrderDetailPresenter;
import com.pai8.ke.app.MyApp;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.utils.DateUtils;
import com.pai8.ke.utils.ImageLoadUtils;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import static com.pai8.ke.utils.DateUtils.FORMAT_YYYY_MM_DD_HHMM;

public class OrderSendActivity extends BaseMvpActivity<OrderDetailPresenter> implements View.OnClickListener, OrderDetailContract.View {

    @BindView(R.id.map_view)
    MapView mMapView;
    @BindView(R.id.iv_btn_back)
    ImageView ivBtnBack;
    @BindView(R.id.rv_order)
    RecyclerView rvOrder;

    private AMap mAMap;
    private UiSettings mUiSettings;
    private AMapLocation mAMapLocation;
    private RecyclerView mRvOrderDetail;
    private OrderDetailAdapter mAdapter;

    private View mViewHead, mViewFooter;

    private TextView mTvStoreName;
    private ImageView mIvStore;
    private TextView mTvStatus;
    private TextView mTvStatusName;
    private TextView mTvStatusPay;
    private TextView mTvCall;
    private TextView mTvPayType;


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

    private TextView mTvAdderss;


    @Override
    public int getLayoutId() {
        return R.layout.activity_order_send;
    }

    @Override
    public void initCreate(Bundle savedInstanceState) {
        super.initCreate(savedInstanceState);
        mMapView.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        ImmersionBar.with(this)
                .transparentStatusBar()
                .statusBarDarkFont(true)
                .titleBarMarginTop(ivBtnBack)
                .init();
        initMapView();
        mAMapLocation = MyApp.mAMapLocation;
        mRvOrderDetail = findViewById(R.id.rv_order);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvOrderDetail.setLayoutManager(layoutManager);
        mAdapter = new OrderDetailAdapter(null);
        mRvOrderDetail.setAdapter(mAdapter);
        mViewHead = getLayoutInflater().inflate(R.layout.activity_send_order_detail_head, (ViewGroup) mRvOrderDetail.getParent(), false);
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
        mTvPayType = mViewFooter.findViewById(R.id.tv_pay_type);
        mTvRiderName = mViewFooter.findViewById(R.id.tv_rider_name);
        mTvRiderTime = mViewFooter.findViewById(R.id.tv_rider_time);

    }

    private void initMapView() {
        mAMap = mMapView.getMap();
        mUiSettings = mAMap.getUiSettings();
        // 地图不能倾斜
        mUiSettings.setTiltGesturesEnabled(false);
        // 不能旋转
        mUiSettings.setRotateGesturesEnabled(false);
        // 不显示缩放按钮，加号减号
        mUiSettings.setZoomControlsEnabled(false);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        super.initData();
        mOrderInfo = (OrderInfo) getIntent().getSerializableExtra("order");
        mAdapter.setNewData(mOrderInfo.goods_info);
        setData(mOrderInfo);
        mPresenter.orderDetail(mOrderInfo.order_no);
    }


    private void setData(OrderInfo orderInfo) {
        ImageLoadUtils.setCircularImage(this, orderInfo.shop_img, mIvStore, R.mipmap.ic_launcher);
        mTvPrice.setText(orderInfo.order_price);
        mTvPackPrice.setText(orderInfo.box_price);
        mTvCoupon.setText(orderInfo.order_discount_price);
        mTvSendPrice.setText(orderInfo.express_price);
        mTvOrderNum.setText(orderInfo.order_no);
        mTvOrderTime.setText(DateUtils.millisToTime(FORMAT_YYYY_MM_DD_HHMM, orderInfo.add_time*1000));
        mAdapter.setNewData(orderInfo.goods_info);
        mTvDiscount.setText("优惠 ¥" + orderInfo.order_discount_price);

        mTvPayType.setText(orderInfo.pay_type == 1 ?"微信支付" : "支付宝");

        if (orderInfo.address_info != null) {
            mTvAdderss.setText(orderInfo.address_info.address);
        }
        if (orderInfo.shop_info != null) {
            mTvStoreName.setText(orderInfo.shop_info.shop_name);
        }

        if(orderInfo.rider_info!=null){
            mTvRiderName.setText(orderInfo.rider_info.rider_name);
            mTvRiderTime.setText("尽快送达");
        }


        if (orderInfo.order_status == 0) {
            mTvStatus.setText("待支付");
            mTvStatusName.setText("请在29:59s内进行付款，否则订单讲自动取消");
            mTvStatusPay.setText("立即支付");
        } else if (orderInfo.order_status == 1) {
            mTvStatus.setText("待商家接单");
            mTvStatusName.setText("支付成功，请等待商家接单");
            mTvStatusPay.setText("联系商家");
        } else if (orderInfo.order_status == 2) {
            mTvStatus.setText("商品准备中");

        } else if (orderInfo.order_status == 3) {
            mTvStatus.setText("商品配送中");
        } else if (orderInfo.order_status == 4) {
            mTvStatus.setText("订单已完成");
            mTvStatusPay.setText("联系商家");
        } else if (orderInfo.order_status == 5) {
            mTvStatus.setText("退款申请中");
            mTvStatusName.setText("你发起的退款正在申请审批中，审批成功将为您发起退款");
            mTvStatusPay.setVisibility(View.INVISIBLE);
        } else if (orderInfo.order_status == 6) {

            mTvStatus.setText("拒绝退款");
            mTvStatusName.setText("您发起的退款申请审批未通过，请与商家进行联系");
            mTvStatusPay.setText("联系商家");

        } else if (orderInfo.order_status == 8) {
            mTvStatus.setText("订单已退款");
            mTvStatusName.setText("退款完成，在5~7个工作日内欠款将原路退回到你支付时的账户");
            mTvStatusPay.setVisibility(View.INVISIBLE);
        } else if (orderInfo.order_status == 9) {
            mTvStatus.setText("订单已取消");
            mTvStatusName.setText("您的订单已经取消，可重新选购下单");
            mTvStatusPay.setText("重新下单");
        } else if (orderInfo.order_status == -1) {
            mTvStatus.setText("订单超时");

        } else if (orderInfo.order_status == -2) {
            mTvStatus.setText("商家拒绝接单");
        }





    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    private void moveMapCamera(double latitude, double longitude) {
        mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 14));
    }

    @Override
    public OrderDetailPresenter initPresenter() {
        return new OrderDetailPresenter(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @OnClick({R.id.iv_btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_btn_back:
                finish();
                break;


        }
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void orderDetailSuccess(OrderInfo data) {
        mOrderInfo = data;
        setData(data);
        if(!TextUtils.isEmpty(mOrderInfo.address_info.latitude)&&!TextUtils.isEmpty(mOrderInfo.address_info.longitude)){
            moveMapCamera(Double.parseDouble(mOrderInfo.address_info.latitude),Double.parseDouble(mOrderInfo.address_info.longitude));
        }
    }


    @Override
    public void orderCancelSuccess(String data) {

    }

    @Override
    public void getStatusSuccess(String data) {

    }
}
