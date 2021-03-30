package com.pai8.ke.activity.takeaway.order;

import android.os.Bundle;

import com.lhs.library.base.BaseFragment;
import com.pai8.ke.activity.takeaway.adapter.GroupOrderAdapter;
import com.pai8.ke.databinding.FragmentGroupOrderListBinding;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.shop.viewmodel.GroupOrderListViewModel;

import org.jetbrains.annotations.Nullable;

public class GroupOrderListFragment extends BaseFragment<GroupOrderListViewModel, FragmentGroupOrderListBinding> {
    private GroupOrderAdapter adapter;

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.smartRefreshLayout.setOnRefreshListener(refreshLayout -> initData());
        mBinding.recyclerView.setAdapter(adapter = new GroupOrderAdapter(getContext(), null));
    }

    @Override
    public void addObserve() {
        mViewModel.getOrderListData().observe(this, data -> {
            adapter.setData(data);
            mBinding.smartRefreshLayout.finishRefresh();
        });
    }

    @Override
    public void initData() {
        int uid = Integer.parseInt(AccountManager.getInstance().getUid());
        mViewModel.orderList(uid, 3);
    }
}
