package com.pai8.ke.activity.takeaway.contract;


import com.pai8.ke.activity.takeaway.entity.ShopFoodGoodInfo;
import com.pai8.ke.activity.takeaway.entity.resq.ShopContent;
import com.pai8.ke.base.BaseView;

import org.json.JSONObject;

/*
 */
public interface StoreContract {

    interface View extends BaseView {


        void getShopContentSuccess(ShopContent data);


        void collectionSuccess(JSONObject data);

        void unCollectionSuccess(String data);


        void getCarSuccess(ShopFoodGoodInfo data);

        void onFail(String msg);
    }


}
