package com.pai8.ke.activity.video.presenter;

import com.pai8.ke.activity.video.contract.VideoContract;
import com.pai8.ke.api.Api;
import com.pai8.ke.base.BasePresenterImpl;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.entity.resp.CommentResp;
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
    public void contentList(String video_id, int pageNo, int tag) {
        Api.getInstance().contentList(video_id, pageNo)
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
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
                            view.contentList(list, tag);
                        }
                        if (tag == GlobalConstants.LOADMORE) {
                            view.contentList(list, tag);
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
    public void getComments(String video_id) {
        view.showProgress(null);
        Api.getInstance().comments(video_id)
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<List<CommentResp>>() {
                    @Override
                    protected void onSuccess(List<CommentResp> list) {
                        view.dismissProgress();
                        view.getComments(list);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        view.dismissProgress();
                        super.onError(msg, errorCode);
                    }
                });
    }

    @Override
    public void follow(String to_user_id, int followStatus) {
        view.showProgress(null);
        if (followStatus == 1) { //取消关注
            Api.getInstance().unfollow(to_user_id)
                    .doOnSubscribe(disposable -> {
                        addDisposable(disposable);
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
            Api.getInstance().follow(to_user_id)
                    .doOnSubscribe(disposable -> {
                        addDisposable(disposable);
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
                        addDisposable(disposable);
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
                        addDisposable(disposable);
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
    public void share(String video_id) {
        view.showProgress(null);
        Api.getInstance().shareUrl(video_id)
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<ShareResp>() {
                    @Override
                    protected void onSuccess(ShareResp shareResp) {
                        view.dismissProgress();
                        view.shareUrl(shareResp.getUrl());
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        view.dismissProgress();
                        super.onError(msg, errorCode);
                    }
                });
    }

    /**
     * 评论
     *
     * @param video_id
     * @param top_id     评论顶级id，一级评论top_id为0，二级评论top_id就是所属一级评论的id
     * @param pid        评论的父id，就是你回复那条评论的id。直接评论视频的话，pid为0
     * @param content    评论内容
     * @param to_user_id 被评论的用户id，被回复的用户id
     * @return
     */
    private void comment(String video_id, String top_id, String pid, String content, String to_user_id) {
        view.showProgress(null);
        Api.getInstance().comment(video_id, top_id, pid, content, to_user_id)
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver() {
                    @Override
                    protected void onSuccess(Object o) {
                        view.dismissProgress();
                        view.toast("评论成功");
                        getComments(video_id);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        view.dismissProgress();
                        super.onError(msg, errorCode);
                    }
                });
    }

    @Override
    public void comment(String video_id, String content, String to_user_id) {
        comment(video_id, "0", "0", content, to_user_id);
    }

    @Override
    public void comment1(String video_id, String commentId, String content, String to_user_id) {
        comment(video_id, commentId, commentId, content, to_user_id);
    }

    @Override
    public void comment2(String video_id, String commentId, String commentsId, String content,
                         String to_user_id) {
        comment(video_id, commentId, commentsId, content, to_user_id);
    }

}
