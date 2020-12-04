package com.pai8.ke.interfaces.contract;


import com.pai8.ke.base.BasePresenter;
import com.pai8.ke.base.BaseView;
import com.pai8.ke.entity.resp.VideoResp;

import java.util.List;

public interface VideoDetailContract {

    interface View extends BaseView {

        void refreshComplete();

        void videoList(List<VideoResp> data, int tag);

    }


    interface Presenter extends BasePresenter {

        void videoList(String videoId, String keywords, int pos, int pageNo, int tag);

        void nearbyVideoList(String videoId, String keywords, int pos, int pageNo, int tag);

        void followVideoList(String videoId, int pos, int pageNo, int tag);

        void myVideoList(String videoId, int pos, int pageNo, int tag);

        void myLikeVideoList(String videoId, int pos, int pageNo, int tag);

    }
}
