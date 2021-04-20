package com.pai8.ke.activity.takeaway.presenter;

import com.pai8.ke.activity.takeaway.api.TakeawayApi;
import com.pai8.ke.activity.takeaway.contract.ShopOrderDetailContract;
import com.pai8.ke.activity.takeaway.entity.OrderInfo;
import com.pai8.ke.base.BasePresenterImpl;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.manager.AccountManager;

import java.util.HashMap;
import java.util.List;

public class ShopOrderDetailPresenter extends BasePresenterImpl<ShopOrderDetailContract.View> {

    public ShopOrderDetailPresenter(ShopOrderDetailContract.View view) {
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
                .subscribe(new BaseObserver<List<String>>() {
                    @Override
                    protected void onSuccess(List<String> data){

                        view.orderCancelSuccess(data);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }


    //0为接单 1为拒绝订单 2为同意退款申请 3为拒绝退款申请 4为订单制作完成 5为订单配送操作
    public void shopDealOrder(String order_no,int type){
        HashMap<String, Object> map = new HashMap<>();
        map.put("shop_id", AccountManager.getInstance().getShopId());
        map.put("order_no",order_no);
        map.put("type",type);
        TakeawayApi.getInstance().shopDealOrder(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<List<String>>() {
                    @Override
                    protected void onSuccess(List<String> data){

                        view.getStatusSuccess(data);
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
                .subscribe(new BaseObserver<List<String>>() {
                    @Override
                    protected void onSuccess(List<String> data){

                        view.orderCancelSuccess(data);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }

}
