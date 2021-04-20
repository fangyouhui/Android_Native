package com.pai8.ke.activity.takeaway.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.ShopOrderGroupAdapter;
import com.pai8.ke.activity.takeaway.contract.ShopOrderContract;
import com.pai8.ke.activity.takeaway.entity.OrderInfo;
import com.pai8.ke.activity.takeaway.order.ShopOrderDetailActivity;
import com.pai8.ke.activity.takeaway.presenter.ShopOrderPresenter;
import com.pai8.ke.base.BaseMvpFragment;

import java.util.List;

public class ShopGroupOrderListFragment extends BaseMvpFragment<ShopOrderPresenter> implements View.OnClickListener, ShopOrderContract.View{
    private RecyclerView mRvOrder;
    private ShopOrderGroupAdapter mAdapter;
    private int page = 1;
    @Override
    public void onClick(View view) {

    }

    @Override
    public void getShopGroupListSuccess(List<OrderInfo> data) {
        if(page == 1){
            mAdapter.setNewData(data);
        }else{
            mAdapter.addData(data);
            if(data.size()<10){
                mAdapter.setEnableLoadMore(false);
            }

        }

    }

    @Override
    public void getStatusSuccess(List<String> data) {
        mPresenter.groudOrderList(page);
    }

    @Override
    public void getShopListSuccess(List<OrderInfo> data) {
    }



    @Override
    public ShopOrderPresenter initPresenter() {
        return new ShopOrderPresenter(this);
    }

    /**
     * 设置根布局资源id
     */
    @Override
    public int getLayoutId() {
        return R.layout.activity_order_main;
    }




    /**
     * 初始化View
     *
     * @param arguments 接收到的从其他地方传递过来的参数
     */
    @Override
    protected void initView(Bundle arguments) {

    }

    @Override
    public void initData() {
        super.initData();
        mRvOrder = mRootView.findViewById(R.id.rv_order);
        mRvOrder.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new ShopOrderGroupAdapter(null);
        mRvOrder.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(mActivity, ShopOrderDetailActivity.class)
                        .putExtra("order",mAdapter.getData().get(position)));

            }
        });

        mPresenter.groudOrderList(page);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                mPresenter.groudOrderList(page);

            }
        },mRvOrder);

    }
}
