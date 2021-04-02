package com.pai8.ke.shop.ui;

import android.os.Bundle;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.lhs.library.base.BaseAppConstants;
import com.lhs.library.base.BaseBottomDialogFragment;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.databinding.DialogBottomPayBinding;
import com.pai8.ke.entity.OrderPrepayResult;
import com.pai8.ke.entity.PayResult;
import com.pai8.ke.entity.event.PayResultEvent;
import com.pai8.ke.global.EventCode;
import com.pai8.ke.global.GlobalConstants;
import com.pai8.ke.shop.viewmodel.PaySelectBottomViwModel;
import com.pai8.ke.utils.EventBusUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class PayBottomDialogFragment extends BaseBottomDialogFragment<PaySelectBottomViwModel, DialogBottomPayBinding> {
    private String totalPrice;
    private String orderNo;

    public static PayBottomDialogFragment newInstance(String totalPrice, String orderNo) {
        PayBottomDialogFragment fragment = new PayBottomDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BaseAppConstants.BundleConstant.ARG_PARAMS_0, totalPrice);
        bundle.putString(BaseAppConstants.BundleConstant.ARG_PARAMS_1, orderNo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusUtils.register(this);
        totalPrice = getArguments().getString(BaseAppConstants.BundleConstant.ARG_PARAMS_0);
        orderNo = getArguments().getString(BaseAppConstants.BundleConstant.ARG_PARAMS_1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(BaseEvent event) {
        if (event != null && event.getCode() == EventCode.EVENT_PAY_RESULT) {
            dismiss();
        }
    }

    @Override
    public void initView() {
        mBinding.tvPrice.setText("¥ " + totalPrice);
        mBinding.btnPay.setText("¥ " + totalPrice + " 立即支付");
        mBinding.ivClose.setOnClickListener(v -> {
            EventBusUtils.sendEvent(new BaseEvent(EventCode.EVENT_PAY_RESULT, new PayResultEvent(PayResultEvent.PAY_CANCEL)));
        });
        mBinding.clPayWeChat.setOnClickListener(v -> {
            mBinding.check.setChecked(true);
            mBinding.check2.setChecked(false);

        });
        mBinding.clPayAli.setOnClickListener(v -> {
            mBinding.check.setChecked(false);
            mBinding.check2.setChecked(true);
        });
        mBinding.btnPay.setOnClickListener(v -> {
            int payWay = 0;
            if (mBinding.check2.isChecked()) {
                payWay = 1;
            }
            if (payWay == 0) {
                mViewModel.orderPrepayWithWx(orderNo);
            } else {
                mViewModel.orderPrepayWithAli(orderNo);
            }
        });
    }

    @Override
    public void addObserve() {
        mViewModel.getOrderPrepayWxData().observe(getViewLifecycleOwner(), data -> {
            wxPay(data);
        });

        mViewModel.getOrderPrepayAliData().observe(getViewLifecycleOwner(), data -> {
            aliPay(data);
        });
    }


    private void wxPay(OrderPrepayResult wx) {
        if (wx == null) return;
        ThreadUtils.getFixedPool(5).execute(() -> {
            IWXAPI mApi = WXAPIFactory.createWXAPI(getActivity(), GlobalConstants.APP_ID);
            mApi.registerApp(GlobalConstants.APP_ID);
            PayReq req = new PayReq();
            req.appId = wx.getAppid();
            req.partnerId = wx.getPartnerid();
            req.prepayId = wx.getPrepayid();
            req.nonceStr = wx.getNoncestr();
            req.timeStamp = String.valueOf(wx.getTimestamp());
            req.sign = wx.getSign();
            req.packageValue = wx.getPackage2();//"Sign=WXPay"
            LogUtils.i("pay", "wx pay" + req.toString());
            mApi.sendReq(req);
            dismiss();
        });
    }

    private void aliPay(final String strAliInfo) {
        ThreadUtils.getFixedPool(5).execute(() -> {
            PayTask payTask = new PayTask(getActivity());
            Map<String, String> result = payTask.payV2(strAliInfo, true);
            PayResult payResult = new PayResult(result);
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000")) {
                EventBusUtils.sendEvent(new BaseEvent(EventCode.EVENT_PAY_RESULT, new PayResultEvent(PayResultEvent.PAY_SUCESS)));
            } else {
                EventBusUtils.sendEvent(new BaseEvent(EventCode.EVENT_PAY_RESULT, new PayResultEvent(PayResultEvent.PAY_FAIL)));
            }
        });

    }

}
