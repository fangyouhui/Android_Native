package com.pai8.ke.presenter;

import com.pai8.ke.api.Api;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.base.BasePresenterImpl;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.entity.resp.VideoListResp;
import com.pai8.ke.entity.Video;
import com.pai8.ke.global.GlobalConstants;
import com.pai8.ke.interfaces.contract.VideoHomeContract;
import com.pai8.ke.utils.CollectionUtils;
import com.pai8.ke.utils.EventBusUtils;

import java.util.List;

import static com.pai8.ke.global.EventCode.EVENT_VIDEO_LIST_REFRESH;

public class VideoHomePresenter extends BasePresenterImpl<VideoHomeContract.View> implements VideoHomeContract.Presenter {

    public VideoHomePresenter(VideoHomeContract.View view) {
        super(view);
    }

    private void setPageNo(List<Video> videos, int page) {
        if (CollectionUtils.isNotEmpty(videos)) {
            for (Video video : videos) {
                video.setPage(page);
            }
        }
    }

    @Override
    public void nearby(int page, int tag) {
        Api.getInstance().nearby(page, GlobalConstants.PAGE_SIZE)
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<VideoListResp>() {
                    @Override
                    protected void onSuccess(VideoListResp data) {
                        view.refreshComplete();
                        List<Video> videos = data.getItems();
                        setPageNo(videos, page);
                        if (tag == GlobalConstants.REFRESH) {
                            view.videoList(videos, tag);
                        }
                        if (tag == GlobalConstants.LOADMORE) {
                            view.videoList(videos, tag);
                        }
                        if (data.getPagination().noMore()) {
                            view.setNoMore();
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
    public void flow(int page, int tag) {
        Api.getInstance().flow(page, GlobalConstants.PAGE_SIZE)
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<VideoListResp>() {
                    @Override
                    protected void onSuccess(VideoListResp data) {
                        view.refreshComplete();
                        List<Video> videos = data.getItems();
                        setPageNo(videos, page);
                        if (tag == GlobalConstants.REFRESH) {
                            view.videoList(videos, tag);
                        }
                        if (tag == GlobalConstants.LOADMORE) {
                            view.videoList(videos, tag);
                        }
                        if (data.getPagination().noMore()) {
                            view.setNoMore();
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
    public void follow(int page, int tag) {
        Api.getInstance().follow(page, GlobalConstants.PAGE_SIZE)
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<VideoListResp>() {
                    @Override
                    protected void onSuccess(VideoListResp data) {
                        view.refreshComplete();
                        List<Video> videos = data.getItems();
                        setPageNo(videos, page);
                        if (tag == GlobalConstants.REFRESH) {
                            view.videoList(videos, tag);
                        }
                        if (tag == GlobalConstants.LOADMORE) {
                            view.videoList(videos, tag);
                        }
                        if (data.getPagination().noMore()) {
                            view.setNoMore();
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
    public void myVideo(int page, int tag) {
        Api.getInstance().myVideo(page, GlobalConstants.PAGE_SIZE)
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<VideoListResp>() {
                    @Override
                    protected void onSuccess(VideoListResp data) {
                        view.refreshComplete();
                        List<Video> videos = data.getItems();
                        setPageNo(videos, page);
                        if (tag == GlobalConstants.REFRESH) {
                            view.videoList(videos, tag);
                        }
                        if (tag == GlobalConstants.LOADMORE) {
                            view.videoList(videos, tag);
                        }
                        if (data.getPagination().noMore()) {
                            view.setNoMore();
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
    public void myLike(int page, int tag) {
        Api.getInstance().myLike(page, GlobalConstants.PAGE_SIZE)
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<VideoListResp>() {
                    @Override
                    protected void onSuccess(VideoListResp data) {
                        view.refreshComplete();
                        List<Video> videos = data.getItems();
                        setPageNo(videos, page);
                        if (tag == GlobalConstants.REFRESH) {
                            view.videoList(videos, tag);
                        }
                        if (tag == GlobalConstants.LOADMORE) {
                            view.videoList(videos, tag);
                        }
                        if (data.getPagination().noMore()) {
                            view.setNoMore();
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
    public void search(String keywords, int page, int tag) {
        Api.getInstance().search(keywords, page, GlobalConstants.PAGE_SIZE)
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<VideoListResp>() {
                    @Override
                    protected void onSuccess(VideoListResp data) {
                        view.refreshComplete();
                        if (data == null) {

                        }
                        List<Video> videos = data.getItems();
                        setPageNo(videos, page);
                        if (tag == GlobalConstants.REFRESH) {
                            view.videoList(videos, tag);
                        }
                        if (tag == GlobalConstants.LOADMORE) {
                            view.videoList(videos, tag);
                        }
                        if (data.getPagination().noMore()) {
                            view.setNoMore();
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
    public void deleteVideo(String videoId) {
        view.showProgress(null);
        Api.getInstance().deleteVideo(videoId)
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver() {
                    @Override
                    protected void onSuccess(Object data) {
                        view.dismissProgress();
                        view.deleteVideo(videoId);
                        view.toast("删除成功");
                        EventBusUtils.sendEvent(new BaseEvent(EVENT_VIDEO_LIST_REFRESH));
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        view.dismissProgress();
                        super.onError(msg, errorCode);
                    }
                });
    }
}
