package com.pai8.ke.activity.takeaway.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.OrderAdapter;
import com.pai8.ke.activity.takeaway.contract.OrderContract;
import com.pai8.ke.activity.takeaway.entity.OrderInfo;
import com.pai8.ke.activity.takeaway.presenter.OrderPresenter;
import com.pai8.ke.base.BaseMvpFragment;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrderFragment extends BaseMvpFragment<OrderPresenter> implements OrderContract.View {

    private RecyclerView mRvOrder;
    private OrderAdapter mAdapter;
    @Override
    public OrderPresenter initPresenter() {
        return new OrderPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order;
    }

    @Override
    protected void initView(Bundle arguments) {
        mRvOrder = mRootView.findViewById(R.id.rv_order);
        mRvOrder.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    protected void initData() {
        super.initData();

        mPresenter.orderList(2);
        mAdapter = new OrderAdapter(null);
        mRvOrder.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                startActivity(new Intent(getActivity(), OrderDetailActivity.class)
                .putExtra("order",mAdapter.getData().get(position)));
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(view.getId() == R.id.tv_cancel){
                    mPresenter.cancelOrder(mAdapter.getData().get(position).order_no);

                }

            }
        });



    }

    @Override
    public void orderListSuccess(List<OrderInfo> data) {
        mAdapter.setNewData(data);
    }

    @Override
    public void orderCancelSuccess(String data) {
        mPresenter.orderList(2);
    }
}
