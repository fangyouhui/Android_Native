package com.pai8.ke.activity.video.contract;


import com.pai8.ke.base.BasePresenter;
import com.pai8.ke.base.BaseView;
import com.pai8.ke.entity.resp.CommentResp;
import com.pai8.ke.entity.resp.VideoResp;

import java.util.List;

public interface VideoContract {

    interface View extends BaseView {

        void refreshComplete();

        void getComments(List<CommentResp> data);

        void follow(int followStatus);

        void like(int likeStatus);

        void shareUrl(String url);

    }

    interface Presenter extends BasePresenter {

        void getComments(String video_id);

        void follow(String to_user_id, int followStatus);

        void like(String video_id, int likeStatus);

        void share(String video_id);

        void comment(String video_id, String content, String to_user_id);

        void comment1(String video_id, String commentId, String content, String to_user_id);

        void comment2(String video_id, String commentId, String commentsId, String content,
                      String to_user_id);
    }
}
