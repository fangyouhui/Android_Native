package com.pai8.ke.activity.takeaway.contract;


import com.pai8.ke.base.BaseView;

import java.util.List;

/*
 */
public interface AddGoodContract {

    interface View extends BaseView {

        void addGoodSuccess(List<String> data);

        void editGoodSuccess(List<String> data);

        void deleteGoodSuccess(List<String> data);

        void fail();
    }


}
