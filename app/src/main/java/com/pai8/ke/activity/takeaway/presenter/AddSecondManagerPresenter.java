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
        TakeawayApi.getInstance().getShopLimits()
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<List<SecondAdminManagerResq.PowerArrayBean>>() {
                    @Override
                    protected void onSuccess(List<SecondAdminManagerResq.PowerArrayBean> data){
                        if(data != null){
                            view.getListSuccess(data);
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

    public void updateSecondAdmin(int id, int verifyStatus, String power) {
        HashMap<String, Object> map = new HashMap<>(3);
        map.put("id", id);
        map.put("verify_status", verifyStatus);
        map.put("power", power);
        TakeawayApi.getInstance().updateSecondAdmin(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver() {
                    @Override
                    protected void onSuccess(Object data) {
                        view.updateSuccess();
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }

}
