package com.pai8.ke.activity.takeaway;

import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.TakeawayAdapter;
import com.pai8.ke.base.BaseActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TakeawayActivity extends BaseActivity {


    private TakeawayAdapter mAdapter;
    private RecyclerView mRvStore;

    @Override
    public int getLayoutId() {
        return R.layout.activity_takeaway;
    }

    @Override
    public void initView() {

        mRvStore = findViewById(R.id.rv_store);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRvStore.setLayoutManager(linearLayoutManager);

    }
}
