package com.pai8.ke.activity.takeaway.contract;


import com.pai8.ke.activity.takeaway.entity.ShopFoodGoodInfo;
import com.pai8.ke.activity.takeaway.entity.resq.ShopContent;
import com.pai8.ke.base.BaseView;

/*
 */
public interface StoreContract {

    interface View extends BaseView {


        void getShopContentSuccess(ShopContent data);


        void collectionSuccess(String data);

        void unCollectionSuccess(String data);


        void getCarSuccess(ShopFoodGoodInfo data);
    }


}
