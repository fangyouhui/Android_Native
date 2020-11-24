package com.pai8.ke.activity.takeaway.ui;

import android.view.View;
import android.widget.EditText;

import com.pai8.ke.R;
import com.pai8.ke.base.BaseActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ShopGoodSearchActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView mRvShop;
    private EditText mEtSearch;
    private String key;
    private int page = 1;
    private String mSearch = "";


    @Override
    public int getLayoutId() {
        return R.layout.activity_shop_good_search;
    }

    @Override
    public void initView() {
        setImmersionBar(R.id.base_tool_bar);
        findViewById(R.id.toolbar_back_all).setOnClickListener(this);
        mEtSearch = findViewById(R.id.et_search);
        mRvShop = findViewById(R.id.rv_shop);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvShop.setLayoutManager(layoutManager);

    }


    @Override
    public void initData() {
        super.initData();

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toolbar_back_all) {
            finish();
        } else if (v.getId() == R.id.tv_relocation) {
        }
    }
}
