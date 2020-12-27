package com.pai8.ke.activity.takeaway.ui;

import android.view.View;

import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.SecondAdminManagerAdapter;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SecondAdminManagerActivity extends BaseMvpActivity implements View.OnClickListener {

    private RecyclerView mRvSecondAdmin;
    private SecondAdminManagerAdapter mAdapter;


    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_second_admin_manager;
    }

    @Override
    public void initView() {

        setImmersionBar(R.id.base_tool_bar);
        findViewById(R.id.toolbar_back_all).setOnClickListener(this);
        findViewById(R.id.tv_next).setOnClickListener(this);
        mRvSecondAdmin = findViewById(R.id.rv_second_admin);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvSecondAdmin.setLayoutManager(layoutManager);


    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.toolbar_back_all){
            finish();
        }else if(v.getId() == R.id.tv_next){
            
        }
    }


    @Override
    public void initData() {
        super.initData();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("1");
        }
        mAdapter = new SecondAdminManagerAdapter(list);
        mRvSecondAdmin.setAdapter(mAdapter);
    }
}
