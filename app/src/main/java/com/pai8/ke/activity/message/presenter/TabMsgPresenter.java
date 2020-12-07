package com.pai8.ke.activity.message.presenter;

import com.pai8.ke.activity.message.api.MessageApi;
import com.pai8.ke.activity.message.contract.AttentionContract;
import com.pai8.ke.activity.message.contract.TabMsgContract;
import com.pai8.ke.activity.message.entity.resp.MessageResp;
import com.pai8.ke.activity.message.entity.resp.MsgCountResp;
import com.pai8.ke.base.BasePresenterImpl;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.manager.AccountManager;

import java.util.HashMap;
import java.util.List;

/**
 * @author Created by zzf
 * @time 2020/12/6 18:28
 * Description：
 */
public class TabMsgPresenter extends BasePresenterImpl<TabMsgContract.View> {

    public TabMsgPresenter(TabMsgContract.View view) {
        super(view);
    }

    public void reqMessageList(){
        HashMap<String,Object> map = new HashMap<>(1);
        map.put("user_id", AccountManager.getInstance().getUid());
        MessageApi.getInstance().getMsgCount(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<MsgCountResp>() {
                    @Override
                    protected void onSuccess(MsgCountResp data){
                        view.getMsgCountSuccess(data);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }

}
