package com.pai8.ke.activity.takeaway.contract;


import com.pai8.ke.activity.takeaway.entity.resq.StoreInfoResult;
import com.pai8.ke.base.BaseView;

import org.json.JSONObject;

/*
 */
public interface StoreManagerContract {

    interface View extends BaseView {

        void getStoreInfo(StoreInfoResult data);

        void getStatusSuccess(JSONObject data);

    }


}
