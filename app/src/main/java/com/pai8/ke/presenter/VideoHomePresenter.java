package com.pai8.ke.presenter;

import com.pai8.ke.api.Api;
import com.pai8.ke.base.BasePresenterImpl;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.entity.resp.VideoNearResp;
import com.pai8.ke.entity.resp.VideoResp;
import com.pai8.ke.global.GlobalConstants;
import com.pai8.ke.interfaces.contract.VideoHomeContract;
import com.pai8.ke.utils.CollectionUtils;

import java.util.List;

public class VideoHomePresenter extends BasePresenterImpl<VideoHomeContract.View> implements VideoHomeContract.Presenter {

    public VideoHomePresenter(VideoHomeContract.View view) {
        super(view);
    }

    @Override
    public void videoList(String keywords, int pageNo, int tag) {
        Api.getInstance().videoList(keywords, pageNo)
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
    public void nearbyVideoList(String keywords, int pageNo, int tag) {
        Api.getInstance().nearbyVideoList(keywords, pageNo)
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<VideoNearResp>() {
                    @Override
                    protected void onSuccess(VideoNearResp data) {
                        view.refreshComplete();
                        List<VideoResp> videos = data.getVidoe_list();
                        if (tag == GlobalConstants.REFRESH) {
                            if (CollectionUtils.isEmpty(videos)) {
                                view.toast("数据为空");
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
    public void followVideoList(int pageNo, int tag) {
        Api.getInstance().followVideoList(pageNo)
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
    public void myVideoList(int pageNo, int tag) {
        Api.getInstance().myVideoList(pageNo)
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
    public void myLikeVideoList(int pageNo, int tag) {
        Api.getInstance().myLikeVideoList(pageNo)
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<List<VideoResp>>() {
                    @Override
                    protected void onSuccess(List<VideoResp> list) {
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
