package com.pai8.ke.activity.takeaway.contract;


import com.pai8.ke.activity.takeaway.entity.OrderInfo;
import com.pai8.ke.base.BaseView;

/*
 */
public interface OrderDetailContract {

    interface View extends BaseView {


        void orderDetailSuccess(OrderInfo data);

        void orderCancelSuccess(String data);

    }


}
