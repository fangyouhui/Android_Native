package com.pai8.ke.activity.takeaway.ui;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pai8.ke.R;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.base.BasePresenter;
import com.pai8.ke.widget.BottomDialog;

public class StoreManagerActivity extends BaseMvpActivity implements View.OnClickListener {

    private BottomDialog mStoreStatusDialog;
    private TextView mTvStatus;



    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_store_manager;
    }

    @Override
    public void initView() {

        setImmersionBar(R.id.iv_top);
        findViewById(R.id.toolbar_back_all).setOnClickListener(this);
        findViewById(R.id.tv_order_manager).setOnClickListener(this);
        findViewById(R.id.tv_good_manager).setOnClickListener(this);
        findViewById(R.id.toolbar_iv_menu).setOnClickListener(this);
        findViewById(R.id.tv_second_manager).setOnClickListener(this);
        mTvStatus = findViewById(R.id.tv_status);
        mTvStatus.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.toolbar_back_all:
                finish();
                break;
            case R.id.tv_status:
                showBottomDialog();
                break;
            case R.id.tv_good_manager:
                startActivity(new Intent(this,GoodManagerActivity.class));
                break;
            case R.id.tv_order_manager:
                startActivity(new Intent(this,OrderProcessingActivity.class));
                break;
            case R.id.toolbar_iv_menu:
                startActivity(new Intent(this,MerchantSettledFirstActivity.class));
                break;
            case R.id.tv_second_manager:
                startActivity(new Intent(this,SecondAdminManagerActivity.class));
                break;
        }

    }

    private void showBottomDialog() {
        View view = View.inflate(this, R.layout.dialog_store_status, null);
        ImageButton itnClose = view.findViewById(R.id.itn_close);
        itnClose.setOnClickListener(view1 -> {
            mStoreStatusDialog.dismiss();
        });

        if (mStoreStatusDialog == null) {
            mStoreStatusDialog = new BottomDialog(this, view);
        }
        mStoreStatusDialog.setIsCanceledOnTouchOutside(true);
        mStoreStatusDialog.show();
    }



}
