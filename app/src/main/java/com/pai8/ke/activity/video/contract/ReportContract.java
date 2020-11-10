package com.pai8.ke.activity.video.contract;


import com.pai8.ke.base.BasePresenter;
import com.pai8.ke.base.BaseView;

/*
 * 举报Contract
 * Created by gh on 2020/11/5.
 */
public interface ReportContract {

    interface View extends BaseView {

    }

    interface Presenter extends BasePresenter {

        void report(String video_id, String content, int type);
    }
}
