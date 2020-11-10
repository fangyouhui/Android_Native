package com.pai8.ke.activity;

import android.view.KeyEvent;

import com.next.easynavigation.view.EasyNavigationBar;
import com.pai8.ke.R;
import com.pai8.ke.base.BaseActivity;
import com.pai8.ke.fragment.home.TabHomeFragment;
import com.pai8.ke.fragment.me.TabMeFragment;
import com.pai8.ke.fragment.pai.TabCameraFragment;
import com.pai8.ke.fragment.shop.TabShopFragment;
import com.pai8.ke.fragment.type.TabTypeFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;


public class MainActivity extends BaseActivity {

    //未选中icon
    private int[] normalIcon = {R.mipmap.icon_tabbar_home_normal, R.mipmap.icon_tabbar_type_normal,
            R.mipmap.icon_tabbar_shopping_normal, R.mipmap.icon_tabbar_me_normal};
    //选中时icon
    private int[] selectIcon = {R.mipmap.icon_tabbar_home_select, R.mipmap.icon_tabbar_type_select,
            R.mipmap.icon_tabbar_shopping_select, R.mipmap.icon_tabbar_me_select};
    private String[] menuTextItems = {"", "", "", ""};
    private EasyNavigationBar navigationBar;
    private List<Fragment> fragments = new ArrayList<>();

    private long mExitTime;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        navigationBar = findViewById(R.id.navigationBar);
        fragments.add(new TabHomeFragment());
        fragments.add(new TabTypeFragment());
        fragments.add(new TabCameraFragment());
        fragments.add(new TabShopFragment());
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
                .build();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 1800) {
                toast("再按一次退出app");
                mExitTime = System.currentTimeMillis();
            } else {
                this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
