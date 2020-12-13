package com.pai8.ke.interfaces.contract;


import com.pai8.ke.base.BasePresenter;
import com.pai8.ke.base.BaseView;
import com.pai8.ke.entity.Video;

import java.util.List;

public interface VideoHomeContract {

    interface View extends BaseView {

        void refreshComplete();

        void setNoMore();

        void videoList(List<Video> data, int tag);

        void deleteVideo(String videoId);

    }


    interface Presenter extends BasePresenter {

        void nearby(int page, int tag);

        void flow(int page, int tag);

        void follow(int page, int tag);

        void myVideo(int page, int tag);

        void myLike(int page, int tag);

        void search(String keywords, int page, int tag);

        void deleteVideo(String videoId);

    }
}
