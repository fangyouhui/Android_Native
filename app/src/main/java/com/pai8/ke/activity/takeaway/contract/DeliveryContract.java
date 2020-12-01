package com.pai8.ke.activity.takeaway.contract;


import com.pai8.ke.activity.takeaway.entity.resq.AddressInfo;
import com.pai8.ke.base.BaseView;

import java.util.List;

/*
 */
public interface DeliveryContract {

    interface View extends BaseView {

        void getAddressSuccess(List<AddressInfo> data);

        void addAddressSuccess(String msg);

        void editAddressSuccess(String msg);

        void deleteAddressSuccess(String msg,int position);

    }


}
