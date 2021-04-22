package com.pai8.ke.groupBuy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;

import androidx.annotation.NonNull;

import com.lhs.library.base.BaseActivity;
import com.lhs.library.base.BaseAppConstants;
import com.pai8.ke.databinding.ActivityGroupBuySearchBinding;
import com.pai8.ke.groupBuy.adapter.GroupBuyTypeAdapter;
import com.pai8.ke.groupBuy.adapter.TextWatcherAdapter;
import com.pai8.ke.groupBuy.viewmodel.GroupBuyMainViewModel;
import com.pai8.ke.shop.ui.BusinessHomeActivity;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.jetbrains.annotations.Nullable;

public class GroupBuySearchActivity extends BaseActivity<GroupBuyMainViewModel, ActivityGroupBuySearchBinding> {

    private GroupBuyTypeAdapter adapter;
    private int pageNo = 1;
    private String keyWord;

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.ivBtnBack.setOnClickListener(v -> finish());
        mBinding.recyclerView.setAdapter(adapter = new GroupBuyTypeAdapter(this, null));
        adapter.setListener((item, position) -> {
            Intent intent = new Intent(getBaseContext(), BusinessHomeActivity.class);
            intent.putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0, item.getId() + "");
            startActivity(intent);
        });

        mBinding.smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                onLoadMoreData();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                onRefreshData();
            }
        });
        mBinding.etSearch.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                onSearch();
            }
        });

        mBinding.etSearch.setOnKeyListener((v, keyCode, event) -> {        // 开始搜索
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                com.blankj.utilcode.util.KeyboardUtils.hideSoftInput(this);
                onSearch();
                return true;
            }
            return false;
        });

    }

    private void onRefreshData() {
        pageNo = 1;
    }

    private void onLoadMoreData() {
        pageNo++;
    }

    private void onSearch() {
        keyWord = mBinding.etSearch.getText().toString();
        pageNo = 1;
    }

    @Override
    public void addObserve() {

    }
}
