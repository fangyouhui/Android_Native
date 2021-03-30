package com.pai8.ke.shop.ui;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.activity.result.contract.ActivityResultContracts;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lhs.library.base.BaseActivity;
import com.lhs.library.base.BaseAppConstants;
import com.lhs.library.base.BaseBottomDialogFragment.OnDialogListener;
import com.pai8.ke.activity.account.LoginActivity;
import com.pai8.ke.activity.me.CouponListActivity;
import com.pai8.ke.activity.takeaway.order.OrderDetailActivity;
import com.pai8.ke.databinding.ActivityConfirmOrderBinding;
import com.pai8.ke.entity.AddOrderParam;
import com.pai8.ke.entity.GroupGoodsInfoResult;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.shop.viewmodel.ConfirmOrderViewModel;
import com.pai8.ke.utils.ImageLoadUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 确认订单
 */
public class ConfirmOrderActivity extends BaseActivity<ConfirmOrderViewModel, ActivityConfirmOrderBinding> {
    private GroupGoodsInfoResult bean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bean = (GroupGoodsInfoResult) getIntent().getSerializableExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0);
        bindView();
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.llCoupon.setOnClickListener(v -> {
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {

                }
            }).launch(new Intent(this, CouponListActivity.class));
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
                int uid = Integer.parseInt(AccountManager.getInstance().getUid());
                PaySelectBottomDialog paySelectBottomDialog = new PaySelectBottomDialog();
                Bundle bundle = new Bundle();
                bundle.putString(BaseAppConstants.BundleConstant.ARG_PARAMS_0, mBinding.tvTotalPrice.getTag().toString());
                paySelectBottomDialog.setArguments(bundle);
                paySelectBottomDialog.setListener(new OnDialogListener() {

                    @Override
                    public void onConfirmClickListener(@NotNull Object data2) {
                        int way = (int) data2;
                        if (way == 0) { //微信支付
                            mViewModel.orderPrepayWithWx(data, uid);
                        } else { //调支付宝支付
                            mViewModel.orderPrepayWithAli(data, uid);
                        }
                    }

                    @Override
                    public void onCloseClickListener() {
                        toOrderDetailActivity(data);
                    }
                });

                paySelectBottomDialog.showNow(getSupportFragmentManager(), "payWay");
            }
        });

        mViewModel.getOrderPrepayData().observe(this, data -> { //微信支付信息
            // TODO: 2021/3/30  调微信支付
        });

        mViewModel.getOrderPrepayData2().observe(this, data -> { //支付宝支付信息
            // TODO: 2021/3/30 调支付宝支付
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
