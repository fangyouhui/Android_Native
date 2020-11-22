package com.pai8.ke.activity.takeaway.contract;


import com.pai8.ke.activity.takeaway.entity.resq.ShopContent;
import com.pai8.ke.base.BaseView;

/*
 */
public interface StoreContract {

    interface View extends BaseView {


        void getShopContentSuccess(ShopContent data);


    }


}
