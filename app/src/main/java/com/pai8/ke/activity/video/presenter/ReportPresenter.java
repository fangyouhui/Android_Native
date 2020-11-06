package com.pai8.ke.activity.video.presenter;

import com.pai8.ke.activity.video.contract.ReportContract;
import com.pai8.ke.api.Api;
import com.pai8.ke.base.BasePresenterImpl;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.utils.StringUtils;

/*
 * 举报Presenter
 * Created by gh on 2020/11/5.
 */
public class ReportPresenter extends BasePresenterImpl<ReportContract.View> implements ReportContract.Presenter {

    public ReportPresenter(ReportContract.View view) {
        super(view);
    }

    @Override
    public void report(String video_id, String content) {
        if (StringUtils.isEmpty(video_id)) {
            view.toast("视频id不能为空");
        }
        if (StringUtils.isEmpty(content)) {
            view.toast("请输入举报的内容");
        }
        view.showProgress(null);
        Api.getInstance().report(video_id, content)
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<Object>() {
                    @Override
                    protected void onSuccess(Object o) {
                        view.dismissProgress();
                        view.toast("举报成功");
                        view.reportSuccess();
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        view.dismissProgress();
                        super.onError(msg, errorCode);
                    }
                });
    }
}
