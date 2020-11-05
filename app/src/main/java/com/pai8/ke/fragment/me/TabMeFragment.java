package com.pai8.ke.fragment.me;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gyf.immersionbar.ImmersionBar;
import com.pai8.ke.R;
import com.pai8.ke.base.BaseFragment;


public class TabMeFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab_me;
    }

    @Override
    protected void initView(Bundle arguments) {
        //透明状态栏，字体深色
        ImmersionBar.with(this)
                .transparentStatusBar()
                .statusBarDarkFont(true)
                .init();
    }

    @Override
    protected void onLazyLoad() {
        super.onLazyLoad();
    }
}
