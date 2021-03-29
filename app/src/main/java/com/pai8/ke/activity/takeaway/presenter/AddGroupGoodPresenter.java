package com.pai8.ke.activity.takeaway.presenter;

import android.util.Log;

import com.pai8.ke.activity.takeaway.api.TakeawayApi;
import com.pai8.ke.activity.takeaway.contract.AddGroupGoodContract;
import com.pai8.ke.activity.takeaway.contract.shopGroupManagerContract;
import com.pai8.ke.activity.takeaway.entity.req.AddFoodReq;
import com.pai8.ke.activity.takeaway.entity.req.GroupFoodReq;
import com.pai8.ke.activity.takeaway.entity.resq.GoodsInfoModel;
import com.pai8.ke.activity.takeaway.entity.resq.smallGoodsInfo;
import com.pai8.ke.base.BasePresenterImpl;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.manager.AccountManager;

import java.util.HashMap;
import java.util.List;

public class AddGroupGoodPresenter extends BasePresenterImpl<AddGroupGoodContract.View> {

    public AddGroupGoodPresenter(AddGroupGoodContract.View view) {
        super(view);
    }

    public void addGood(GroupFoodReq req){

        TakeawayApi.getInstance().addGroupFood(req)
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


    public void editGoods(GroupFoodReq req){
        TakeawayApi.getInstance().editGroupFood(req)
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

    public void getGoods(String food_id){
        HashMap<String,Object> map = new HashMap<>();
        map.put("id",food_id);
        TakeawayApi.getInstance().getGroupFood(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<GoodsInfoModel>() {
                    @Override
                    protected void onSuccess(GoodsInfoModel data){
                        view.getGoodSuccess(data);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                        view.fail();
                    }
                });
    }

    public void groupFoodDelete(String food_id){
        HashMap<String,Object> map = new HashMap<>();
        map.put("goods_id",food_id);

        map.put("status","2");
        TakeawayApi.getInstance().setGroupGoodsStatus(createRequestBody(map))
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

    public static class GroupBuyManagerPresenter extends BasePresenterImpl<shopGroupManagerContract.View> {
        public GroupBuyManagerPresenter(shopGroupManagerContract.View view) {
            super(view);
        }

        public void ShopGroupList(int page){
            HashMap<String, Object> map = new HashMap<>();
            map.put("shop_id", AccountManager.getInstance().getShopId());
            map.put("limit","10");
            map.put("page",page);

            TakeawayApi.getInstance().ShopGroupList(createRequestBody(map))
                    .doOnSubscribe(disposable -> {
                    })
                    .compose(RxSchedulers.io_main())
                    .subscribe(new BaseObserver<List<smallGoodsInfo>>() {
                        @Override
                        protected void onSuccess(List<smallGoodsInfo> data) {
                            if(page == 1){
                                view.completeRefresh();
                            }else {
                                view.completeLoadMore();
                            }
                            if (data != null ) {
                                view.getCategoryListSuccess(data);
                            } else {
                                Log.d("####","数据异常");
                                //ToastUtils.showShort("数据异常");
                            }
                        }

                        @Override
                        protected void onError(String msg, int errorCode) {
                            super.onError(msg, errorCode);
                            if(page == 1){
                                view.completeRefresh();
                            }else {
                                view.completeLoadMore();
                            }
                        }
                    });

        }
    }
}
