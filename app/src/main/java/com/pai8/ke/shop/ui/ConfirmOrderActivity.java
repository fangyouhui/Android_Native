package com.pai8.ke.shop.ui;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lhs.library.base.BaseActivity;
import com.lhs.library.base.BaseAppConstants;
import com.pai8.ke.activity.account.LoginActivity;
import com.pai8.ke.activity.me.CouponListActivity;
import com.pai8.ke.activity.takeaway.order.OrderDetailActivity;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.databinding.ActivityConfirmOrderBinding;
import com.pai8.ke.entity.AddOrderParam;
import com.pai8.ke.entity.GroupGoodsInfoResult;
import com.pai8.ke.entity.event.PayResultEvent;
import com.pai8.ke.global.EventCode;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.shop.viewmodel.ConfirmOrderViewModel;
import com.pai8.ke.utils.EventBusUtils;
import com.pai8.ke.utils.ImageLoadUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 确认订单
 */
public class ConfirmOrderActivity extends BaseActivity<ConfirmOrderViewModel, ActivityConfirmOrderBinding> {
    private GroupGoodsInfoResult bean;
    private ActivityResultLauncher activityResultLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusUtils.register(this);
        bean = (GroupGoodsInfoResult) getIntent().getSerializableExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0);
        bindView();

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(BaseEvent event) {
        switch (event.getCode()) {
            case EventCode.EVENT_PAY_RESULT:
                PayResultEvent data = (PayResultEvent) event.getData();
                if (data.getResult() == PayResultEvent.PAY_SUCESS) {
                    com.pai8.ke.utils.ToastUtils.showShort("支付成功");
                } else if (data.getResult() == PayResultEvent.PAY_FAIL) {
                    com.pai8.ke.utils.ToastUtils.showShort("支付失败");
                } else {
                    com.pai8.ke.utils.ToastUtils.showShort("支付已取消");
                }
                toOrderDetailActivity(mViewModel.getAddOrderData().getValue());
                break;
        }
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.llCoupon.setOnClickListener(v -> {
            activityResultLauncher.launch(new Intent(this, CouponListActivity.class)
                    .putExtra("intentType", CouponListActivity.INTENT_TYPE_CAN_USE));
        });
        mBinding.llBottom.setOnClickListener(v -> pay());
        mBinding.btnReduce.setOnClickListener(v -> {
            int count = Integer.parseInt(mBinding.tvCount.getText().toString());
            if (count > 1) {
                count--;
            }
            mBinding.tvCount.setText(String.valueOf(count));
            bindPrice();
        });

        mBinding.btnAdd.setOnClickListener(v -> {
            int count = Integer.parseInt(mBinding.tvCount.getText().toString());
            count++;
            mBinding.tvCount.setText(String.valueOf(count));
            bindPrice();
        });
    }

    @Override
    public void addObserve() {
        mViewModel.getAddOrderData().observe(this, data -> {
            if (!TextUtils.isEmpty(data)) { //下单成功 HX126741617094442
                PayBottomDialogFragment paySelectBottomDialog = PayBottomDialogFragment.newInstance(mBinding.tvTotalPrice.getTag().toString(), data);
                paySelectBottomDialog.showNow(getSupportFragmentManager(), "payWay");
            }
        });
    }

    private void toOrderDetailActivity(String orderNo) {
        Intent intent = new Intent(getBaseContext(), OrderDetailActivity.class);
        intent.putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0, orderNo);
        startActivity(intent);
        finish();
    }

    private void bindView() {
        ImageLoadUtils.loadImage(bean.getShop().getShop_img(), mBinding.ivShopLogo);
        mBinding.tvShopName.setText(bean.getShop().getShop_name());
        ImageLoadUtils.loadImage(bean.getCover().get(0), mBinding.ivProductImg);
        mBinding.tvProductName.setText(bean.getTitle());
        mBinding.tvDesc.setText(bean.getDesc());
        mBinding.tvGroupBuyPrice.setText("¥" + bean.getSell_price());
        mBinding.tvOriginPrice.setText("¥" + bean.getOrigin_price());
        mBinding.tvOriginPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        bindPrice();
    }

    private void bindPrice() {
        int count = Integer.parseInt(mBinding.tvCount.getText().toString());
        double productPrice = count * Double.parseDouble(bean.getSell_price());
        mBinding.tvProductPrice.setText("¥" + productPrice);

        double fullDiscountPrice = Double.valueOf(mBinding.tvFullDiscountPrice.getTag().toString());

        double finalPrice = productPrice - fullDiscountPrice;
        mBinding.tvTotalPrice.setText("¥" + finalPrice);
        mBinding.tvTotalPrice.setTag(finalPrice);
    }

    private void pay() {
        if (TextUtils.isEmpty(mBinding.etNickname.getText())) {
            ToastUtils.showShort("请输入联系人称呼");
            return;
        }

        if (TextUtils.isEmpty(mBinding.etPhone.getText())) {
            ToastUtils.showShort("请填写收货人的手机号");
            return;
        }

        if (!RegexUtils.isMobileSimple(mBinding.etPhone.getText())) {
            ToastUtils.showShort("请检查手机号是否正确");
            return;
        }

        String uid = AccountManager.getInstance().getUid();
        if (TextUtils.isEmpty(uid)) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }

        int count = Integer.parseInt(mBinding.tvCount.getText().toString());

        AddOrderParam.GoodsInfo goodsInfo = new AddOrderParam.GoodsInfo();
        goodsInfo.setGoods_id(bean.getId());
        goodsInfo.setGoods_num(count);
        goodsInfo.setGoods_price(Double.valueOf(mBinding.tvTotalPrice.getTag().toString()));

        List<AddOrderParam.GoodsInfo> goods_info = new ArrayList<>();
        goods_info.add(goodsInfo);

        AddOrderParam addOrderParam = new AddOrderParam();
        addOrderParam.setGoods_info(GsonUtils.toJson(goods_info));
        addOrderParam.setShop_id(bean.getShop_id());
        addOrderParam.setBuyer_id(Integer.parseInt(uid));
        addOrderParam.setOrder_type(3);
        addOrderParam.setBuyer_name(mBinding.etNickname.getText().toString());
        addOrderParam.setBuyer_phone(mBinding.etPhone.getText().toString());

        mViewModel.addOrder(addOrderParam);

    }


}
