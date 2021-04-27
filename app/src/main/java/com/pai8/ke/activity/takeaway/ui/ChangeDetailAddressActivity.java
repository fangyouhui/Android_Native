package com.pai8.ke.activity.takeaway.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.lhs.library.base.BaseActivity;
import com.lhs.library.base.BaseAppConstants;
import com.lhs.library.base.NoViewModel;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.databinding.ActivityChangeDetailAddressBinding;
import com.pai8.ke.entity.Address;
import com.pai8.ke.global.EventCode;
import com.pai8.ke.utils.EventBusUtils;
import com.pai8.ke.utils.ToastUtils;

import org.jetbrains.annotations.Nullable;


public class ChangeDetailAddressActivity extends BaseActivity<NoViewModel, ActivityChangeDetailAddressBinding> {

    private Address mAddress;

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mAddress = (Address) getIntent().getSerializableExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0);
        mBinding.etChangeAddress.setText(mAddress.getTitle());
        mBinding.btnConfirm.setOnClickListener(v -> {
            String address = mBinding.etChangeAddress.getText().toString().trim();
            if (TextUtils.isEmpty(address)) {
                ToastUtils.showShort("请输入详细街道");
                return;
            }
            mAddress.setTitle(address);

            EventBusUtils.sendEvent(new BaseEvent(EventCode.EVENT_CHOOSE_ADDRESS, mAddress));
            Intent intent = new Intent();
            intent.putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0, mAddress);
            setResult(RESULT_OK, intent);
            finish();
        });
    }

}
