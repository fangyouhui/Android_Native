package com.pai8.ke.activity.takeaway.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.lhs.library.base.BaseActivity;
import com.pai8.ke.databinding.ActivityShopWithDrawBinding;
import com.pai8.ke.groupBuy.adapter.TextWatcherAdapter;
import com.pai8.ke.viewmodel.ShopWithDrawlViewModel;

import org.jetbrains.annotations.Nullable;

public class ShopWithDrawActivity extends BaseActivity<ShopWithDrawlViewModel, ActivityShopWithDrawBinding> {

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.etMoney.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                mBinding.btnConfirm.setEnabled(TextUtils.isEmpty(mBinding.etMoney.getText().toString()) ? false : true);
            }
        });
        mBinding.btnConfirm.setOnClickListener(v -> submit());
    }

    @Override
    public void initData() {
        mViewModel.memberIncomeList(1, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "提现记录").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == 1) {
            startActivity(new Intent(this, ShopWithDrawRecordActivity.class));
        }
        return true;
    }

    @Override
    public void addObserve() {
        mViewModel.getMemberIncomeListData().observe(this, data -> {

        });
        mViewModel.getShopCashData().observe(this, data -> {
            startActivity(new Intent(this, ShopWithDrawSuccessActivity.class));
            finish();

        });
    }

    private void submit() {
        double money = Double.valueOf(mBinding.etMoney.getText().toString());
        mViewModel.shopCash(money + "");
    }
}
