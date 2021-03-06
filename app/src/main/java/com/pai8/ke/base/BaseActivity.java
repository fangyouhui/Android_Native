package com.pai8.ke.base;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import com.gyf.immersionbar.ImmersionBar;
import com.hjq.bar.TitleBar;
import com.pai8.ke.R;
import com.pai8.ke.activity.account.LoginActivity;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.manager.ActivityManager;
import com.pai8.ke.utils.EventBusUtils;
import com.pai8.ke.utils.StringUtils;
import com.pai8.ke.utils.ToastUtils;
import com.pai8.ke.widget.LoadingDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;


/**
 * Activity基类
 * Created by gh on 2018/7/27.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public AccountManager mAccountManager;
    private LoadingDialog mLoadingDialog;
    public TitleBar mTitleBar;

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        if (isRegisterEventBus()) {
            EventBusUtils.register(this);
        }
        ActivityManager.getInstance().addActivity(this);
        mAccountManager = AccountManager.getInstance();
        initBasePresenter();
        initCreate(savedInstanceState);
        setBarDark(false);
        initView();
        initData();
        initListener();
    }

    protected void setBarDark(boolean isDark) {
        ImmersionBar immersionBar = ImmersionBar.with(this);
        if (isDark) {
            immersionBar.statusBarColor(R.color.color_black);
            immersionBar.statusBarDarkFont(false);
        } else {
            immersionBar.statusBarColor(R.color.color_white);
            immersionBar.statusBarDarkFont(true);
        }
        if (mTitleBar != null) {
            immersionBar.titleBarMarginTop(mTitleBar);
        }
        immersionBar.init();
    }

    public void setBarTrans(boolean isBarDarkFont) {
        ImmersionBar.with(this)
                .transparentStatusBar()
                .statusBarDarkFont(isBarDarkFont)
                .init();
    }

    public void setImmersionBar(int id) {
        ImmersionBar.with(this)
                .transparentBar()
                .statusBarDarkFont(true, 0.5f)
                .init();   //解决白色背景时状态栏字体不显示的问题
        ImmersionBar.setTitleBar(this, findViewById(id));
        if (ImmersionBar.hasNavigationBar(this)) {
            ImmersionBar.with(this).fullScreen(false).navigationBarColor(R.color.color_white).init();
        }
    }

    public void initCreate(Bundle savedInstanceState) {
        mTitleBar = findViewById(R.id.title_bar);
    }

    public void initBasePresenter() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().removeActivity(this);
        if (isRegisterEventBus()) {
            EventBusUtils.unregister(this);
        }
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    @LayoutRes
    public abstract int getLayoutId();

    public abstract void initView();

    public void initData() {

    }

    public void initListener() {

    }

    /**
     * 是否注册事件分发
     *
     * @return true绑定EventBus事件分发，默认不绑定，子类需要绑定的话复写此方法返回true.
     */
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(BaseEvent event) {
        if (event != null) {
            receiveEvent(event);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onStickyEventBusCome(BaseEvent event) {
        if (event != null) {
            receiveStickyEvent(event);
        }
    }

    /**
     * 接收到分发到事件
     *
     * @param event 事件
     */
    protected void receiveEvent(BaseEvent event) {

    }

    /**
     * 接受到分发的粘性事件
     *
     * @param event 粘性事件
     */
    protected void receiveStickyEvent(BaseEvent event) {

    }

    public void showLoadingDialog(String msg) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }
        if (StringUtils.isNotEmpty(msg)) {
            mLoadingDialog.setMessage(msg);
        }
        mLoadingDialog.show();
    }

    public void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    public void toast(String strMsg) {
        ToastUtils.showShort(this, strMsg);
    }

    public void launch(Class clazz) {
        Intent intent = new Intent(this, clazz);
        intent.putExtras(new Bundle());
        startActivity(intent);
    }

    public void launch(Class clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void launchInterceptLogin(Class clazz) {
        if (mAccountManager.isLogin()) {
            Intent intent = new Intent(this, clazz);
            startActivity(intent);
        } else {
            launch(LoginActivity.class);
        }
    }

}

