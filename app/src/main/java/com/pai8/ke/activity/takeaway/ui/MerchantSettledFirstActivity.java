package com.pai8.ke.activity.takeaway.ui;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.Constants;
import com.pai8.ke.activity.takeaway.entity.event.NotifyEvent;
import com.pai8.ke.api.Api;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.base.BasePresenter;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.utils.EventBusUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MerchantSettledFirstActivity extends BaseMvpActivity implements View.OnClickListener, TextWatcher {

    private EditText mEtStoreName, mEtPhone, mEtEmail, mEtAddressDetail, mEtBankNo, mEtBankAddress;
    private TextView mTvCate, mTvAddress;
    private TextView mTvNext;

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_merchant_settled_first;
    }

    @Override
    public void initView() {
        EventBusUtils.register(this);
        setImmersionBar(R.id.base_tool_bar);
        findViewById(R.id.toolbar_back_all).setOnClickListener(this);
        mTvNext = findViewById(R.id.tv_next);
        mTvNext.setOnClickListener(this);
        mEtStoreName = findViewById(R.id.et_store_name);
        mEtEmail = findViewById(R.id.et_email);
        mEtPhone = findViewById(R.id.et_phone);
        mEtAddressDetail = findViewById(R.id.et_address_detail);
        mEtBankAddress = findViewById(R.id.et_bank_address);
        mEtBankNo = findViewById(R.id.et_bank_no);
        mTvAddress = findViewById(R.id.tv_address);
        mTvAddress.setOnClickListener(this);
        mTvCate = findViewById(R.id.tv_cate);
        editListener();
    }


    @Override
    public void initListener() {
        super.initListener();
        mEtStoreName.addTextChangedListener(this);
        mEtPhone.addTextChangedListener(this);
        mEtEmail.addTextChangedListener(this);
        mEtAddressDetail.addTextChangedListener(this);
        mEtBankNo.addTextChangedListener(this);
        mEtBankAddress.addTextChangedListener(this);
        mTvCate.addTextChangedListener(this);
        mTvAddress.addTextChangedListener(this);

    }

    private void editListener() {
        if (TextUtils.isEmpty(mEtStoreName.getText().toString()) || TextUtils.isEmpty(mEtPhone.getText().toString())
                || TextUtils.isEmpty(mEtEmail.getText().toString())|| TextUtils.isEmpty(mEtAddressDetail.getText().toString())
                || TextUtils.isEmpty(mEtBankNo.getText().toString())|| TextUtils.isEmpty(mEtBankAddress.getText().toString())
                || TextUtils.isEmpty(mTvCate.getText().toString())|| TextUtils.isEmpty(mTvAddress.getText().toString())) {
            mTvNext.setBackgroundResource(R.drawable.shape_orgin_gradient_gray);
            mTvNext.setEnabled(false);
        } else {
            mTvNext.setBackgroundResource(R.drawable.shape_orgin_gradient);
            mTvNext.setEnabled(true);
        }
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NotifyEvent event) {
        if (event.type == Constants.EVENT_TYPE_MERCHANT_SETTLED) {
            finish();
        }

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toolbar_back_all) {
            finish();
        } else if (v.getId() == R.id.tv_address) {
            Api.getInstance().getArea()
                    .doOnSubscribe(disposable -> {
                    })
                    .compose(RxSchedulers.io_main())
                    .subscribe(new BaseObserver<String>() {
                        @Override
                        protected void onSuccess(String token) {

                        }

                        @Override
                        protected void onError(String msg, int errorCode) {
                            dismissLoadingDialog();
                            super.onError(msg, errorCode);
                        }
                    });
        } else if (v.getId() == R.id.tv_next) {

            String storeName = mEtStoreName.getText().toString();
            String phone = mEtPhone.getText().toString();
            String email = mEtEmail.getText().toString();
            String cate = mTvAddress.getText().toString();
            String province = "阿法";
            String city = "as";
            String district = "afafa";
            String addressDetail = mEtAddressDetail.getText().toString();
            String bankAddress = mEtBankAddress.getText().toString();
            String bankNo = mEtBankNo.getText().toString();

            Intent intent = new Intent(this, MerchantSettledSecondActivity.class);
            intent.putExtra("storeName", storeName);
            intent.putExtra("phone", phone);
            intent.putExtra("email", email);
            intent.putExtra("cate", cate);
            intent.putExtra("province", province);
            intent.putExtra("city", city);
            intent.putExtra("district", district);
            intent.putExtra("addressDetail", addressDetail);
            intent.putExtra("bankAddress", bankAddress);
            intent.putExtra("bankNo", bankNo);
            startActivity(intent);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        editListener();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
