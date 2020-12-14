package com.pai8.ke.activity.takeaway.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.hjq.bar.OnTitleBarListener;
import com.pai8.ke.R;
import com.pai8.ke.base.BaseActivity;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.entity.Address;
import com.pai8.ke.utils.EventBusUtils;
import com.pai8.ke.utils.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.pai8.ke.global.EventCode.EVENT_CHOOSE_ADDRESS;

/**
 * @author Created by zzf
 * @time 21:58
 * Description：
 */
public class ChangeDetailAddressActivity extends BaseActivity {


    @BindView(R.id.et_change_address)
    EditText etChangeAddress;
    private Address mAddress;

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_detail_address;
    }

    @Override
    public void initListener() {
        super.initListener();
        mTitleBar.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(View v) {
                finish();
            }

            @Override
            public void onTitleClick(View v) {

            }

            @Override
            public void onRightClick(View v) {

            }
        });
    }

    @Override
    public void initView() {
        mTitleBar.setTitle("详细街道");
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            finish();
            return;
        }
        mAddress = (Address) bundle.getSerializable("ADDRESS");
        if (mAddress != null) {
            etChangeAddress.setText(mAddress.getAddress());
        } else {
            finish();
        }
    }


    @OnClick(R.id.btn_confirm)
    public void onViewClicked() {
        String address = etChangeAddress.getText().toString().trim();
        if (StringUtils.isEmpty(address)) {
            toast("请输入详细街道");
            return;
        }
//        if (!address.contains("街") || !address.contains("号")) {
//            toast("请输入正确格式的详细街道");
//            return;
//        }
        mAddress.setAddress(address);
        EventBusUtils.sendEvent(new BaseEvent(EVENT_CHOOSE_ADDRESS, mAddress));
        finish();
    }
}
