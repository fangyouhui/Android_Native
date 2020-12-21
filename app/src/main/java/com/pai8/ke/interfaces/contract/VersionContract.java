package com.pai8.ke.interfaces.contract;


import com.pai8.ke.base.BasePresenter;
import com.pai8.ke.base.BaseView;
import com.pai8.ke.entity.resp.VersionResp;

/**
 * 版本信息Contract
 * Created by gh on 2020/12/20.
 */
public interface VersionContract {

    interface View extends BaseView {

        void showUpdateDialog(VersionResp data);

    }

    interface Presenter extends BasePresenter {
        void getVersion();
    }
}
