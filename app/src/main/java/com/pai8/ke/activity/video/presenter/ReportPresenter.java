package com.pai8.ke.activity.video.presenter;

import com.pai8.ke.activity.video.contract.ReportContract;
import com.pai8.ke.api.Api;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.base.BasePresenterImpl;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.utils.EventBusUtils;
import com.pai8.ke.utils.StringUtils;

import androidx.annotation.IntDef;

import static com.pai8.ke.global.EventCode.EVENT_REPORT;

/*
 * 举报Presenter
 * Created by gh on 2020/11/5.
 */
public class ReportPresenter extends BasePresenterImpl<ReportContract.View> implements ReportContract.Presenter {

    public ReportPresenter(ReportContract.View view) {
        super(view);
    }

    /**
     * @param video_id
     * @param content
     * @param type     1-举报 2-投诉 0-拉黑
     */
    @Override
    public void report(String video_id, String content, int type) {
        if (StringUtils.isEmpty(video_id)) {
            view.toast("视频id不能为空");
            return;
        }
        if (type == 0) {
            content = "拉黑";
        }
        if (StringUtils.isEmpty(content)) {
            switch (type) {
                case 1:
                    view.toast("请输入举报的内容");
                    break;
                case 2:
                    view.toast("请输入投诉的内容");
                    break;
            }
            return;
        }
        view.showProgress(null);
        Api.getInstance().report(video_id, content)
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<Object>() {
                    @Override
                    protected void onSuccess(Object o) {
                        view.dismissProgress();
                        switch (type) {
                            case 1:
                                view.toast("举报成功");
                                break;
                            case 2:
                                view.toast("投诉成功");
                                break;
                            case 0:
                                view.toast("拉黑成功");
                                break;
                        }
                        EventBusUtils.sendEvent(new BaseEvent(EVENT_REPORT, type));
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        view.dismissProgress();
                        super.onError(msg, errorCode);
                    }
                });
    }
}
