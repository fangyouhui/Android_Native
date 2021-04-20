package com.pai8.ke.activity.takeaway.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.lhs.library.base.BaseAppConstants;
import com.lhs.library.base.BaseFragment;
import com.pai8.ke.activity.takeaway.adapter.UserGroupOrderAdapter;
import com.pai8.ke.activity.takeaway.entity.OrderListResult;
import com.pai8.ke.databinding.FragmentGroupOrderListBinding;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.shop.ui.CommentActivity;
import com.pai8.ke.shop.ui.ShopProductDetailActivity;
import com.pai8.ke.shop.viewmodel.OrderListViewModel;

import org.jetbrains.annotations.Nullable;

public class UserGroupOrderListFragment extends BaseFragment<OrderListViewModel, FragmentGroupOrderListBinding> {
    private UserGroupOrderAdapter adapter;
    private ActivityResultLauncher activityResultLauncher;

    @Override
    public void onCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                initData();
            }
        });
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.smartRefreshLayout.setOnRefreshListener(refreshLayout -> initData());
        mBinding.recyclerView.setAdapter(adapter = new UserGroupOrderAdapter(getContext(), null));

        adapter.setOnItemListener(new UserGroupOrderAdapter.OnItemListener() {
            @Override
            public void onItemComment(OrderListResult bean, int position) {
                Intent intent = new Intent(getContext(), CommentActivity.class);
                intent.putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0, bean.getOrder_no());
                intent.putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_1, bean.getShop_id() + "");
                intent.putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_2, bean.getGoods_info().size() + "");
                intent.putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_3, bean.getGoods_info().get(0));
                activityResultLauncher.launch(intent);
            }

            @Override
            public void onItemReOrder(OrderListResult bean, int position) {
                Intent intent = new Intent(getContext(), ShopProductDetailActivity.class);
                intent.putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0, bean.getShop_id() + "");
                intent.putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_1, bean.getGoods_info().get(0).getId() + "");
                startActivity(intent);
            }

            @Override
            public void onItemClick(OrderListResult item, int position) {
                Intent intent = new Intent(getContext(), UserGroupOrderDetailActivity.class);
                intent.putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0, item.getOrder_no());
                startActivity(intent);
            }
        });

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (adapter != null) adapter.destroy();
    }
}
