package com.pai8.ke.activity.takeaway.contract;


import com.pai8.ke.activity.takeaway.entity.resq.StoreInfo;
import com.pai8.ke.base.BaseView;

/*
 */
public interface StoreManagerContract {

    interface View extends BaseView {

        void getStoreInfo(StoreInfo data);

        void getStatusSuccess(String data);

    }


}
