package com.pai8.ke.activity.wxapi;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pai8.ke.activity.MainActivity;
import com.pai8.ke.entity.resp.WeixinBean;
import com.pai8.ke.global.GlobalConstants;
import com.pai8.ke.utils.NetWorkUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import okhttp3.Call;
import okhttp3.Response;

public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
    private static final String TAG = "WXEntryActivity";
    // 获取第一步的code后，请求以下链接获取access_token
    private String GetCodeRequest = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code" +
            "=CODE&grant_type=authorization_code";
    // 获取用户个人信息
    private String GetUserInfo = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalConstants.wx_api.handleIntent(getIntent(), this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
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
            //正确返回
            case BaseResp.ErrCode.ERR_OK:
                switch (resp.getType()) {
                    case ConstantsAPI.COMMAND_SENDAUTH:    //表示授权
                        if (resp instanceof SendAuth.Resp) {
                            SendAuth.Resp newResp = (SendAuth.Resp) resp;
                            //获取微信传回的code
                            final String code = newResp.code;
                            String get_access_token_url = getCodeRequest(code);
                            Map<String, String> reqBody = new ConcurrentSkipListMap<>();
                            NetWorkUtils netUtils = NetWorkUtils.getInstance();
                            netUtils.postDataAsynToNet(get_access_token_url, reqBody, new NetWorkUtils.MyNetCall() {
                                @Override
                                public void success(Call call, Response response) throws IOException {
                                    String responseData = response.body().string();
                                    parseJSONWithGSON(responseData);
                                }

                                @Override
                                public void failed(Call call, IOException e) {
                                }
                            });
                        }
                        break;
                    case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX://表示微信分享
                        finish();
                        break;
                    default:
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

    private void getUserInfo(String user_info_url) {
        Map<String, String> reqBody = new ConcurrentSkipListMap<>();
        NetWorkUtils netUtils = NetWorkUtils.getInstance();
        netUtils.postDataAsynToNet(user_info_url, reqBody,
                new NetWorkUtils.MyNetCall() {
                    @Override
                    public void success(Call call, Response response) throws IOException {
                        String str = response.body().string();
                        Log.d(TAG, "success: " + str);
                        Intent intent = new Intent(WXEntryActivity.this, MainActivity.class);
                        intent.putExtra("userInfo", str);
                        intent.putExtra("tag", "weixin");
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void failed(Call call, IOException e) {
                    }

                });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        GlobalConstants.wx_api.handleIntent(intent, this);
        finish();
    }

    private String getUserInfo(String access_token, String openid) {
        String result = null;
        GetUserInfo = GetUserInfo.replace("ACCESS_TOKEN", urlEnodeUTF8(access_token));
        GetUserInfo = GetUserInfo.replace("OPENID", urlEnodeUTF8(openid));
        result = GetUserInfo;
        return result;
    }

    // 解析返回的数据
    private void parseJSONWithGSON(String jsonData) {
        Gson gson = new Gson();
        WeixinBean appList = gson.fromJson(jsonData, new TypeToken<WeixinBean>() {
        }.getType());
        String getUserInfoUrl = getUserInfo(appList.getAccess_token(), appList.getOpenid());
        getUserInfo(getUserInfoUrl);
    }

    private String getCodeRequest(String code) {
        String result = null;
        GetCodeRequest = GetCodeRequest.replace("APPID", urlEnodeUTF8(GlobalConstants.APP_ID));
        GetCodeRequest = GetCodeRequest.replace("SECRET", urlEnodeUTF8(GlobalConstants.APP_SECRET));
        GetCodeRequest = GetCodeRequest.replace("CODE", urlEnodeUTF8(code));
        result = GetCodeRequest;
        return result;
    }

    private String urlEnodeUTF8(String str) {
        String result = str;
        try {
            result = URLEncoder.encode(str, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private void parseJSONUser(String jsonData) {
        Gson gson = new Gson();
        WeixinBean appList = gson.fromJson(jsonData, new TypeToken<WeixinBean>() {
        }.getType());
        Intent intent = new Intent(WXEntryActivity.this, MainActivity.class);
        String str = appList.getSex() == 1 ? "   性别:男" : "   性别:女";
        intent.putExtra("username", "微信昵称:" + appList.getNickname());
        intent.putExtra("sex", str);
        startActivity(intent);
    }
}
