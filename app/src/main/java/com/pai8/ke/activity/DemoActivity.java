package com.pai8.ke.activity;

import com.pai8.ke.R;
import com.pai8.ke.api.Api;
import com.pai8.ke.base.BaseActivity;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.entity.req.CodeReq;
import com.pai8.ke.entity.req.LoginReq;
import com.pai8.ke.entity.resp.UserInfo;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 示例代码
 * 以登录为例介绍了网络请求框架的使用
 */
public class DemoActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_demo;
    }

    @Override
    public void initView() {

    }

    /**
     * 登录
     */
    private void login() {
        showLoadingDialog(null);
        LoginReq loginReq = new LoginReq();
        loginReq.setMobile("1500000000");
        loginReq.setVerifyCode("1234");

        Api.getInstance().login(loginReq)
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<UserInfo>() {

                    @Override
                    protected void onSuccess(UserInfo data) {
                        dismissLoadingDialog();
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }

    /**
     * 获取验证码
     */
    private void getVerifyCode() {
        showLoadingDialog(null);
        CodeReq codeReq = new CodeReq();
        Api.getInstance().verifyCode(codeReq)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) {
                    }
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<UserInfo>() {

                    @Override
                    protected void onSuccess(UserInfo data) {
                        dismissLoadingDialog();
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }
}
