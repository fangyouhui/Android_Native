package com.pai8.ke.activity.takeaway.presenter;

import com.pai8.ke.activity.takeaway.api.TakeawayApi;
import com.pai8.ke.activity.takeaway.contract.StoreManagerContract;
import com.pai8.ke.activity.takeaway.entity.resq.StoreInfoResult;
import com.pai8.ke.base.BasePresenterImpl;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.manager.AccountManager;

import org.json.JSONObject;

import java.util.HashMap;

public class StoreManagerPresenter extends BasePresenterImpl<StoreManagerContract.View> {

    public StoreManagerPresenter(StoreManagerContract.View view) {
        super(view);
    }


    public void shopEditInfo() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("shop_id", AccountManager.getInstance().getShopId());
        map.put("user_id", AccountManager.getInstance().getUid());
        TakeawayApi.getInstance().shopEditInfo(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<StoreInfoResult>() {
                    @Override
                    protected void onSuccess(StoreInfoResult data) {
                        view.getStoreInfo(data);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }

    public void shopIndex() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("shop_id", AccountManager.getInstance().getShopId());
        TakeawayApi.getInstance().shopIndex(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<StoreInfoResult>() {
                    @Override
                    protected void onSuccess(StoreInfoResult data) {
                        view.getStoreInfo(data);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }

    public void shopStatus(String shop_status) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("shop_id", AccountManager.getInstance().getShopId());
        map.put("shop_status", shop_status);
        TakeawayApi.getInstance().shopStatus(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<JSONObject>() {
                    @Override
                    protected void onSuccess(JSONObject data) {
                        view.getStatusSuccess(data);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }

}
