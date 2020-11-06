package com.pai8.ke.activity.takeaway.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.GroupBuyManagerAdapter;
import com.pai8.ke.activity.takeaway.ui.AddGoodActivity;
import com.pai8.ke.base.BaseMvpFragment;
import com.pai8.ke.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TakeawayManagerFragment extends BaseMvpFragment implements OnClickListener {

    private RecyclerView mRvGroupBuy;
    private GroupBuyManagerAdapter mAdapter;

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_takeaway_manager;
    }

    @Override
    protected void initView(Bundle arguments) {
        mRvGroupBuy = mRootView.findViewById(R.id.rv_group_buy);
        mRootView.findViewById(R.id.tv_add).setOnClickListener(this);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
        mRvGroupBuy.setLayoutManager(layoutManager);
    }

    @Override
    protected void initData() {
        super.initData();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("1");
        }
        mAdapter = new GroupBuyManagerAdapter(list);
        mRvGroupBuy.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tv_add){
            startActivity(new Intent(mActivity, AddGoodActivity.class));

        }
    }
}
