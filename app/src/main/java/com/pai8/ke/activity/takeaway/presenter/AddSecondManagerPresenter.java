package com.pai8.ke.activity.takeaway.presenter;

import com.pai8.ke.activity.takeaway.api.TakeawayApi;
import com.pai8.ke.activity.takeaway.contract.AddSecondManagerContract;
import com.pai8.ke.activity.takeaway.contract.SecondAdminManagerContract;
import com.pai8.ke.activity.takeaway.entity.resq.SecondAdminManagerResq;
import com.pai8.ke.base.BasePresenterImpl;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.manager.AccountManager;

import java.util.HashMap;
import java.util.List;

public class AddSecondManagerPresenter extends BasePresenterImpl<AddSecondManagerContract.View> {

    public AddSecondManagerPresenter(AddSecondManagerContract.View view) {
        super(view);
    }

    public void getList(){
        HashMap<String, Object> map = new HashMap<>(2);
        map.put("shop_id", AccountManager.getInstance().getShopId());
        map.put("user_id", AccountManager.getInstance().getUid());
        TakeawayApi.getInstance().getShopLimits(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<SecondAdminManagerResq>() {
                    @Override
                    protected void onSuccess(SecondAdminManagerResq data){
                        if(data != null && data.getPower_array() != null){
                            view.getListSuccess(data.getPower_array());
                        }else {
                            onError("数据为空", -1);
                        }
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }

    public void addSecondManager(String phone , String power){
        HashMap<String, Object> map = new HashMap<>(3);
        map.put("shop_id", AccountManager.getInstance().getShopId());
        map.put("mobile", phone);
        map.put("power", power);
        TakeawayApi.getInstance().addSecondAdmin(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver() {
                    @Override
                    protected void onSuccess(Object data){
                        view.addAdminSuccess();
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }

}
