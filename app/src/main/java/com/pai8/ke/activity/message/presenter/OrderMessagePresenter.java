package com.pai8.ke.activity.message.presenter;

import com.pai8.ke.activity.message.api.MessageApi;
import com.pai8.ke.activity.message.contract.OrderMessageContract;
import com.pai8.ke.activity.message.entity.resp.MessageResp;
import com.pai8.ke.app.MyApp;
import com.pai8.ke.base.BasePresenterImpl;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.utils.PreferencesUtils;

import java.util.HashMap;
import java.util.List;

/**
 * @author Created by zzf
 * @time 2020/12/6 18:28
 * Description：
 */
public class OrderMessagePresenter extends BasePresenterImpl<OrderMessageContract.View> {

    public OrderMessagePresenter(OrderMessageContract.View view) {
        super(view);
    }

    public void reqMessageList(int page){
        HashMap<String,Object> map = new HashMap<>(3);
        map.put("user_id", AccountManager.getInstance().getUid());
        map.put("type",2);
        map.put("page",page);
        MessageApi.getInstance().getMsgList(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<List<MessageResp>>() {
                    @Override
                    protected void onSuccess(List<MessageResp> data){
                        if(page == 1){
                            view.completeRefresh();
                        }else {
                            view.completeLoadMore();
                        }
                        view.getOrderMessageSuccess(data);
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
