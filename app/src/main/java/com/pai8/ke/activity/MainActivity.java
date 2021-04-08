package com.pai8.ke.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lhs.library.base.BaseActivity;
import com.next.easynavigation.view.EasyNavigationBar;
import com.pai8.ke.R;
import com.pai8.ke.activity.account.LoginActivity;
import com.pai8.ke.activity.video.VideoPublishActivity;
import com.pai8.ke.app.MyApp;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.fragment.home.TabHomeFragment;
import com.pai8.ke.fragment.me.TabMeFragment;
import com.pai8.ke.fragment.msg.TabMsgFragment;
import com.pai8.ke.fragment.pai.TabCameraFragment;
import com.pai8.ke.global.EventCode;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.manager.UpdateAppManager;
import com.pai8.ke.shop.ui.TabShopMainFragment;
import com.pai8.ke.utils.AMapLocationUtils;
import com.pai8.ke.utils.EventBusUtils;
import com.pai8.ke.viewmodel.MainViewModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity<MainViewModel, com.pai8.ke.databinding.ActivityMainBinding> {

    //未选中icon
    private int[] normalIcon = {R.mipmap.icon_tabbar_home_normal, R.mipmap.icon_tabbar_shopping_normal,
            R.mipmap.icon_tabbar_msg_normal, R.mipmap.icon_tabbar_me_normal};
    //选中时icon
    private int[] selectIcon = {R.mipmap.icon_tabbar_home_select, R.mipmap.icon_tabbar_shopping_select,
            R.mipmap.icon_tabbar_msg_select, R.mipmap.icon_tabbar_me_select};
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EventBusUtils.register(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        fragments.add(new TabHomeFragment());
        //   fragments.add(new TabTakeawayFragment());
        fragments.add(new TabShopMainFragment());
        fragments.add(new TabCameraFragment());
        fragments.add(new TabMsgFragment());
        fragments.add(new TabMeFragment());

        mBinding.navigationBar
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
                    startActivity(new Intent(getBaseContext(), VideoPublishActivity.class));
                    return true;
                })
                .setOnTabClickListener(new EasyNavigationBar.OnTabClickListener() {
                    @Override
                    public boolean onTabSelectEvent(View view, int position) {
                        if (position == 2 && !AccountManager.getInstance().isLogin()) {
                            startActivity(new Intent(getBaseContext(), LoginActivity.class));
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
    public void addObserve() {
        mViewModel.getCheckUpgradeData().observe(this, data -> UpdateAppManager.showUpdateDialog(this, data));

        mViewModel.getMyInfoData().observe(this, data -> AccountManager.getInstance().saveShopInfo(data));
    }

    @Override
    public void initData() {
        getShopInfo();
        ThreadUtils.runOnUiThreadDelayed(() -> {
            MyApp.setJPushAlias();
            mViewModel.getCheckUpgradeData();
        }, 1000);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(BaseEvent event) {
        if (event != null) {
            switch (event.getCode()) {
                case EventCode.EVENT_HOME_TAB:
                    mBinding.navigationBar.selectTab((int) event.getData(), false);
                    break;
                case EventCode.EVENT_LOGIN_STATUS:
                    if (AccountManager.getInstance().isLogin()) {
                        getShopInfo();
                    }
                    break;
            }
        }
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
        if (!AccountManager.getInstance().isLogin()) return;
        mViewModel.getMyInfo();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AMapLocationUtils.destroy();
        EventBusUtils.unregister(this);
    }


}
