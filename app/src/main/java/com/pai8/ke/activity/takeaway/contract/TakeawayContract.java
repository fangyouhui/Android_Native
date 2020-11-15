package com.pai8.ke.activity.takeaway.contract;


import com.pai8.ke.activity.takeaway.entity.resq.TakeawayResq;
import com.pai8.ke.base.BaseView;

/*
 */
public interface TakeawayContract {

    interface View extends BaseView {


        void getTakeawaySuccess(TakeawayResq data);

    }


}
