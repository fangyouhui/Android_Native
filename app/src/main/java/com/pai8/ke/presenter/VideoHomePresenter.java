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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VideoHomePresenter extends BasePresenterImpl<VideoHomeContract.View> implements VideoHomeContract.Presenter {

    public VideoHomePresenter(VideoHomeContract.View view) {
        super(view);
    }

    @Override
    public void videoList(String keywords, int page, int tag) {
        Map<String, Object> fields = new HashMap<>();
        fields.put("keywords", keywords);
        fields.put("page", page);
        Api.getInstance().videoList(fields)
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<List<VideoResp>>() {
                    @Override
                    protected void onSuccess(List<VideoResp> videos) {
                        view.refreshComplete();
                        setPageNo(videos, page);
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
    public void nearbyVideoList(String keywords, int page, int tag) {
        Map<String, Object> fields = new HashMap<>();
        fields.put("keywords", keywords);
        fields.put("page", page);
        Api.getInstance().nearbyVideoList(fields)
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<VideoNearResp>() {
                    @Override
                    protected void onSuccess(VideoNearResp data) {
                        view.refreshComplete();
                        List<VideoResp> videos = data.getVidoe_list();
                        setPageNo(videos, page);
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
    public void followVideoList(int page, int tag) {
        Map<String, Object> fields = new HashMap<>();
        fields.put("page", page);
        Api.getInstance().followVideoList(fields)
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<List<VideoResp>>() {
                    @Override
                    protected void onSuccess(List<VideoResp> list) {
                        view.refreshComplete();
                        setPageNo(list, page);
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
    public void myVideoList(int page, int tag) {
        Map<String, Object> fields = new HashMap<>();
        fields.put("page", page);
        Api.getInstance().myVideoList(fields)
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<List<VideoResp>>() {
                    @Override
                    protected void onSuccess(List<VideoResp> list) {
                        view.refreshComplete();
                        setPageNo(list, page);
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
    public void myLikeVideoList(int page, int tag) {
        Map<String, Object> fields = new HashMap<>();
        fields.put("page", page);
        Api.getInstance().myLikeVideoList(fields)
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<List<VideoResp>>() {
                    @Override
                    protected void onSuccess(List<VideoResp> list) {
                        view.refreshComplete();
                        setPageNo(list, page);
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

    private void setPageNo(List<VideoResp> list, int page) {
        if (CollectionUtils.isNotEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                VideoResp videoResp = list.get(i);
                videoResp.setPageNo(page);
                videoResp.setPosition(i);
            }
        }
    }
}
