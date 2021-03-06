package com.pai8.ke.activity.account;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.gyf.immersionbar.ImmersionBar;
import com.pai8.ke.R;
import com.pai8.ke.activity.common.WebViewActivity;
import com.pai8.ke.api.Api;
import com.pai8.ke.base.BaseActivity;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.entity.UserInfo;
import com.pai8.ke.entity.event.LoginStatusEvent;
import com.pai8.ke.entity.req.CodeReq;
import com.pai8.ke.entity.req.LoginReq;
import com.pai8.ke.global.EventCode;
import com.pai8.ke.global.GlobalConstants;
import com.pai8.ke.utils.AppUtils;
import com.pai8.ke.utils.EventBusUtils;
import com.pai8.ke.utils.PreferencesUtils;
import com.pai8.ke.utils.StringUtils;
import com.pai8.ke.widget.BottomDialog;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.BindView;
import butterknife.OnClick;

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
    private IWXAPI mWxapi;

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    //test git
    @Override
    protected void receiveEvent(BaseEvent event) {
        super.receiveEvent(event);
        switch (event.getCode()) {
            case EventCode.EVENT_WX_CODE:
                String code = (String) event.getData();
                showLoadingDialog("登录中...");
                Api.getInstance().getUid(code)
                        .doOnSubscribe(disposable -> {
                        })
                        .compose(RxSchedulers.io_main())
                        .subscribe(loginObserver);
                break;
        }
    }

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

    @OnClick({R.id.bt_get_code, R.id.btn_login, R.id.btn_changLogin, R.id.btn_register, R.id.img_weixin, R.id.tv_showpro, R.id.tv_secrets})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_get_code:
                getVerifyCode();
                break;
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_changLogin:
                //改变登录方式
                if (btnChangLogin.getText().equals("密码登录")) {
                    btnChangLogin.setText("验证码登录");
                    btGetCode.setVisibility(View.GONE);
                    etMessage.setText("");
                    etMessage.setHint(new SpannableString("请输入密码"));
                    etMessage.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    btGetCode.setVisibility(View.VISIBLE);
                    btnChangLogin.setText("密码登录");
                    etMessage.setText("");
                    etMessage.setHint(new SpannableString("请输入短信验证码"));
                    etMessage.setInputType(InputType.TYPE_CLASS_NUMBER);
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
            case R.id.tv_showpro:
                //用户服务协议
                WebViewActivity.launch(this, GlobalConstants.HTTP_SERVER_PROTOCOL, "用户服务协议");
                break;
            case R.id.tv_secrets:
                //隐私保护指引
                WebViewActivity.launch(this, GlobalConstants.HTTP_PRIVACY_PROTOCOL, "隐私保护政策");
                break;
            default:
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getVerifyCode() {
        String userPhone = StringUtils.getEditText(etUserName);
        if (StringUtils.isEmpty(userPhone)) {
            toast("请输入手机号");
            return;
        }
        if (!AppUtils.isPhone(userPhone)) {
            toast("请输入正确的手机号");
            return;
        }
        showLoadingDialog(null);
        Api.getInstance().verifyCode(new CodeReq(userPhone))
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<Object>() {
                    @Override
                    protected void onSuccess(Object o) {
                        dismissLoadingDialog();
                        toast("验证码发送成功");
                        TimeCountDown timeCountDown = new TimeCountDown(120 * 1000, 1000);
                        timeCountDown.start();
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        dismissLoadingDialog();
                        super.onError(msg, errorCode);
                    }
                });
    }

    /**
     * 登录
     */
    private void login() {
        String userPhone = StringUtils.getEditText(etUserName);
        String userMessage = StringUtils.getEditText(etMessage);

        if (StringUtils.isEmpty(userPhone)) {
            toast("请输入手机号");
            return;
        }
        if (!AppUtils.isPhone(userPhone)) {
            toast("请输入正确的手机号");
            return;
        }

        if ((Boolean) PreferencesUtils.get(this, "policy_check", false) == false) {
            // 打开用户协议
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_webview, null);
            BottomDialog dialog = new BottomDialog(this, view);
            WebView webView = view.findViewById(R.id.web_view);
            TextView btnConfirm = view.findViewById(R.id.btn_confirm);
            TextView tvTitle = view.findViewById(R.id.tv_title);
            tvTitle.setText("用户协议");
            webView.loadUrl(GlobalConstants.HTTP_SERVER_PROTOCOL);
            btnConfirm.setOnClickListener(v -> {
                PreferencesUtils.put(this, "policy_check", true);
                dialog.dismiss();
                login();
            });
            dialog.setCancelable(false);
            dialog.show();
            return;
        }
        if ((Boolean) PreferencesUtils.get(this, "privacy_check", false) == false) {
            // 打开隐私政策
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_webview, null);
            BottomDialog dialog = new BottomDialog(this, view);
            WebView webView = view.findViewById(R.id.web_view);
            TextView btnConfirm = view.findViewById(R.id.btn_confirm);
            TextView tvTitle = view.findViewById(R.id.tv_title);
            tvTitle.setText("隐私政策");
            webView.loadUrl(GlobalConstants.HTTP_PRIVACY_PROTOCOL);
            btnConfirm.setOnClickListener(v -> {
                PreferencesUtils.put(this, "privacy_check", true);
                dialog.dismiss();
                login();
            });
            dialog.setCancelable(false);
            dialog.show();
            return;
        }

        LoginReq loginReq = new LoginReq();
        loginReq.setMobile(userPhone);

        if (btnChangLogin.getText().equals("密码登录")) {
            loginReq.setCode(userMessage);
            Api.getInstance().login(loginReq)
                    .doOnSubscribe(disposable -> {
                    })
                    .compose(RxSchedulers.io_main())
                    .subscribe(loginObserver);
        } else {
            loginReq.setPwd(userMessage);
            Api.getInstance().mobileLogin(loginReq)
                    .doOnSubscribe(disposable -> {
                    })
                    .compose(RxSchedulers.io_main())
                    .subscribe(loginObserver);
        }

    }

    BaseObserver loginObserver = new BaseObserver<UserInfo>() {
        @Override
        protected void onSuccess(UserInfo userInfo) {
            dismissLoadingDialog();
            toast("登录成功");
            mAccountManager.saveUserInfo(userInfo);
            EventBusUtils.sendEvent(new BaseEvent(EventCode.EVENT_LOGIN_STATUS,
                    new LoginStatusEvent(LoginStatusEvent.LOGIN)));
            finish();
        }

        @Override
        protected void onError(String msg, int errorCode) {
            dismissLoadingDialog();
            super.onError(msg, errorCode);
        }
    };

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
        mWxapi = WXAPIFactory.createWXAPI(LoginActivity.this, GlobalConstants.APP_ID, true);
        mWxapi.registerApp(GlobalConstants.APP_ID);
        if (mWxapi != null && mWxapi.isWXAppInstalled()) {
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wechat_sdk_demo";
            mWxapi.sendReq(req);
        } else {
            toast("请先安装微信");
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
