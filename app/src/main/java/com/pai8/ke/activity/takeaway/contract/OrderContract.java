package com.pai8.ke.activity.takeaway.contract;


import com.pai8.ke.activity.takeaway.entity.OrderInfo;
import com.pai8.ke.base.BaseView;

import java.util.List;

/*
 */
public interface OrderContract {

    interface View extends BaseView {


        void orderListSuccess(List<OrderInfo> data);


    }


}
