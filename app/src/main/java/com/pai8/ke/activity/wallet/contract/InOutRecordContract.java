package com.pai8.ke.activity.wallet.contract;

import com.pai8.ke.activity.wallet.data.InOutRecordResp;
import com.pai8.ke.base.BaseView;

public interface InOutRecordContract {
    interface View extends BaseView {

        void isRefresh();

        void completeRefresh();

        void completeLoadMore();

        void getInOutRecordSuccess(InOutRecordResp data);
    }
}
