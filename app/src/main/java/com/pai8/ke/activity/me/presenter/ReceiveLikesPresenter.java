package com.pai8.ke.activity.me.presenter;

import com.pai8.ke.activity.me.api.MineApi;
import com.pai8.ke.activity.me.contract.ReceiveLikesContract;
import com.pai8.ke.activity.me.entity.resp.HistoryResp;
import com.pai8.ke.activity.message.api.MessageApi;
import com.pai8.ke.activity.message.contract.AttentionContract;
import com.pai8.ke.activity.message.contract.LikesContract;
import com.pai8.ke.activity.message.entity.resp.MessageResp;
import com.pai8.ke.activity.takeaway.Constants;
import com.pai8.ke.app.MyApp;
import com.pai8.ke.base.BasePresenterImpl;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.entity.resp.LikeInfo;
import com.pai8.ke.global.GlobalConstants;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.utils.PreferencesUtils;
import com.pai8.ke.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;

/**
 * @author Created by zzf
 * @time 2020/12/6 18:28
 * Description：
 */
public class ReceiveLikesPresenter extends BasePresenterImpl<ReceiveLikesContract.View> {

    public ReceiveLikesPresenter(ReceiveLikesContract.View view) {
        super(view);
    }

    public void reqMessageList(int page){
        HashMap<String,Object> map = new HashMap<>(2);
        map.put("page",page);
        map.put("size", GlobalConstants.PAGE_SIZE);
        MineApi.getInstance().getLikesList(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<HistoryResp<LikeInfo>>() {
                    @Override
                    protected void onSuccess(HistoryResp<LikeInfo> data){
                        if(page == 1){
                            view.completeRefresh();
                        }else {
                            view.completeLoadMore();
                        }
                        if (data != null && data.getItems() != null && data.getPagination() != null) {
                            view.getReceiveLikesSuccess(data.getPagination().getTotal(), data.getItems());
                        } else {
                            ToastUtils.showShort("数据异常");
                        }
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                        if(page == 1){
                            view.completeRefresh();
                        }else {
                            view.completeLoadMore();
                        }
                    }
                });
    }

}
