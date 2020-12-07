package com.pai8.ke.interfaces.contract;


import com.pai8.ke.base.BasePresenter;
import com.pai8.ke.base.BaseRespose;
import com.pai8.ke.base.BaseView;
import com.pai8.ke.entity.resp.CommentResp;
import com.pai8.ke.entity.resp.VideoListResp;
import com.pai8.ke.entity.resp.VideoResp;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface VideoHomeContract {

    interface View extends BaseView {

        void refreshComplete();

        void setNoMore();

        void videoList(List<VideoResp> data, int tag);

    }


    interface Presenter extends BasePresenter {

        void nearby(int page, int tag);

        void flow(int page, int tag);

        void follow(int page, int tag);

        void myVideo(int page, int tag);

        void myLike(int page, int tag);

        void search(String keywords, int page, int tag);

    }
}
