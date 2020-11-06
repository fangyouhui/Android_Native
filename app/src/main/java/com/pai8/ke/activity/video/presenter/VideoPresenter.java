package com.pai8.ke.activity.video.presenter;

import com.pai8.ke.activity.video.contract.VideoContract;
import com.pai8.ke.api.Api;
import com.pai8.ke.base.BasePresenterImpl;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.entity.resp.ShareResp;
import com.pai8.ke.entity.resp.VideoResp;
import com.pai8.ke.global.GlobalConstants;
import com.pai8.ke.utils.CollectionUtils;

import java.util.List;

public class VideoPresenter extends BasePresenterImpl<VideoContract.View> implements VideoContract.Presenter {

    public VideoPresenter(VideoContract.View view) {
        super(view);
    }

    @Override
    public void getVideoList(String keywords, int pageNo, int tag) {
        Api.getInstance().videoList(keywords, pageNo)
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<List<VideoResp>>() {
                    @Override
                    protected void onSuccess(List<VideoResp> list) {
                        view.refreshComplete();
                        if (tag == GlobalConstants.REFRESH) {
                            if (CollectionUtils.isEmpty(list)) {
                                view.toast("数据为空");
                                return;
                            }
                            view.getVideoList(list, tag);
                        }
                        if (tag == GlobalConstants.REFRESH) {
                            view.getVideoList(list, tag);
                        }
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        view.refreshComplete();
                        super.onError(msg, errorCode);
                    }
                });
    }

    @Override
    public void follow(String from_user_id, int followStatus) {
        view.showProgress(null);
        if (followStatus == 1) { //取消关注
            Api.getInstance().unfollow(from_user_id)
                    .doOnSubscribe(disposable -> {
                    })
                    .compose(RxSchedulers.io_main())
                    .subscribe(new BaseObserver<Object>() {
                        @Override
                        protected void onSuccess(Object o) {
                            view.dismissProgress();
                            view.follow(0);
                        }

                        @Override
                        protected void onError(String msg, int errorCode) {
                            view.dismissProgress();
                            super.onError(msg, errorCode);
                        }
                    });
            return;
        }
        if (followStatus == 0) { //关注
            Api.getInstance().follow(from_user_id)
                    .doOnSubscribe(disposable -> {
                    })
                    .compose(RxSchedulers.io_main())
                    .subscribe(new BaseObserver<Object>() {
                        @Override
                        protected void onSuccess(Object o) {
                            view.dismissProgress();
                            view.follow(1);
                        }

                        @Override
                        protected void onError(String msg, int errorCode) {
                            view.dismissProgress();
                            super.onError(msg, errorCode);
                        }
                    });
            return;
        }
    }

    @Override
    public void like(String video_id, int likeStatus) {
        view.showProgress(null);
        if (likeStatus == 1) { //取消喜欢
            Api.getInstance().unlike(video_id)
                    .doOnSubscribe(disposable -> {
                    })
                    .compose(RxSchedulers.io_main())
                    .subscribe(new BaseObserver<Object>() {
                        @Override
                        protected void onSuccess(Object o) {
                            view.dismissProgress();
                            view.like(0);
                        }

                        @Override
                        protected void onError(String msg, int errorCode) {
                            view.dismissProgress();
                            super.onError(msg, errorCode);
                        }
                    });
            return;
        }

        if (likeStatus == 0) { //喜欢
            Api.getInstance().unlike(video_id)
                    .doOnSubscribe(disposable -> {
                    })
                    .compose(RxSchedulers.io_main())
                    .subscribe(new BaseObserver<Object>() {
                        @Override
                        protected void onSuccess(Object o) {
                            view.dismissProgress();
                            view.like(1);
                        }

                        @Override
                        protected void onError(String msg, int errorCode) {
                            view.dismissProgress();
                            super.onError(msg, errorCode);
                        }
                    });
            return;
        }
    }

    @Override
    public void share(String platform, String video_id) {
        view.showProgress(null);
        Api.getInstance().shareUrl(video_id)
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<ShareResp>() {
                    @Override
                    protected void onSuccess(ShareResp shareResp) {
                        view.dismissProgress();
                        view.share(platform,shareResp.getUrl());
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        view.dismissProgress();
                        super.onError(msg, errorCode);
                    }
                });
    }

}
