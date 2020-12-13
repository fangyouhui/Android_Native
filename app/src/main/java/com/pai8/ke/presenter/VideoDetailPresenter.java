package com.pai8.ke.presenter;

import com.pai8.ke.api.Api;
import com.pai8.ke.base.BasePresenterImpl;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.entity.resp.VideoNearResp;
import com.pai8.ke.entity.Video;
import com.pai8.ke.global.GlobalConstants;
import com.pai8.ke.interfaces.contract.VideoDetailContract;
import com.pai8.ke.utils.CollectionUtils;

import java.util.List;
import java.util.Map;

public class VideoDetailPresenter extends BasePresenterImpl<VideoDetailContract.View> implements VideoDetailContract.Presenter {

    public VideoDetailPresenter(VideoDetailContract.View view) {
        super(view);
    }

    @Override
    public void videoList(Map<String, Object> fields, int tag) {
        Api.getInstance().videoList(fields)
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<List<Video>>() {
                    @Override
                    protected void onSuccess(List<Video> videos) {
                        view.refreshComplete();
                        if (tag == GlobalConstants.REFRESH) {
                            if (CollectionUtils.isEmpty(videos)) {
                                return;
                            }
                            view.videoList(videos, tag);
                        }
                        if (tag == GlobalConstants.LOADMORE) {
                            view.videoList(videos, tag);
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
    public void nearbyVideoList(Map<String, Object> fields, int tag) {
        Api.getInstance().nearbyVideoList(fields)
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<VideoNearResp>() {
                    @Override
                    protected void onSuccess(VideoNearResp data) {
                        view.refreshComplete();
                        List<Video> videos = data.getVidoe_list();
                        if (tag == GlobalConstants.REFRESH) {
                            if (CollectionUtils.isEmpty(videos)) {
                                return;
                            }
                            view.videoList(videos, tag);
                        }
                        if (tag == GlobalConstants.LOADMORE) {
                            view.videoList(videos, tag);
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
    public void followVideoList(Map<String, Object> fields, int tag) {
        Api.getInstance().followVideoList(fields)
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<List<Video>>() {
                    @Override
                    protected void onSuccess(List<Video> list) {
                        view.refreshComplete();
                        if (tag == GlobalConstants.REFRESH) {
                            if (CollectionUtils.isEmpty(list)) {
                                return;
                            }
                            view.videoList(list, tag);
                        }
                        if (tag == GlobalConstants.LOADMORE) {
                            view.videoList(list, tag);
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
    public void myVideoList(Map<String, Object> fields, int tag) {
        Api.getInstance().myVideoList(fields)
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<List<Video>>() {
                    @Override
                    protected void onSuccess(List<Video> list) {
                        view.refreshComplete();
                        if (tag == GlobalConstants.REFRESH) {
                            if (CollectionUtils.isEmpty(list)) {
                                return;
                            }
                            view.videoList(list, tag);
                        }
                        if (tag == GlobalConstants.LOADMORE) {
                            view.videoList(list, tag);
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
    public void myLikeVideoList(Map<String, Object> fields, int tag) {
        Api.getInstance().myLikeVideoList(fields)
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<List<Video>>() {
                    @Override
                    protected void onSuccess(List<Video> list) {
                        view.refreshComplete();
                        if (tag == GlobalConstants.REFRESH) {
                            if (CollectionUtils.isEmpty(list)) {
                                return;
                            }
                            view.videoList(list, tag);
                        }
                        if (tag == GlobalConstants.LOADMORE) {
                            view.videoList(list, tag);
                        }
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        view.refreshComplete();
                        super.onError(msg, errorCode);
                    }
                });
    }

}
