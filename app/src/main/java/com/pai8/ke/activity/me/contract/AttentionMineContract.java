package com.pai8.ke.activity.me.contract;

import com.pai8.ke.activity.me.entity.resp.AttentionMineResp;
import com.pai8.ke.base.BaseView;
import com.pai8.ke.entity.User;

import java.util.List;

/**
 * @author Created by zzf
 * @time 2020/12/6 18:24
 * Description：
 */
public interface AttentionMineContract {

    interface View extends BaseView {

        void getAttentionMineSuccess(int total, List<User> data);

        void isRefresh();

        void completeRefresh();

        void completeLoadMore();

    }

}
