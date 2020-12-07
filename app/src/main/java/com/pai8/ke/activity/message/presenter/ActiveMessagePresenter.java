package com.pai8.ke.activity.message.presenter;

import com.pai8.ke.activity.message.api.MessageApi;
import com.pai8.ke.activity.message.contract.ActiveMessageContract;
import com.pai8.ke.activity.message.entity.resp.MessageResp;
import com.pai8.ke.app.MyApp;
import com.pai8.ke.base.BasePresenterImpl;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.utils.PreferencesUtils;

import java.util.HashMap;
import java.util.List;

/**
 * @author Created by zzf
 * @time 2020/12/6 18:28
 * Description：
 */
public class ActiveMessagePresenter extends BasePresenterImpl<ActiveMessageContract.View> {

    public ActiveMessagePresenter(ActiveMessageContract.View view) {
        super(view);
    }

    public void reqMessageList(int page){
        HashMap<String,Object> map = new HashMap<>(3);
        map.put("user_id", PreferencesUtils.get(MyApp.getMyApp(),"id",""));
        map.put("type",6);
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
                        view.getActiveMessageSuccess(data);
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
