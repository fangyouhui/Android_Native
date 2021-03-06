package com.pai8.ke.activity.takeaway.contract;

import com.pai8.ke.activity.takeaway.entity.resq.SecondAdminManagerResq;
import com.pai8.ke.base.BaseView;

import java.util.List;

/**
 * @author Created by zzf
 * @time 2020/12/23 22:10
 * Description：
 */
public interface SecondAdminManagerContract {

    interface View extends BaseView {

        void deleteSuccess();

        /**
         *
         * @param data
         */
        void getListSuccess(List<SecondAdminManagerResq> data);

    }

}
