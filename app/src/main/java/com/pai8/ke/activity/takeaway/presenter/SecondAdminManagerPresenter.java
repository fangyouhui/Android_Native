package com.pai8.ke.activity.takeaway.presenter;

import com.pai8.ke.activity.takeaway.api.TakeawayApi;
import com.pai8.ke.activity.takeaway.contract.SecondAdminManagerContract;
import com.pai8.ke.activity.takeaway.entity.resq.SecondAdminManagerResq;
import com.pai8.ke.base.BasePresenterImpl;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.manager.AccountManager;

import java.util.HashMap;
import java.util.List;

public class SecondAdminManagerPresenter extends BasePresenterImpl<SecondAdminManagerContract.View> {


    public SecondAdminManagerPresenter(SecondAdminManagerContract.View view) {
        super(view);
    }


    public void getSecondAdminList(){
        HashMap<String, Object> map = new HashMap<>(1);
        map.put("shop_id", AccountManager.getInstance().getShopId());
        TakeawayApi.getInstance().getSecondAdminList(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<List<SecondAdminManagerResq>>() {
                    @Override
                    protected void onSuccess(List<SecondAdminManagerResq> data){
                        data.remove(null);
                        for(SecondAdminManagerResq d : data) {
                            d.getPower_array().remove(null);
                        }
                        view.getListSuccess(data);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }

    public void deleteSecondAdmin(int id){
        HashMap<String, Object> map = new HashMap<>(1);
        map.put("id", id);
        TakeawayApi.getInstance().deleteSecondAdmin(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver() {
                    @Override
                    protected void onSuccess(Object data){
                        view.deleteSuccess();
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }
}
