package com.pai8.ke.wxapi;


import android.content.Intent;
import android.os.Bundle;

import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.global.EventCode;
import com.pai8.ke.global.GlobalConstants;
import com.pai8.ke.utils.EventBusUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import cn.sharesdk.wechat.utils.WechatHandlerActivity;

public class WXEntryActivity extends WechatHandlerActivity implements IWXAPIEventHandler {
    private final String TAG = "WXEntryActivity";

    private IWXAPI wxApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wxApi = WXAPIFactory.createWXAPI(this, GlobalConstants.APP_ID);
        wxApi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        wxApi.handleIntent(intent, this);
        finish();
    }

    @Override
    public void onReq(BaseReq baseReq) {
        switch (baseReq.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                break;
            default:
                break;
        }
    }

    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                switch (resp.getType()) {
                    case ConstantsAPI.COMMAND_SENDAUTH: //授权
                        if (resp instanceof SendAuth.Resp) {
                            SendAuth.Resp newResp = (SendAuth.Resp) resp;
                            EventBusUtils.sendEvent(new BaseEvent(EventCode.EVENT_WX_CODE, newResp.code));
                        }
                        break;
                    case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX: //微信分享
                        break;
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                break;
            default:
                break;
        }
        finish();
    }

}
