package com.pai8.ke.presenter;

import com.pai8.ke.api.Api;
import com.pai8.ke.base.BasePresenterImpl;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.entity.resp.VideoNearResp;
import com.pai8.ke.entity.resp.VideoResp;
import com.pai8.ke.global.GlobalConstants;
import com.pai8.ke.interfaces.contract.VideoDetailContract;
import com.pai8.ke.interfaces.contract.VideoHomeContract;
import com.pai8.ke.utils.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VideoDetailPresenter extends BasePresenterImpl<VideoDetailContract.View> implements VideoDetailContract.Presenter {

    public VideoDetailPresenter(VideoDetailContract.View view) {
        super(view);
    }

    @Override
    public void videoList(String videoId, String keywords, int pos, int page, int tag) {
        Map<String, Object> fields = new HashMap<>();
        fields.put("videoId", videoId);
        fields.put("keywords", keywords);
        fields.put("page", page);
        fields.put("position", pos);
        Api.getInstance().videoList(fields)
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<List<VideoResp>>() {
                    @Override
                    protected void onSuccess(List<VideoResp> videos) {
                        view.refreshComplete();
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
    public void nearbyVideoList(String videoId, String keywords, int pos, int page, int tag) {
        Map<String, Object> fields = new HashMap<>();
        fields.put("videoId", videoId);
        fields.put("keywords", keywords);
        fields.put("page", page);
        fields.put("position", pos);
        Api.getInstance().nearbyVideoList(fields)
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
    public void followVideoList(String videoId, int pos, int page, int tag) {
        Map<String, Object> fields = new HashMap<>();
        fields.put("videoId", videoId);
        fields.put("page", page);
        fields.put("position", pos);
        Api.getInstance().followVideoList(fields)
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
    public void myVideoList(String videoId, int pos, int page, int tag) {
        Map<String, Object> fields = new HashMap<>();
        fields.put("videoId", videoId);
        fields.put("page", page);
        fields.put("position", pos);
        Api.getInstance().myVideoList(fields)
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
    public void myLikeVideoList(String videoId, int pos, int page, int tag) {
        Map<String, Object> fields = new HashMap<>();
        fields.put("videoId", videoId);
        fields.put("page", page);
        fields.put("position", pos);
        Api.getInstance().myLikeVideoList(fields)
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
