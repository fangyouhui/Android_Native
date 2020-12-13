package com.pai8.ke.activity.me.contract;

import com.pai8.ke.activity.message.entity.resp.MessageResp;
import com.pai8.ke.base.BaseView;

import java.util.List;

/**
 * @author Created by zzf
 * @time 2020/12/6 18:24
 * Description：
 */
public interface AttentionMineContract {

    interface View extends BaseView {

        void getAttentionMineSuccess(List<MessageResp> data);

        void cancelAttentionSuccess();

        void attentionSuccess();

        void isRefresh();

        void completeRefresh();

        void completeLoadMore();

    }

}
