package com.pai8.ke.activity.takeaway.presenter;

import com.pai8.ke.activity.takeaway.api.TakeawayApi;
import com.pai8.ke.activity.takeaway.contract.ConfirmContract;
import com.pai8.ke.activity.takeaway.entity.resq.WaimaiResq;
import com.pai8.ke.base.BasePresenterImpl;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.manager.AccountManager;

import java.util.HashMap;

public class ConfirmOrderPresenter extends BasePresenterImpl<ConfirmContract.View> {

    public ConfirmOrderPresenter(ConfirmContract.View view) {
        super(view);
    }



    public void waimaiPrice(int shop_id,int address_id){
        HashMap<String, Object> map = new HashMap<>();
        map.put("shop_id",shop_id);
        map.put("address_id",address_id);
        TakeawayApi.getInstance().waimaiPrice(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<WaimaiResq>() {
                    @Override
                    protected void onSuccess(WaimaiResq data){
                        view.waimaiSuccess(data);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }


    public void addOrder(String goods_info,String shop_id,int order_type,int address_id,String express_price
            ,String order_discount_coupon_id,String express_discount_coupon_id,String box_price){
        HashMap<String, Object> map = new HashMap<>();
        map.put("goods_info",goods_info);
        map.put("buyer_id", AccountManager.getInstance().getUid());
        map.put("shop_id",shop_id);
        map.put("order_type",order_type);  //1邮寄  2外卖 3核销
        map.put("address_id",address_id);
        map.put("express_price",express_price);  //配送费用
        map.put("order_discount_coupon_id",order_discount_coupon_id);  //满减优惠券ID
        map.put("express_discount_coupon_id",express_discount_coupon_id);
        map.put("box_price",box_price);
        TakeawayApi.getInstance().addOrder(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String data){
                        view.orderSuccess(data);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }

}
