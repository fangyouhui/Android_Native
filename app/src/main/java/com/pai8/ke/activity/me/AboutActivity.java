package com.pai8.ke.activity.me;

import android.os.Bundle;

import com.blankj.utilcode.util.AppUtils;
import com.lhs.library.base.BaseActivity;
import com.pai8.ke.databinding.ActivityAboutBinding;
import com.pai8.ke.manager.UpdateAppManager;
import com.pai8.ke.viewmodel.AboutViewModel;

import org.jetbrains.annotations.Nullable;

public class AboutActivity extends BaseActivity<AboutViewModel, ActivityAboutBinding> {

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.tvAppVersion.setText("V" + AppUtils.getAppVersionName());
        mBinding.btnUpgrade.setOnClickListener(v -> mViewModel.checkUpgrade());
    }

    @Override
    public void addObserve() {
        mViewModel.getCheckUpgradeData().observe(this, data -> {
            UpdateAppManager.showUpdateDialog(this, data);
        });
    }
}
