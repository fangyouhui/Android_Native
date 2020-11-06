package com.pai8.ke.activity.takeaway.ui;

import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.TakeawayAdapter;
import com.pai8.ke.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TakeawayActivity extends BaseActivity implements View.OnClickListener {


    private TakeawayAdapter mAdapter;
    private RecyclerView mRvStore;

    @Override
    public int getLayoutId() {
        return R.layout.activity_takeaway;
    }

    @Override
    public void initView() {
        setImmersionBar(R.id.base_tool_bar);
        mRvStore = findViewById(R.id.rv_store);
        findViewById(R.id.toolbar_back_all).setOnClickListener(this);
        findViewById(R.id.toolbar_iv_menu).setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRvStore.setLayoutManager(linearLayoutManager);

    }


    @Override
    public void initData() {
        super.initData();

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("1");
        }
        mAdapter = new TakeawayAdapter(list);
        mRvStore.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                startActivity(new Intent(TakeawayActivity.this, StoreActivity.class));
            }
        });


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toolbar_back_all) {
            startActivity(new Intent(this, StoreManagerActivity.class));

        } else if (v.getId() == R.id.toolbar_iv_menu) {

            startActivity(new Intent(this, AddressActivity.class));
        }
    }
}
