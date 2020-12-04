package com.pai8.ke.fragment.pay;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.pai8.ke.R;
import com.pai8.ke.api.Api;
import com.pai8.ke.base.BaseDialogFragment;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.entity.PayResult;
import com.pai8.ke.entity.event.PayResultEvent;
import com.pai8.ke.entity.resp.WxOrderPrepayResp;
import com.pai8.ke.global.EventCode;
import com.pai8.ke.global.GlobalConstants;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.utils.EventBusUtils;
import com.pai8.ke.utils.LogUtils;
import com.pai8.ke.utils.SpanUtils;
import com.pai8.ke.utils.StringUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class PayDialogFragment extends BaseDialogFragment {

    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.iv_cb_wx)
    ImageView ivCbWx;
    @BindView(R.id.iv_cb_zfb)
    ImageView ivCbZfb;
    @BindView(R.id.tv_btn_pay)
    TextView tvBtnPay;
    private IWXAPI mApi;

    private int mPayType = 1;
    private String mOrderNo;
    private String mGoodsPrice;

    public static PayDialogFragment newInstance(String goodsPrice, String orderNo) {
        PayDialogFragment fragment = new PayDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("goodsPrice", goodsPrice);
        bundle.putString("orderNo", orderNo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(BaseEvent event) {
        super.receiveEvent(event);
        switch (event.getCode()) {
            case EventCode.EVENT_PAY_RESULT:
                PayResultEvent data = (PayResultEvent) event.getData();
                if (data.getResult() == PayResultEvent.PAY_SUCESS) {
                    tvBtnPay.setEnabled(false);
                    toast("支付成功");
                    dismiss();
                } else if (data.getResult() == PayResultEvent.PAY_FAIL) {
                    toast("支付失败");
                } else {
                    toast("支付已取消");
                }
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_dialog_pay;
    }

    @Override
    public void initView(Bundle arguments) {
        mGoodsPrice = arguments.getString("goodsPrice", "");
        mOrderNo = arguments.getString("orderNo", "");
        setAmount(mGoodsPrice);
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.itn_close, R.id.rl_wx_pay, R.id.rl_zfb_pay, R.id.tv_btn_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.itn_close:
                dismiss();
                break;
            case R.id.rl_wx_pay:
                changePayType(1);
                break;
            case R.id.rl_zfb_pay:
                changePayType(2);
                break;
            case R.id.tv_btn_pay:
                if (mPayType == 1) {
                    getWxPayInfo();
                } else {

                }
                break;
        }
    }

    private void setAmount(String amount) {
        SpannableStringBuilder scoreSpan = SpanUtils.getBuilder("")
                .append(getActivity(), "￥")
                .setProportion(0.75f)
                .append(getActivity(), amount)
                .create(getActivity());
        tvAmount.setText(scoreSpan);
    }

    private void wxPay(WxOrderPrepayResp wx) {
        if (wx == null) return;
        tvBtnPay.setEnabled(true);
        new Thread(() -> {
            mApi = WXAPIFactory.createWXAPI(getActivity(), GlobalConstants.APP_ID);
            mApi.registerApp(GlobalConstants.APP_ID);
            PayReq req = new PayReq();
            req.appId = wx.getAppid();
            req.partnerId = wx.getPartnerid();
            req.prepayId = wx.getPrepayid();
            req.nonceStr = wx.getNoncestr();
            req.timeStamp = String.valueOf(wx.getTimestamp());
            req.sign = wx.getSign();
            req.packageValue = wx.getPackageX();//"Sign=WXPay"
            LogUtils.i("pay", "wx pay" + req.toString());
            mApi.sendReq(req);
            dismiss();
        }).start();
    }

    private void aliPay(final String strAliInfo) {
        tvBtnPay.setEnabled(true);
        new Thread(() -> {
            PayTask payTask = new PayTask(getActivity());
            Map<String, String> result = payTask.payV2(strAliInfo, true);
            PayResult payResult = new PayResult(result);
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000")) {
                EventBusUtils.sendEvent(new BaseEvent(EventCode.EVENT_PAY_RESULT, new PayResultEvent
                        (PayResultEvent.PAY_SUCESS)));
            } else {
                EventBusUtils.sendEvent(new BaseEvent(EventCode.EVENT_PAY_RESULT, new PayResultEvent
                        (PayResultEvent.PAY_FAIL)));
            }
        }).start();
    }

    private void changePayType(int i) {
        switch (i) {
            case 1:
                mPayType = 1;
                ivCbWx.setImageResource(R.mipmap.ic_cb_s);
                ivCbZfb.setImageResource(R.mipmap.ic_cb_n);
                break;
            case 2:
                mPayType = 2;
                ivCbWx.setImageResource(R.mipmap.ic_cb_n);
                ivCbZfb.setImageResource(R.mipmap.ic_cb_s);
                break;
        }

    }

    private void getWxPayInfo() {
        if (StringUtils.isEmpty(mOrderNo)) {
            toast("订单号为空");
            return;
        }
        Api.getInstance().orderPrepay(mOrderNo, AccountManager.getInstance().getUid(),mPayType)
                .doOnSubscribe(disposable -> {

                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<WxOrderPrepayResp>() {
                    @Override
                    protected void onSuccess(WxOrderPrepayResp orderInfo) {
                        wxPay(orderInfo);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }
}
