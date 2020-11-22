package com.pai8.ke.activity.takeaway.presenter;

import com.pai8.ke.activity.takeaway.api.TakeawayApi;
import com.pai8.ke.activity.takeaway.contract.TakeawayContract;
import com.pai8.ke.activity.takeaway.entity.req.ShopListReq;
import com.pai8.ke.activity.takeaway.entity.resq.TakeawayResq;
import com.pai8.ke.base.BasePresenterImpl;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;

public class TakeawayPresenter extends BasePresenterImpl<TakeawayContract.View> {

    public TakeawayPresenter(TakeawayContract.View view) {
        super(view);
    }
    public void getShopList(String key,int page,String longitude,String latitude){
        ShopListReq shopListReq = new ShopListReq();
        shopListReq.keywords = key;
        shopListReq.page = page+"";
        shopListReq.longitude =longitude;
        shopListReq.latitude = latitude;
        TakeawayApi.getInstance().getShopList(shopListReq)
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<TakeawayResq>() {
                    @Override
                    protected void onSuccess(TakeawayResq shopList) {
                        view.getTakeawaySuccess(shopList);


                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });

    }

}
