package com.pai8.ke.activity.takeaway.contract;


import com.pai8.ke.activity.takeaway.entity.resq.ShopInfo;
import com.pai8.ke.base.BaseView;

import java.util.List;

/*
 */
public interface TakeawayManagerContract {

    interface View extends BaseView {


        void getCategoryListSuccess(List<ShopInfo> data);


        void deleteCategorySuccess(int position);

        void addUpCategorySuccess(ShopInfo data);


    }


}
