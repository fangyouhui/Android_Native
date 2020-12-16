package com.pai8.ke.activity.me.presenter;

import com.pai8.ke.activity.me.api.MineApi;
import com.pai8.ke.activity.me.contract.AttentionMineContract;
import com.pai8.ke.activity.me.contract.HistoryWatchContract;
import com.pai8.ke.activity.me.entity.resp.HistoryResp;
import com.pai8.ke.activity.message.api.MessageApi;
import com.pai8.ke.activity.message.entity.resp.MessageResp;
import com.pai8.ke.base.BasePresenterImpl;
import com.pai8.ke.base.BaseRespose;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.entity.Video;
import com.pai8.ke.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;

/**
 * @author Created by zzf
 * @time 2020/12/6 18:28
 * Description：
 */
public class HistoryWatchPresenter extends BasePresenterImpl<HistoryWatchContract.View> {

    public HistoryWatchPresenter(HistoryWatchContract.View view) {
        super(view);
    }

    public void reqMessageList(int page) {
        HashMap<String, Object> map = new HashMap<>(1);
        map.put("page", page);
        MineApi.getInstance().getHistoryList(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<HistoryResp>() {
                    @Override
                    protected void onSuccess(HistoryResp data) {
                        if (page == 1) {
                            view.completeRefresh();
                        } else {
                            view.completeLoadMore();
                        }
                        if (data != null && data.getItems() != null && data.getPagination() != null) {
                            view.getHistorySuccess(data.getPagination().getTotal(), data.getItems());
                        } else {
                            ToastUtils.showShort("数据异常");
                        }
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                        if (page == 1) {
                            view.completeRefresh();
                        } else {
                            view.completeLoadMore();
                        }
                    }
                });
    }

}
