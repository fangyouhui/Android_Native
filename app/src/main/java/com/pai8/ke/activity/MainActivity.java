package com.pai8.ke.activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.next.easynavigation.view.EasyNavigationBar;
import com.pai8.ke.R;
import com.pai8.ke.activity.account.LoginActivity;
import com.pai8.ke.activity.video.VideoPublishActivity;
import com.pai8.ke.api.Api;
import com.pai8.ke.app.MyApp;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.entity.resp.MyInfoResp;
import com.pai8.ke.entity.resp.VersionResp;
import com.pai8.ke.fragment.home.TabHomeFragment;
import com.pai8.ke.fragment.me.TabMeFragment;
import com.pai8.ke.fragment.msg.TabMsgFragment;
import com.pai8.ke.fragment.pai.TabCameraFragment;
import com.pai8.ke.global.EventCode;
import com.pai8.ke.interfaces.contract.VersionContract;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.manager.UpdateAppManager;
import com.pai8.ke.presenter.VersionPresenter;
import com.pai8.ke.shop.ui.TabShopMainFragment;
import com.pai8.ke.utils.AMapLocationUtils;
import com.pai8.ke.utils.LogUtils;
import com.pai8.ke.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseMvpActivity<VersionContract.Presenter> implements VersionContract.View {

    //未选中icon
    private int[] normalIcon = {R.mipmap.icon_tabbar_home_normal, R.mipmap.icon_tabbar_shopping_normal,
            R.mipmap.icon_tabbar_msg_normal, R.mipmap.icon_tabbar_me_normal};
    //选中时icon
    private int[] selectIcon = {R.mipmap.icon_tabbar_home_select, R.mipmap.icon_tabbar_shopping_select,
            R.mipmap.icon_tabbar_msg_select, R.mipmap.icon_tabbar_me_select};
    private EasyNavigationBar navigationBar;
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult == null) return;
        String contents = intentResult.getContents();
        if (StringUtils.isEmpty(contents)) return;
        LogUtils.d("二维码：" + contents);
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(BaseEvent event) {
        super.receiveEvent(event);
        switch (event.getCode()) {
            case EventCode.EVENT_HOME_TAB:
                navigationBar.selectTab((int) event.getData(), false);
                break;
            case EventCode.EVENT_LOGIN_STATUS:
                if (mAccountManager.isLogin()) {
                    getShopInfo();
                }
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        navigationBar = findViewById(R.id.navigationBar);
        fragments.add(new TabHomeFragment());
        //   fragments.add(new TabTakeawayFragment());
        fragments.add(new TabShopMainFragment());
        fragments.add(new TabCameraFragment());
        fragments.add(new TabMsgFragment());
        fragments.add(new TabMeFragment());

        navigationBar
                .normalIconItems(normalIcon)
                .selectIconItems(selectIcon)
                .fragmentList(fragments)
                .fragmentManager(getSupportFragmentManager())
                .iconSize(40)
                .mode(EasyNavigationBar.NavigationMode.MODE_ADD)
                .centerAsFragment(true)
                .centerImageRes(R.mipmap.icon_tabbar_camera)
                .centerIconSize(60)
                .navigationHeight(55)
                .setOnCenterTabClickListener(view -> {
                    launchInterceptLogin(VideoPublishActivity.class);
                    return true;
                })
                .setOnTabClickListener(new EasyNavigationBar.OnTabClickListener() {
                    @Override
                    public boolean onTabSelectEvent(View view, int position) {
                        if (position == 2 && !mAccountManager.isLogin()) {
                            launch(LoginActivity.class);
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public boolean onTabReSelectEvent(View view, int position) {
                        //Tab重复点击事件
                        return false;
                    }
                })
                .build();

    }

    @Override
    public void initData() {
        getShopInfo();
        ThreadUtils.runOnUiThreadDelayed(() -> {
            MyApp.setJPushAlias();
            mPresenter.getVersion();
        }, 1000);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private long mExitTime;

    private void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            ToastUtils.showShort("再按一次退出程序");
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            AppUtils.exitApp();
        }
    }


    private void getShopInfo() {
        if (!mAccountManager.isLogin()) return;
        Api.getInstance().getMyInfo()
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<MyInfoResp>() {
                    @Override
                    protected void onSuccess(MyInfoResp myInfoResp) {
                        AccountManager.getInstance().saveShopInfo(myInfoResp);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AMapLocationUtils.destroy();
    }

    @Override
    public VersionContract.Presenter initPresenter() {
        return new VersionPresenter(this);
    }

    @Override
    public void showUpdateDialog(VersionResp data) {
        UpdateAppManager.showUpdateDialog(this, data);
    }
}
