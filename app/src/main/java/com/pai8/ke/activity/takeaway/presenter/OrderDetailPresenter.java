package com.pai8.ke.activity.takeaway.presenter;

import com.pai8.ke.activity.takeaway.api.TakeawayApi;
import com.pai8.ke.activity.takeaway.contract.OrderDetailContract;
import com.pai8.ke.activity.takeaway.entity.OrderInfo;
import com.pai8.ke.base.BasePresenterImpl;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.manager.AccountManager;

import java.util.HashMap;

public class OrderDetailPresenter extends BasePresenterImpl<OrderDetailContract.View> {

    public OrderDetailPresenter(OrderDetailContract.View view) {
        super(view);
    }

    public void orderDetail(String order_no){
        HashMap<String, Object> map = new HashMap<>();
        map.put("order_no",order_no);
        TakeawayApi.getInstance().orderDetail(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<OrderInfo>() {
                    @Override
                    protected void onSuccess(OrderInfo data){
                        view.orderDetailSuccess(data);
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

    public void applyRefund(String order_no){
        HashMap<String, Object> map = new HashMap<>();
        map.put("buyer_id", AccountManager.getInstance().getUid());
        map.put("order_no",order_no);
        map.put("reason","");
        TakeawayApi.getInstance().applyRefund(createRequestBody(map))
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
