package com.pai8.ke.activity.takeaway.contract;

import com.pai8.ke.activity.takeaway.entity.resq.GoodsInfoModel;
import com.pai8.ke.activity.takeaway.entity.resq.smallGoodsInfo;
import com.pai8.ke.base.BaseView;

public interface AddGroupGoodContract {
    interface View extends BaseView {

        void addGoodSuccess(smallGoodsInfo data);


        void editGoodSuccess(smallGoodsInfo data);



        void deleteGoodSuccess(smallGoodsInfo data);

        void getGoodSuccess(GoodsInfoModel data);

        void fail();
    }

}
