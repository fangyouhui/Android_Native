package com.pai8.ke.activity.takeaway.ui;

import android.os.Bundle;

import com.lhs.library.base.BaseActivity;
import com.lhs.library.base.NoViewModel;
import com.pai8.ke.databinding.ActivityWriteOffErrorBinding;

import org.jetbrains.annotations.Nullable;

public class WriteOffErrorActivity extends BaseActivity<NoViewModel, ActivityWriteOffErrorBinding> {

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mBinding.btnBack.setOnClickListener(v -> finish());
    }
}
