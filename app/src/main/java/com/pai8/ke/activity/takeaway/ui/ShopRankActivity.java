package com.pai8.ke.activity.takeaway.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.ShopRankAdapter;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ShopRankActivity extends BaseMvpActivity implements View.OnClickListener {


    private int mType;  //0 销售数量， 1销售收入
    private RecyclerView mRvRank;
    private ShopRankAdapter mAdapter;

    private ImageView mIvTop;

    private TextView mTvTitle;


    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_shop_rank;
    }

    @Override
    public void initView() {
        mType = getIntent().getIntExtra("type",0);

        findViewById(R.id.toolbar_back_all).setOnClickListener(this);
        mIvTop = findViewById(R.id.iv_top);
        mTvTitle = findViewById(R.id.tv_title);
        mRvRank = findViewById(R.id.rv_rank);
        mRvRank.setLayoutManager(new LinearLayoutManager(this));

        mTvTitle.setText(mType == 0 ? "商品月销量排行" : "商品月收入排行");
        mIvTop.setBackgroundResource(mType == 0 ? R.mipmap.bg_store_month_num : R.mipmap.bg_store_month_income);

        setImmersionBar(R.id.iv_top);

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("1");
        }
        mAdapter = new ShopRankAdapter(list);
        mRvRank.setAdapter(mAdapter);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.toolbar_back_all){
            finish();
        }
    }
}
