package com.pai8.ke.activity.video.contract;


import com.pai8.ke.base.BasePresenter;
import com.pai8.ke.base.BaseView;
import com.pai8.ke.entity.Video;
import com.pai8.ke.entity.resp.CommentResp;

import java.util.List;

public interface VideoContract {

    interface View extends BaseView {

        void login();
        
        void getComments(List<CommentResp> data);

        void newVideo(Video video, boolean refreshPage);

    }

    interface Presenter extends BasePresenter {

        void getComments(Video video);

        void follow(Video video);

        void like(Video video);

        void look(Video video);

        void commentVideo(Video video, String content);

        void comment(Video video, String pId, String content);

    }
}
