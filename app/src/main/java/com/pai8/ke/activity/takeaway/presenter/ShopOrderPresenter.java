package com.pai8.ke.activity.takeaway.presenter;

import com.pai8.ke.activity.takeaway.api.TakeawayApi;
import com.pai8.ke.activity.takeaway.contract.ShopOrderContract;
import com.pai8.ke.activity.takeaway.entity.OrderInfo;
import com.pai8.ke.base.BasePresenterImpl;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.manager.AccountManager;

import java.util.HashMap;
import java.util.List;

public class ShopOrderPresenter extends BasePresenterImpl<ShopOrderContract.View> {

    public ShopOrderPresenter(ShopOrderContract.View view) {
        super(view);
    }

    public void orderList(String order_status,int page){

        HashMap<String, Object> map = new HashMap<>();
        map.put("shop_id", AccountManager.getInstance().getShopId());
        map.put("order_status",order_status); //订单状态 0为待支付 1为已支付 2为商家已接单 7为订单制作完成 3为配送中 4为订单已完成 5为订单已申请退款 6订单被拒绝退款 8为订单已退款 9为订单已取消 -1为支付超时 -2订单拒绝接单
        map.put("page",page);
        TakeawayApi.getInstance().shopOrderList(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<List<OrderInfo>>() {
                    @Override
                    protected void onSuccess(List<OrderInfo> data){

                        view.getShopListSuccess(data);
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

                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }


}
