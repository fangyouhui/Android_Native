package com.pai8.ke.activity.me.contract;

import com.pai8.ke.activity.me.entity.resp.FansResp;
import com.pai8.ke.activity.message.entity.resp.MessageResp;
import com.pai8.ke.activity.takeaway.entity.resq.TakeawayResq;
import com.pai8.ke.base.BaseView;
import com.pai8.ke.entity.User;

import java.util.List;

/**
 * @author Created by zzf
 * @time 2020/12/6 18:24
 * Description：
 */
public interface FansContract {

    interface View extends BaseView {

        void getFansSuccess(int total,List<User> data);

        void cancelAttentionSuccess();

        void attentionSuccess();

        void isRefresh();

        void completeRefresh();

        void completeLoadMore();
    }

}
