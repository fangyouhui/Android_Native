package com.pai8.ke.activity.takeaway.contract;

import com.pai8.ke.activity.takeaway.entity.resq.smallGoodsInfo;
import com.pai8.ke.base.BaseView;

import java.util.List;

public interface shopGroupManagerContract {
    interface View extends BaseView {


        void getCategoryListSuccess(List<smallGoodsInfo> data);


        void deleteCategorySuccess(int position);

        void addUpCategorySuccess(smallGoodsInfo data);

        void isRefresh();

        void completeRefresh();

        void completeLoadMore();

    }

}
