package com.pai8.ke.wxapi;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.entity.event.PayResultEvent;
import com.pai8.ke.global.EventCode;
import com.pai8.ke.global.GlobalConstants;
import com.pai8.ke.utils.EventBusUtils;
import com.pai8.ke.utils.ToastUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 微信sdk 回调页面
 */
public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, GlobalConstants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
        finish();
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (resp.errCode) {
                case 0://支付成功
                    EventBusUtils.sendEvent(new BaseEvent(EventCode.EVENT_PAY_RESULT, new PayResultEvent
                            (PayResultEvent.PAY_SUCESS)));
                    break;
                case -2://支付取消
                    EventBusUtils.sendEvent(new BaseEvent(EventCode.EVENT_PAY_RESULT, new PayResultEvent
                            (PayResultEvent.PAY_CANCEL)));
                    break;
                default://支付失败
                    EventBusUtils.sendEvent(new BaseEvent(EventCode.EVENT_PAY_RESULT, new PayResultEvent
                            (PayResultEvent.PAY_FAIL)));
                    break;
            }
            finish();
        }
    }
}