package com.pai8.ke.activity.takeaway.order;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.lhs.library.base.BaseActivity;
import com.lhs.library.base.BaseAppConstants;
import com.pai8.ke.activity.takeaway.entity.GoodsInfo;
import com.pai8.ke.activity.takeaway.entity.OrderDetailResult;
import com.pai8.ke.databinding.ActivityOrderDetailBinding;
import com.pai8.ke.groupBuy.viewmodel.UserOrderDetailViewModel;
import com.pai8.ke.qrcode.QRCodeTools;
import com.pai8.ke.shop.ui.CommentActivity;
import com.pai8.ke.shop.ui.LookCommentActivity;
import com.pai8.ke.shop.ui.PayBottomDialogFragment;
import com.pai8.ke.shop.ui.ShopProductDetailActivity;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.utils.TimeUtil;

import org.jetbrains.annotations.Nullable;

public class UserGroupOrderDetailActivity extends BaseActivity<UserOrderDetailViewModel, ActivityOrderDetailBinding> {

    private String orderNo;
    private ActivityResultLauncher activityResultLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                initData();
            }
        });
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        orderNo = getIntent().getStringExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0);
        mBinding.btnCall.setOnClickListener(v -> {
            if (mBinding.btnCall.getTag() != null) {
                String phone = (String) mBinding.btnCall.getTag();
                PhoneUtils.dial(phone);
            }
        });
        mBinding.btnRefund.setOnClickListener(v -> mViewModel.applyRefund(orderNo));
    }

    @Override
    public void addObserve() {
        mViewModel.getOrderDetailData().observe(this, data -> bindViewData(data));
        mViewModel.getApplyRefundData().observe(this, data -> {
            initData();
        });
    }

    @Override
    public void initData() {
        mViewModel.orderDetail(orderNo);
    }

    private void bindViewData(OrderDetailResult bean) {
        mBinding.btnRight.setVisibility(View.GONE);
        mBinding.btnLeft.setVisibility(View.GONE);
        mBinding.ivQrCode.setVisibility(View.GONE);
        mBinding.flRefund.setVisibility(View.GONE);
        if (bean.getOrder_status() == 0) {
            mBinding.tvOrderStatus.setText("待支付");
            mBinding.tvStatusName.setText("请在29:59s内进行付款，否则订单讲自动取消");
            mBinding.btnLeft.setVisibility(View.GONE);
            mBinding.btnRight.setText("立即支付");
            mBinding.btnRight.setVisibility(View.VISIBLE);
        } else if (bean.getOrder_status() == 1) {
            mBinding.tvOrderStatus.setText("待使用");
            mBinding.tvStatusName.setText("你已成功购买团购商品，请到店消费时出示二维码");
            Bitmap bitmap = QRCodeTools.createCode(bean.getOrder_no(), 200, true);
            mBinding.ivQrCode.setVisibility(View.VISIBLE);
            mBinding.ivQrCode.setImageBitmap(bitmap);
            mBinding.flRefund.setVisibility(View.VISIBLE);
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
        } else if (bean.getOrder_status() == 10) {
            mBinding.tvOrderStatus.setText("已完成");
            mBinding.tvStatusName.setText("你的订单已完成");
            mBinding.btnLeft.setText("重新下单");
            mBinding.btnRight.setText("查看评价");
            mBinding.btnRight.setVisibility(View.VISIBLE);
            mBinding.btnLeft.setVisibility(View.VISIBLE);
        }

        ImageLoadUtils.loadImage(bean.getShop_info().getShop_img(), mBinding.ivLogo);
        mBinding.tvShopName.setText(bean.getShop_info().getShop_name());
        mBinding.tvFraction.setText(bean.getShop_info().getScore() + "");
        mBinding.tvTotalSaleCount.setText("总销量：" + bean.getShop_info().getSale_count());
        mBinding.tvAddress.setText("地址：" + bean.getShop_info().getAddress());
        mBinding.tvPhone.setText("电话：" + bean.getShop_info().getMobile());
        mBinding.btnCall.setTag(bean.getShop_info().getMobile());

        GoodsInfo info = bean.getGoods_info().get(0);
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
        if (!TextUtils.isEmpty(info.getTerm())) {
            String[] times = info.getTerm().split("-");
            String startTime = TimeUtil.timeStampToString(times[0]);
            String endTime = TimeUtil.timeStampToString(times[1]);
            mBinding.tvTermTime.setText(String.format("%s至%s （周末、法定节假日通用）", startTime, endTime));
        }
        mBinding.tvMatter.setText(info.getMatter());

        mBinding.tvContent.setText(bean.getBuyer_name());
        mBinding.tvPhone2.setText(bean.getBuyer_phone());
        mBinding.tvOrderNo.setText(bean.getOrder_no());
        long mill = bean.getAdd_time() * 1000L;
        mBinding.tvOrderTime.setText(TimeUtils.millis2String(mill));

        mBinding.btnRight.setOnClickListener(v -> {
            if (bean.getOrder_status() == 9 || -1 == bean.getOrder_status()) {
                toProductDetailActivity(bean);
            } else if (4 == bean.getOrder_status()) { //立即评价
                toCommentActivity(bean);
            } else if (0 == bean.getOrder_status()) {//待支付
                PayBottomDialogFragment paySelectBottomDialog = PayBottomDialogFragment.newInstance(mBinding.tvTotalPrice.getTag().toString(), bean.getOrder_no());
                paySelectBottomDialog.showNow(getSupportFragmentManager(), "payWay");
            }
        });

        mBinding.btnLeft.setOnClickListener(v -> {
            if (4 == bean.getOrder_status() || 10 == bean.getOrder_status()) { //再次购买
                toProductDetailActivity(bean);
            }
        });

        mBinding.btnRight.setOnClickListener(v -> {
            if (10 == bean.getOrder_status()) { //查看评价
                Intent intent = new Intent(this, LookCommentActivity.class);
                intent.putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0, bean);
                startActivity(intent);
            } else if (4 == bean.getOrder_status()) { //立即评价
                toCommentActivity(bean);
            }
        });

    }

    private void toProductDetailActivity(OrderDetailResult bean) {
        Intent intent = new Intent(this, ShopProductDetailActivity.class);
        intent.putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0, bean.getShop_id() + "");
        intent.putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_1, bean.getGoods_info().get(0).getGoods_id() + "");
        startActivity(intent);
    }

    private void toCommentActivity(OrderDetailResult bean) {
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0, bean.getOrder_no());
        intent.putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_1, bean.getShop_id() + "");
        intent.putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_2, bean.getGoods_info().size() + "");
        intent.putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_3, bean.getGoods_info().get(0));
        activityResultLauncher.launch(intent);
    }

}
