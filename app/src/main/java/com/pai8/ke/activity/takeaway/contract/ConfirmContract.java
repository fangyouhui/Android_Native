package com.pai8.ke.activity.takeaway.contract;


import com.pai8.ke.activity.takeaway.entity.resq.WaimaiResq;
import com.pai8.ke.base.BaseView;

/*
 */
public interface ConfirmContract {

    interface View extends BaseView {


        void orderSuccess(String data);


        void waimaiSuccess(WaimaiResq data);

        void onFail(String msg);


    }


}
