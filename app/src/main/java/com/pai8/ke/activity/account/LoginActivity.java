package com.pai8.ke.activity.account;

import android.content.Intent;
import android.os.CountDownTimer;
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

import com.gyf.immersionbar.ImmersionBar;
import com.pai8.ke.R;
import com.pai8.ke.activity.MainActivity;
import com.pai8.ke.api.Api;
import com.pai8.ke.app.MyApp;
import com.pai8.ke.base.BaseActivity;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.entity.req.CodeReq;
import com.pai8.ke.entity.req.LoginReq;
import com.pai8.ke.entity.resp.UserInfo;
import com.pai8.ke.global.EventCode;
import com.pai8.ke.global.GlobalConstants;
import com.pai8.ke.utils.AppUtils;
import com.pai8.ke.utils.StringUtils;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import androidx.annotation.Nullable;
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

    @OnClick({R.id.bt_get_code, R.id.btn_login, R.id.btn_changLogin, R.id.btn_register, R.id.img_weixin})
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
            launch(MainActivity.class);
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
