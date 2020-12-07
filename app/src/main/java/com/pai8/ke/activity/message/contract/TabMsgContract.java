package com.pai8.ke.activity.message.contract;

import com.pai8.ke.activity.message.entity.resp.MessageResp;
import com.pai8.ke.activity.message.entity.resp.MsgCountResp;
import com.pai8.ke.base.BaseView;

import java.util.List;

/**
 * @author Created by zzf
 * @time 2020/12/6 18:24
 * Description：
 */
public interface TabMsgContract {

    interface View extends BaseView {

        void getMsgCountSuccess(MsgCountResp data);

        void showProgress();

        void hideProgress();

    }

}
