package com.pai8.ke.activity.takeaway.order;

import android.content.Intent;
import android.os.Bundle;

import com.lhs.library.base.BaseAppConstants;
import com.lhs.library.base.BaseFragment;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.UserTakeawayOrderAdapter;
import com.pai8.ke.activity.takeaway.entity.OrderListResult;
import com.pai8.ke.activity.takeaway.entity.resq.StoreInfoResult;
import com.pai8.ke.activity.takeaway.ui.StoreActivity;
import com.pai8.ke.databinding.FragmentTakeawayOrderListBinding;
import com.pai8.ke.fragment.pay.PayDialogFragment;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.shop.viewmodel.OrderListViewModel;
import com.pai8.ke.utils.AppUtils;

import org.jetbrains.annotations.Nullable;

public class UserTakeawayOrderListFragment extends BaseFragment<OrderListViewModel, FragmentTakeawayOrderListBinding> {

    private UserTakeawayOrderAdapter mAdapter;

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mBinding.smartRefreshLayout.setOnRefreshListener(refreshLayout -> initData());
        mBinding.recyclerView.setAdapter(mAdapter = new UserTakeawayOrderAdapter(null));

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
//            if (mAdapter.getData().get(position).getOrder_status() == 2
//                    || mAdapter.getData().get(position).getOrder_status() == 3
//                    || mAdapter.getData().get(position).getOrder_status() == 7) {
//                startActivity(new Intent(getActivity(), UserTakeawayOrderDetailActivity.class).putExtra("order", mAdapter.getData().get(position)));
//            } else {
//                startActivity(new Intent(getActivity(), UserGroupOrderDetailActivity.class).putExtra("order", mAdapter.getData().get(position)));
//            }
            startActivity(new Intent(getActivity(), UserTakeawayOrderDetailActivity.class)
                    .putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0, mAdapter.getData().get(position).getOrder_no()));
        });

        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            OrderListResult orderInfo = mAdapter.getData().get(position);
            if (view.getId() == R.id.tv_cancel) {
                if (orderInfo.getOrder_status() == 0
                        || orderInfo.getOrder_status() == 1) {  //取消订单
                    mViewModel.cancelOrder(mAdapter.getData().get(position).getOrder_no());
                }
            } else if (view.getId() == R.id.tv_food_status) {
                if (orderInfo.getOrder_status() == 0) {
                    PayDialogFragment payDialogFragment = PayDialogFragment.newInstance(orderInfo.getOrder_price(), orderInfo.getOrder_no());
                    payDialogFragment.show(getChildFragmentManager(), "pay");
                } else if (orderInfo.getOrder_status() == 1) {  //联系商家
                    AppUtils.intentCallPhone(getActivity(), orderInfo.getShop_phone());
                } else if (orderInfo.getOrder_status() == 9) {  //重新下单
                    StoreInfoResult storeInfo = new StoreInfoResult();
                    storeInfo.id = orderInfo.getShop_id();
                    Intent intent = new Intent(getActivity(), StoreActivity.class);
                    intent.putExtra("storeInfo", storeInfo);
                    intent.putExtra("orderNo", orderInfo.getOrder_no());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void initData() {
        mViewModel.orderList(Integer.parseInt(AccountManager.getInstance().getUid()), 2);
    }

    @Override
    public void addObserve() {
        mViewModel.getOrderListData().observe(this, data -> {
            mAdapter.setNewData(data);
            mBinding.smartRefreshLayout.finishRefresh();
        });
        mViewModel.getCancelOrderData().observe(this, data -> {
            initData();
        });
    }

}
