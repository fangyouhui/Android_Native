package com.pai8.ke.activity.me;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gyf.immersionbar.ImmersionBar;
import com.pai8.ke.R;
import com.pai8.ke.activity.MainActivity;
import com.pai8.ke.base.BaseActivity;
import com.pai8.ke.entity.resp.ResLoginInfo;
import com.pai8.ke.entity.resp.UserInfo;
import com.pai8.ke.global.GlobalConstants;
import com.pai8.ke.utils.NetWorkUtils;
import com.pai8.ke.utils.PreferencesUtils;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallback;
import com.permissionx.guolindev.callback.ForwardToSettingsCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.permissionx.guolindev.request.ForwardScope;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class LoginActivity extends BaseActivity implements TextWatcher {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_REGISTER = 1;
    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.et_Message)
    EditText etMessage;
    @BindView(R.id.bt_get_code)
    Button btGetCode;
    @BindView(R.id.tv_showpro)
    TextView tvShowpro;
    @BindView(R.id.tv_secrets)
    TextView tvSecrets;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_changLogin)
    Button btnChangLogin;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.img_weixin)
    ImageView imgWeixin;
    private String userPhone;
    private String userMessage;
    private NetWorkUtils netUtils;


    public int getLayoutId() {
        return R.layout.activity_login;
    }


    public void initView() {
        //透明状态栏，字体深色
        ImmersionBar.with(this)
                .transparentStatusBar()
                .statusBarDarkFont(true)
                .init();
    }


    @Override
    public void initListener() {
        super.initListener();
        etUserName.addTextChangedListener(this);
        etMessage.addTextChangedListener(this);
    }

    //获取输入
    public void getData() {
        userPhone = etUserName.getText().toString().trim();
        userMessage = etMessage.getText().toString().trim();
    }


    @OnClick({R.id.bt_get_code, R.id.btn_login, R.id.btn_changLogin, R.id.btn_register, R.id.img_weixin})

    public void onViewClicked(View view) {
        getData();
        netUtils = NetWorkUtils.getInstance();
        switch (view.getId()) {
            case R.id.bt_get_code:
                if (RegisterActivity.isPhone(userPhone)) {
                    TimeCountDown timeCountDown = new TimeCountDown(120 * 1000, 1000);
                    timeCountDown.start();
                    Map<String, String> reqBody = new ConcurrentSkipListMap<>();
                    reqBody.put("mobile", userPhone);
                    netUtils.postDataAsynToNet(GlobalConstants.HTTP_URL_RELEASE + "public/getCode", reqBody, new
                            NetWorkUtils.MyNetCall() {
                                @Override
                                public void success(Call call, Response response) throws IOException {
                                    //获取验证码
                                }

                                @Override
                                public void failed(Call call, IOException e) {
                                }
                            });
                } else {
                    toast("请输入正确的手机号");
                }
                break;
            case R.id.btn_login:
                Map<String, String> reqBody = new ConcurrentSkipListMap<>();
                reqBody.put("mobile", userPhone);
                if (btnChangLogin.getText().equals("密码登录")) {
                    reqBody.put("code", userMessage);

                    netUtils.postDataAsynToNet(GlobalConstants.HTTP_URL_RELEASE + "public/login", reqBody, new
                            NetWorkUtils.MyNetCall() {
                                @Override
                                public void success(Call call, Response response) throws IOException {
                                    getTokenFromGson(response.body().string());

                                }

                                @Override
                                public void failed(Call call, IOException e) {
                                    toast(e.getMessage());
                                }
                            });
                } else {
                    reqBody.put("pwd", userMessage);
                    netUtils.postDataAsynToNet(GlobalConstants.HTTP_URL_RELEASE + "public/mobileLogin", reqBody, new
                            NetWorkUtils.MyNetCall() {
                                @Override
                                public void success(Call call, Response response) throws IOException {
                                    getTokenFromGson(response.body().string());
                                }

                                @Override
                                public void failed(Call call, IOException e) {
                                    toast(e.getMessage());
                                }
                            });
                }

                break;
            case R.id.btn_changLogin:
                //改变登录方式
                if (btnChangLogin.getText().equals("密码登录")) {
                    btnChangLogin.setText("验证码登录");
                    btGetCode.setVisibility(View.GONE);
                    etMessage.setText("");
                    etMessage.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    etMessage.setHint(new SpannableString("请输入密码"));
                } else {
                    btGetCode.setVisibility(View.VISIBLE);
                    btnChangLogin.setText("密码登录");
                    etMessage.setText("");
                    etMessage.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    etMessage.setHint(new SpannableString("请输入短信验证码"));
                }
                break;
            case R.id.btn_register:
                //跳转到注册页面
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, REQUEST_REGISTER);
                break;
            case R.id.img_weixin:
                //微信登录
                wxLogin();
                break;
        }
    }

    public void getEditextStatus() {
        if (TextUtils.isEmpty(etUserName.getText()) || etUserName.getText().length() < 11 || TextUtils.isEmpty(etMessage.getText())) {
            btnLogin.setAlpha(0.4f);
            btnLogin.setEnabled(Boolean.FALSE);

        } else {
            btnLogin.setAlpha(1f);
            btnLogin.setEnabled(Boolean.TRUE);
        }
    }

    public class TimeCountDown extends CountDownTimer {
        public TimeCountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btGetCode.setClickable(false);
            btGetCode.setText(millisUntilFinished / 1000 + "s后重新发送");
        }

        @Override
        public void onFinish() {
            btGetCode.setText("获取验证码");
            btGetCode.setClickable(true);
        }
    }

    public void wxLogin() {
        PermissionX.init(this)
                .permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE)
                .onExplainRequestReason(new ExplainReasonCallback() {
                    @Override
                    public void onExplainReason(ExplainScope scope, List<String> deniedList) {
                        scope.showRequestReasonDialog(deniedList, "该权限是必须依赖的权限", "去开启", "关闭");
                    }
                })
                .onForwardToSettings(new ForwardToSettingsCallback() {
                    @Override
                    public void onForwardToSettings(ForwardScope scope, List<String> deniedList) {
                        scope.showForwardToSettingsDialog(deniedList, "需要您去应用程序设置当中手动开启权限", "去开启", "关闭");
                    }
                })
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                        if (allGranted) {
                            GlobalConstants.wx_api = WXAPIFactory.createWXAPI(LoginActivity.this,
                                    GlobalConstants.APP_ID, true);
                            GlobalConstants.wx_api.registerApp(GlobalConstants.APP_ID);
                            if (GlobalConstants.wx_api != null && GlobalConstants.wx_api.isWXAppInstalled()) {
                                SendAuth.Req req = new SendAuth.Req();
                                req.scope = "snsapi_userinfo";
                                req.state = "wechat_sdk_demo";
                                GlobalConstants.wx_api.sendReq(req);
                                finish();
                            } else {
                                toast("请先安装微信");
                            }
                        }
                    }
                });
    }

    public void getTokenFromGson(String jsonData) {
        Gson gson = new Gson();
        ResLoginInfo<UserInfo> loginInfo = gson.fromJson(jsonData,
                new TypeToken<ResLoginInfo<UserInfo>>() {
                }.getType());
        UserInfo userInfo = loginInfo.getResult();
        if (userInfo != null) {
            PreferencesUtils.put(LoginActivity.this, "token", userInfo.getToken());
            Looper.prepare();
            toast("登录成功");
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            Looper.loop();
        } else {
            Looper.prepare();
            toast("登录失败，请重新登录");
            Looper.loop();
        }

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        getEditextStatus();
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            etUserName.setText(data.getStringExtra("userPhone"));
        }
    }
}
