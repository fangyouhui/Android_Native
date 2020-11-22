package com.pai8.ke.activity.takeaway.contract;


import com.pai8.ke.base.BaseView;

/*
 */
public interface AddGoodContract {

    interface View extends BaseView {

        void addGoodSuccess(String data);


        void editGoodSuccess(String data);



        void deleteGoodSuccess(String data);


        void fail();
    }


}
