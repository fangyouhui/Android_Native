package com.pai8.ke.activity.takeaway.presenter;

import com.pai8.ke.activity.takeaway.api.TakeawayApi;
import com.pai8.ke.activity.takeaway.contract.OrderContract;
import com.pai8.ke.activity.takeaway.entity.OrderInfo;
import com.pai8.ke.base.BasePresenterImpl;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.manager.AccountManager;

import java.util.HashMap;
import java.util.List;

public class OrderPresenter extends BasePresenterImpl<OrderContract.View> {

    public OrderPresenter(OrderContract.View view) {
        super(view);
    }

    public void orderList(int order_type){
        HashMap<String, Object> map = new HashMap<>();
        map.put("buyer_id", AccountManager.getInstance().getUid());
        map.put("order_type",order_type+"");
        TakeawayApi.getInstance().orderList(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<List<OrderInfo>>() {
                    @Override
                    protected void onSuccess(List<OrderInfo> data){
                        view.orderListSuccess(data);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }

    public void cancelOrder(String order_no){
        HashMap<String, Object> map = new HashMap<>();
        map.put("buyer_id", AccountManager.getInstance().getUid());
        map.put("order_no",order_no);
        TakeawayApi.getInstance().cancelOrder(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String data){

                        view.orderCancelSuccess(data);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }




}
