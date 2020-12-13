package com.pai8.ke.interfaces.contract;


import com.pai8.ke.base.BasePresenter;
import com.pai8.ke.base.BaseView;
import com.pai8.ke.entity.Video;

import java.util.List;
import java.util.Map;

public interface VideoDetailContract {

    interface View extends BaseView {

        void refreshComplete();

        void videoList(List<Video> data, int tag);

    }


    interface Presenter extends BasePresenter {

        void videoList(Map<String, Object> fields, int tag);

        void nearbyVideoList(Map<String, Object> fields, int tag);

        void followVideoList(Map<String, Object> fields, int tag);

        void myVideoList(Map<String, Object> fields, int tag);

        void myLikeVideoList(Map<String, Object> fields, int tag);

    }
}
