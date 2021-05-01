package com.pai8.ke.activity.video;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.KeyboardUtils;
import com.lhs.library.base.BaseActivity;
import com.lhs.library.base.BaseAppConstants;
import com.pai8.ke.activity.video.adapter.ShopSearchListAdapter;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.databinding.ActivityShopSearchListBinding;
import com.pai8.ke.global.EventCode;
import com.pai8.ke.groupBuy.adapter.TextWatcherAdapter;
import com.pai8.ke.utils.EventBusUtils;
import com.pai8.ke.viewmodel.ShopSearchViewModel;
import com.pai8.ke.widget.SpaceItemDecoration;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.jetbrains.annotations.Nullable;

/**
 * 商家搜索列表
 * Created by gh on 2020/11/14.
 */
public class ShopSearchListActivity extends BaseActivity<ShopSearchViewModel, ActivityShopSearchListBinding> {
    private ShopSearchListAdapter mAdapter;
    private int mPageNo = 1;
    private String mKeywords = "";
    private boolean showMenu = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        showMenu = getIntent().getBooleanExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0, false);
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "取消关联").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return showMenu;
    }

    public static int REQUEST_CODE_CANCEL = 0X111;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == 1) {
            setResult(REQUEST_CODE_CANCEL);
            finish();
        }
        return true;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mAdapter = new ShopSearchListAdapter(this, null);
        mBinding.recyclerView.addItemDecoration(new SpaceItemDecoration(2, 0, 0, 0));
        mBinding.recyclerView.setHasFixedSize(true);
        mBinding.recyclerView.setAdapter(mAdapter);
        mAdapter.setListener((item, position) -> {
            EventBusUtils.sendEvent(new BaseEvent(EventCode.EVENT_CHOOSE_SHOP, item));
            Intent intent = new Intent();
            intent.putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0, item);
            setResult(RESULT_OK, intent);
            finish();
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

        mBinding.tvBtnCancel.setOnClickListener(v -> {
            mBinding.etSearch.setText("");
            KeyboardUtils.hideSoftInput(this);
            initData();
        });

        mBinding.etSearch.setOnKeyListener((view, keyCode, keyEvent) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                mKeywords = mBinding.etSearch.getText().toString();
                initData();
            }
            return false;
        });

        mBinding.etSearch.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                mKeywords = mBinding.etSearch.getText().toString();
                if (TextUtils.isEmpty(mKeywords)) {
                    mBinding.tvBtnCancel.setVisibility(View.INVISIBLE);
                } else {
                    mBinding.tvBtnCancel.setVisibility(View.VISIBLE);
                }
                initData();
            }
        });

    }


    @Override
    public void initData() {
        mBinding.tvStatus.setText(TextUtils.isEmpty(mKeywords) ? "附近店铺" : "搜索结果");
        mViewModel.shopSelect(mPageNo, mKeywords);
    }

    @Override
    public void addObserve() {
        mViewModel.getShopSelectData().observe(this, data -> {
            if (mBinding.smartRefreshLayout.isRefreshing()) {
                mAdapter.setData(data);
                mBinding.smartRefreshLayout.finishRefresh();
                return;
            }
            if (mBinding.smartRefreshLayout.isLoading()) {
                if (data == null || data.isEmpty()) {
                    mBinding.smartRefreshLayout.finishLoadMoreWithNoMoreData();
                } else {
                    mAdapter.setData(data);
                    mBinding.smartRefreshLayout.finishLoadMore();
                }
                return;
            }

            mAdapter.setData(data);
        });
    }


    private void onRefreshData() {
        mPageNo = 1;
        initData();
    }

    private void onLoadMoreData() {
        mPageNo++;
        initData();
    }

}

