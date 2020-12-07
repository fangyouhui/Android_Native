package com.pai8.ke.activity.message.contract;

import com.pai8.ke.activity.message.entity.resp.MessageResp;
import com.pai8.ke.base.BaseView;

import java.util.List;

/**
 * @author Created by zzf
 * @time 2020/12/6 18:24
 * Description：
 */
public interface ActiveMessageContract {

    interface View extends BaseView {

        void getActiveMessageSuccess(List<MessageResp> data);

        void isRefresh();

        void completeRefresh();

        void completeLoadMore();
    }

}
