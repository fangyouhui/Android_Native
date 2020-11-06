package com.pai8.ke.activity.video.contract;


import com.pai8.ke.base.BasePresenter;
import com.pai8.ke.base.BaseView;
import com.pai8.ke.entity.resp.VideoResp;

import java.util.List;

public interface VideoContract {

    interface View extends BaseView {

        void refreshComplete();

        void getVideoList(List<VideoResp> data, int tag);

        void follow(int followStatus);

        void like(int likeStatus);

        void share(String platform, String url);

    }

    interface Presenter extends BasePresenter {

        void getVideoList(String keywords, int pageNo, int tag);

        void follow(String from_user_id, int followStatus);

        void like(String video_id, int likeStatus);

        void share(String platform, String video_id);

    }
}
