package com.pai8.ke.activity.video.presenter;

import com.pai8.ke.activity.video.contract.VideoContract;
import com.pai8.ke.api.Api;
import com.pai8.ke.base.BasePresenterImpl;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.entity.Video;
import com.pai8.ke.entity.resp.AttentionResp;
import com.pai8.ke.entity.resp.CommentResp;
import com.pai8.ke.entity.resp.VideoResp;
import com.pai8.ke.manager.AccountManager;

import java.util.List;

public class VideoPresenter extends BasePresenterImpl<VideoContract.View> implements VideoContract.Presenter {

    public VideoPresenter(VideoContract.View view) {
        super(view);
    }

    @Override
    public void getComments(Video video) {
        if (video == null) return;
        view.showProgress(null);
        Api.getInstance().comments(video.getId())
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
    public void follow(Video video) {
        if (!AccountManager.getInstance().isLogin()) {
            view.login();
            return;
        }
        if (video == null) return;
        view.showProgress(null);
        Api.getInstance().attention(video.getUser().getId())
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<AttentionResp>() {
                    @Override
                    protected void onSuccess(AttentionResp resp) {
                        view.dismissProgress();
                        video.setFollow_status(resp.getStatus());
                        view.newVideo(video, true);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        view.dismissProgress();
                        super.onError(msg, errorCode);
                    }
                });
    }

    @Override
    public void like(Video video) {
        if (!AccountManager.getInstance().isLogin()) {
            view.login();
            return;
        }
        if (video == null) return;
        view.showProgress(null);
        Api.getInstance().like(video.getId())
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<VideoResp>() {
                    @Override
                    protected void onSuccess(VideoResp videoResp) {
                        view.dismissProgress();
                        Video video = videoResp.getVideo();
                        if (video != null) view.newVideo(video, true);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        view.dismissProgress();
                        super.onError(msg, errorCode);
                    }
                });
    }

    @Override
    public void look(Video video) {
        if (video == null) return;
        Api.getInstance().look(video.getId())
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<VideoResp>() {
                    @Override
                    protected void onSuccess(VideoResp videoResp) {
                        Video video = videoResp.getVideo();
                        if (video != null) view.newVideo(video, false);
                    }
                });
    }

    /**
     * 评论视频
     */
    @Override
    public void commentVideo(Video video, String content) {
        if (!AccountManager.getInstance().isLogin()) {
            view.login();
            return;
        }
        if (video == null) return;
        view.showProgress(null);
        Api.getInstance().comment(video.getId(), "0", content)
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<VideoResp>() {
                    @Override
                    protected void onSuccess(VideoResp videoResp) {
                        view.dismissProgress();
                        view.toast("评论成功");
                        getComments(video);
                        Video video = videoResp.getVideo();
                        if (video != null) view.newVideo(video, true);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        view.dismissProgress();
                        super.onError(msg, errorCode);
                    }
                });
    }

    /**
     * 评论视频
     */
    @Override
    public void comment(Video video, String pid, String content) {
        if (!AccountManager.getInstance().isLogin()) {
            view.login();
            return;
        }
        if (video == null) return;
        view.showProgress(null);
        Api.getInstance().comment(video.getId(), pid, content)
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<VideoResp>() {
                    @Override
                    protected void onSuccess(VideoResp videoResp) {
                        view.dismissProgress();
                        view.toast("评论成功");
                        getComments(video);
                        Video video = videoResp.getVideo();
                        if (video != null) view.newVideo(video, true);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        view.dismissProgress();
                        super.onError(msg, errorCode);
                    }
                });
    }

}
