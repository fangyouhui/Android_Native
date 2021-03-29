package com.pai8.ke.groupBuy.ui;

import android.content.Intent;
import android.os.Bundle;

import com.lhs.library.base.BaseAppConstants;
import com.lhs.library.base.BaseFragment;
import com.pai8.ke.databinding.FragmentGroupBuyBusinessListBinding;
import com.pai8.ke.entity.BusinessTypeResult;
import com.pai8.ke.groupBuy.adapter.GroupBuyTypeAdapter;
import com.pai8.ke.groupBuy.viewmodel.GroupBuyMainViewModel;
import com.pai8.ke.shop.ui.BusinessHomeActivity;

import org.jetbrains.annotations.Nullable;

public class GroupBuyBusinessListFragment extends BaseFragment<GroupBuyMainViewModel, FragmentGroupBuyBusinessListBinding> {
    private BusinessTypeResult businessTypeResult;
    private GroupBuyTypeAdapter adapter;

    public static GroupBuyBusinessListFragment newInstance(BusinessTypeResult status) {
        GroupBuyBusinessListFragment fragment = new GroupBuyBusinessListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BaseAppConstants.BundleConstant.ARG_PARAMS_0, status);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        businessTypeResult = (BusinessTypeResult) getArguments().getSerializable(BaseAppConstants.BundleConstant.ARG_PARAMS_0);
        mBinding.recyclerView2.setAdapter(adapter = new GroupBuyTypeAdapter(getContext(), null));
        adapter.setListener((item, position) -> {
            Intent intent = new Intent(getContext(), BusinessHomeActivity.class);
            intent.putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0, item.getId() + "");
            startActivity(intent);
        });

    }

    public void onRefresh() {
        page = 1;
        initData();
    }

    public void onLoadMore() {
        page++;
        initData();
    }

    private int page = 1;

    @Override
    public void initData() {
        mViewModel.getGroupShopList(String.valueOf(page), "10", "", businessTypeResult.getId() + "");
    }


    @Override
    public void addObserve() {
        mViewModel.getGetGroupShopListData().observe(getViewLifecycleOwner(), data -> {
            GroupBuyMainActivity groupBuyMainActivity = null;
            if (getActivity() instanceof GroupBuyMainActivity) {
                groupBuyMainActivity = (GroupBuyMainActivity) getActivity();
            }
            if (page == 1) {
                adapter.setData(data.getList());
                if (groupBuyMainActivity != null) {
                    groupBuyMainActivity.setBanner(data.getBanner());
                    groupBuyMainActivity.finishRefresh();
                }
            } else {
                if (data == null || data.getList().isEmpty()) {
                    if (groupBuyMainActivity != null) {
                        groupBuyMainActivity.finishLoadMore(true);
                    }
                } else {
                    adapter.addData(data.getList());
                    if (groupBuyMainActivity != null) {
                        groupBuyMainActivity.finishLoadMore(false);
                    }
                }
            }
        });
    }


}
