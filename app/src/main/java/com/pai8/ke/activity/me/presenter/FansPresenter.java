package com.pai8.ke.activity.me.presenter;

import com.pai8.ke.activity.me.api.MineApi;
import com.pai8.ke.activity.me.contract.FansContract;
import com.pai8.ke.activity.me.entity.resp.FansResp;
import com.pai8.ke.activity.message.api.MessageApi;
import com.pai8.ke.activity.message.contract.AttentionContract;
import com.pai8.ke.activity.message.entity.resp.MessageResp;
import com.pai8.ke.activity.takeaway.api.TakeawayApi;
import com.pai8.ke.activity.takeaway.entity.OrderInfo;
import com.pai8.ke.app.MyApp;
import com.pai8.ke.base.BasePresenterImpl;
import com.pai8.ke.base.BaseRespose;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
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
public class FansPresenter extends BasePresenterImpl<FansContract.View> {

    public FansPresenter(FansContract.View view) {
        super(view);
    }

    public void reqMessageList(int page){
        HashMap<String,Object> map = new HashMap<>(1);
        map.put("page",page);
        map.put("size", GlobalConstants.PAGE_SIZE);
        MineApi.getInstance().getFansList(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<FansResp>() {
                    @Override
                    protected void onSuccess(FansResp data){
                        if(page == 1){
                            view.completeRefresh();
                        }else {
                            view.completeLoadMore();
                        }
                        if (data != null && data.getUsers() != null && data.getPagination() != null) {
                            view.getFansSuccess(data.getPagination().getTotal(), data.getUsers());
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

    public void cancelAttention(String id) {
        HashMap<String,Object> map = new HashMap<>(1);
        map.put("to_user_id", id );
        MessageApi.getInstance().cancelAttention(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String data){
                        view.cancelAttentionSuccess();
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }

    public void getAttention(String id) {
        HashMap<String,Object> map = new HashMap<>(1);
        map.put("to_user_id", id );
        MessageApi.getInstance().getAttention(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver() {

                    @Override
                    protected void onSuccess(Object o) {
                        view.attentionSuccess();
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }
}
