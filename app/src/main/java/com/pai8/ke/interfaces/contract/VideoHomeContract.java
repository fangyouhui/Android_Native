package com.pai8.ke.interfaces.contract;


import com.pai8.ke.base.BasePresenter;
import com.pai8.ke.base.BaseRespose;
import com.pai8.ke.base.BaseView;
import com.pai8.ke.entity.resp.CommentResp;
import com.pai8.ke.entity.resp.VideoResp;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface VideoHomeContract {

    interface View extends BaseView {

        void refreshComplete();

        void videoList(List<VideoResp> data, int tag);

    }


    interface Presenter extends BasePresenter {

        void videoList(String keywords, int pageNo, int tag);

        void nearbyVideoList(String keywords, int pageNo, int tag);

        void followVideoList(int pageNo, int tag);

        void myVideoList(int pageNo, int tag);

        void myLikeVideoList(int pageNo, int tag);

    }
}
