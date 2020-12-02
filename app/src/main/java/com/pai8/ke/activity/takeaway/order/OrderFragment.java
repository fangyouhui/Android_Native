package com.pai8.ke.activity.takeaway.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.Constants;
import com.pai8.ke.activity.takeaway.adapter.OrderAdapter;
import com.pai8.ke.activity.takeaway.contract.OrderContract;
import com.pai8.ke.activity.takeaway.entity.OrderInfo;
import com.pai8.ke.activity.takeaway.entity.event.NotifyEvent;
import com.pai8.ke.activity.takeaway.entity.resq.StoreInfo;
import com.pai8.ke.activity.takeaway.presenter.OrderPresenter;
import com.pai8.ke.activity.takeaway.ui.StoreActivity;
import com.pai8.ke.base.BaseMvpFragment;
import com.pai8.ke.fragment.pay.PayDialogFragment;
import com.pai8.ke.utils.AppUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
                if (mAdapter.getData().get(position).order_status == 2 || mAdapter.getData().get(position).order_status == 3
                        || mAdapter.getData().get(position).order_status == 7) {
                    startActivity(new Intent(getActivity(), OrderSendActivity.class)
                            .putExtra("order",mAdapter.getData().get(position)));
                }else{
                    startActivity(new Intent(getActivity(), OrderDetailActivity.class)
                            .putExtra("order",mAdapter.getData().get(position)));
                }


            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                OrderInfo orderInfo = mAdapter.getData().get(position);
                if(view.getId() == R.id.tv_cancel){

                    if(orderInfo.order_status == 0 || orderInfo.order_status == 1){  //取消订单
                        mPresenter.cancelOrder(mAdapter.getData().get(position).order_no);
                    }

                }else if(view.getId() == R.id.tv_food_status){
                    if(orderInfo.order_status == 0 ){
                        PayDialogFragment payDialogFragment = PayDialogFragment.newInstance(orderInfo.order_price, orderInfo.order_no);
                        payDialogFragment.show(getChildFragmentManager(), "pay");
                    }else if(orderInfo.order_status == 1){  //联系商家
                        AppUtils.intentCallPhone(getActivity(), orderInfo.shop_info.mobile);
                    }else if(orderInfo.order_status == 9){  //重新下单
                        StoreInfo storeInfo = new StoreInfo();
                        storeInfo.id = orderInfo.shop_id;
                        Intent intent = new Intent(getActivity(), StoreActivity.class);
                        intent.putExtra("storeInfo",storeInfo);
                        intent.putExtra("orderNo",orderInfo.order_no);
                        startActivity(intent);
                    }

                }

            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NotifyEvent event) {
        if (event.type == Constants.EVENT_TYPE_CANCEL_ORDER) {
            mPresenter.orderList(2);
        }

    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
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
