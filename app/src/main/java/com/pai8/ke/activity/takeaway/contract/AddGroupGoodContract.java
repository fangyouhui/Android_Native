package com.pai8.ke.activity.takeaway.contract;

import com.pai8.ke.activity.takeaway.entity.resq.GoodsInfoModel;
import com.pai8.ke.activity.takeaway.entity.resq.smallGoodsInfo;
import com.pai8.ke.base.BaseView;

public interface AddGroupGoodContract {
    interface View extends BaseView {

        void addGoodSuccess(String data);


        void editGoodSuccess(String data);



        void deleteGoodSuccess(String data);

        void getGoodSuccess(GoodsInfoModel data);

        void fail();
    }

}
