package com.pai8.ke.activity.takeaway.presenter;

import com.pai8.ke.activity.takeaway.Constants;
import com.pai8.ke.activity.takeaway.api.TakeawayApi;
import com.pai8.ke.activity.takeaway.contract.StoreContract;
import com.pai8.ke.activity.takeaway.entity.FoodGoodInfo;
import com.pai8.ke.activity.takeaway.entity.ShopFoodGoodInfo;
import com.pai8.ke.activity.takeaway.entity.event.ShopDataEvent;
import com.pai8.ke.activity.takeaway.entity.req.ShopIdReq;
import com.pai8.ke.activity.takeaway.entity.resq.ShopContent;
import com.pai8.ke.base.BasePresenterImpl;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.manager.AccountManager;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;

public class StorePresenter extends BasePresenterImpl<StoreContract.View> {


    public StorePresenter(StoreContract.View view) {
        super(view);
    }

    public void shopContent(ShopIdReq req){

        TakeawayApi.getInstance().shopContent(req)
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<ShopContent>() {
                    @Override
                    protected void onSuccess(ShopContent data){
                        if(view!=null){
                            view.getShopContentSuccess(data);
                            EventBus.getDefault().post(new ShopDataEvent(Constants.EVENT_TYPE_SHOP_CONTENT,data));
                        }
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }


    public void collection(ShopIdReq shopIdReq){
        TakeawayApi.getInstance().collection(shopIdReq)
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String shopList) {

                        view.collectionSuccess(shopList);

                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }


    public void unCollection(ShopIdReq shopIdReq){
        TakeawayApi.getInstance().unCollection(shopIdReq)
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String shopList) {
                        view.unCollectionSuccess(shopList);


                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }

    public void outDistance(String shopId,String addressId){
        HashMap<String, Object> map = new HashMap<>(2);
        map.put("shop_id", shopId);
        map.put("address_id",addressId);
        TakeawayApi.getInstance().outDistance(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String shopList) {
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                        if(errorCode == 0){
                            view.onFail(msg);
                        }
                    }
                });
    }


    public void getCart(String shop_id){
        HashMap<String, Object> map = new HashMap<>();
        map.put("buyer_id", AccountManager.getInstance().getUid());
        map.put("shop_id",shop_id);
        TakeawayApi.getInstance().getCart(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<ShopFoodGoodInfo>() {
                    @Override
                    protected void onSuccess(ShopFoodGoodInfo data){

                        view.getCarSuccess(data);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }


    public void reAddCart(String orderNo){
        HashMap<String, Object> map = new HashMap<>();
        map.put("buyer_id", AccountManager.getInstance().getUid());
        map.put("order_no",orderNo);
        TakeawayApi.getInstance().reAddCart(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<List<FoodGoodInfo>>() {
                    @Override
                    protected void onSuccess(List<FoodGoodInfo> data){
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }


}
