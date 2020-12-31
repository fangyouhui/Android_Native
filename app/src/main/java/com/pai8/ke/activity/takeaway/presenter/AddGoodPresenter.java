package com.pai8.ke.activity.takeaway.presenter;

import com.pai8.ke.activity.takeaway.api.TakeawayApi;
import com.pai8.ke.activity.takeaway.contract.AddGoodContract;
import com.pai8.ke.activity.takeaway.entity.req.AddFoodReq;
import com.pai8.ke.base.BasePresenterImpl;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;

import java.util.HashMap;

public class AddGoodPresenter extends BasePresenterImpl<AddGoodContract.View> {

    public AddGoodPresenter(AddGoodContract.View view) {
        super(view);
    }


    public void addGood(AddFoodReq req){
        req.key = req.cover_qiniu_key;
        TakeawayApi.getInstance().addFood(req)
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String data){
                        view.addGoodSuccess(data);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                        view.fail();
                    }
                });
    }


    public void editGoods(AddFoodReq req){
        req.key = req.cover_qiniu_key;
        TakeawayApi.getInstance().editGoods(req)
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String data){
                        view.editGoodSuccess(data);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                        view.fail();
                    }
                });
    }


    public void foodDelete(String food_id){
        HashMap<String,Object>  map = new HashMap<>();
        map.put("food_id",food_id);
        TakeawayApi.getInstance().foodDelete(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String data){
                        view.deleteGoodSuccess(data);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                        view.fail();
                    }
                });
    }

}
