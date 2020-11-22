package com.pai8.ke.activity.takeaway.presenter;

import com.pai8.ke.activity.takeaway.api.TakeawayApi;
import com.pai8.ke.activity.takeaway.contract.TakeawayManagerContract;
import com.pai8.ke.activity.takeaway.entity.req.UpCategoryReq;
import com.pai8.ke.activity.takeaway.entity.resq.ShopInfo;
import com.pai8.ke.base.BasePresenterImpl;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;

public class TakeawayManagerPresenter extends BasePresenterImpl<TakeawayManagerContract.View> {

    public TakeawayManagerPresenter(TakeawayManagerContract.View view) {
        super(view);
    }


    /**
     * 获取分类列表
     */
    public void getCategoryList(int shopId){
        UpCategoryReq upCategoryReq = new UpCategoryReq();
        upCategoryReq.shop_id = shopId+"";
        TakeawayApi.getInstance().getCategoryList(upCategoryReq)
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<List<ShopInfo>>() {
                    @Override
                    protected void onSuccess(List<ShopInfo> data) {
                        view.getCategoryListSuccess(data);

                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });

    }


    public void goodslist(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("shop_id", AccountManager.getInstance().getShopId());
        TakeawayApi.getInstance().goodslist(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<List<ShopInfo>>() {
                    @Override
                    protected void onSuccess(List<ShopInfo> data) {
                        view.getCategoryListSuccess(data);

                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });

    }





    /**
     * 添加类别
     */
    public void addUpCategory(String name,String shopId){
        UpCategoryReq upCategoryReq = new UpCategoryReq();
        upCategoryReq.name = name;
        upCategoryReq.shop_id = shopId;
        TakeawayApi.getInstance().addUpCategory(upCategoryReq)
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<ShopInfo>() {
                    @Override
                    protected void onSuccess(ShopInfo msg) {
                        ToastUtils.showShort("添加成功");
                        msg.name = name;
                        msg.id = Integer.parseInt(msg.cate_id);
                        view.addUpCategorySuccess(msg);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });

    }

    /**
     *删除分类列表
     */
    public void deleteCategory(int categoryId,int position){
        UpCategoryReq upCategoryReq = new UpCategoryReq();
        upCategoryReq.category_id = categoryId+"";
        TakeawayApi.getInstance().deleteCategory(upCategoryReq)
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String data) {
                        view.deleteCategorySuccess(position);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });

    }
}
