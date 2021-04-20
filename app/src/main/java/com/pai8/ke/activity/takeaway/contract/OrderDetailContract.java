package com.pai8.ke.activity.takeaway.contract;


import com.pai8.ke.activity.takeaway.entity.OrderInfo;
import com.pai8.ke.activity.takeaway.entity.OrderListResult;
import com.pai8.ke.base.BaseView;

import java.util.List;

/*
 */
public interface OrderDetailContract {

    interface View extends BaseView {


        void orderDetailSuccess(OrderListResult data);

        void orderCancelSuccess(List<String> data);

        void getStatusSuccess(List<String> data);

    }


}
