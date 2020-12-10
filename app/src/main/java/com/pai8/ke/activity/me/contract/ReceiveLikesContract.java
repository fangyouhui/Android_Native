package com.pai8.ke.activity.me.contract;

import com.pai8.ke.activity.message.entity.resp.MessageResp;
import com.pai8.ke.base.BaseView;

import java.util.List;

/**
 * @author Created by zzf
 * @time 2020/12/6 18:24
 * Description：
 */
public interface ReceiveLikesContract {

    interface View extends BaseView {

        void getReceiveLikesSuccess(List<MessageResp> data);

        void isRefresh();

        void completeRefresh();

        void completeLoadMore();
    }

}
