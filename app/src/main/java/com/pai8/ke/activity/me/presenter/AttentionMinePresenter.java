package com.pai8.ke.activity.me.presenter;

import com.pai8.ke.activity.me.api.MineApi;
import com.pai8.ke.activity.me.contract.AttentionMineContract;
import com.pai8.ke.activity.me.entity.resp.AttentionMineResp;
import com.pai8.ke.base.BasePresenterImpl;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.utils.ToastUtils;

import java.util.HashMap;

/**
 * @author Created by zzf
 * @time 2020/12/6 18:28
 * Description：
 */
public class AttentionMinePresenter extends BasePresenterImpl<AttentionMineContract.View> {

    public AttentionMinePresenter(AttentionMineContract.View view) {
        super(view);
    }

    public void reqMessageList(int page){
        HashMap<String,Object> map = new HashMap<>(1);
        map.put("page",page);
        MineApi.getInstance().getAttentionMineList(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<AttentionMineResp>() {
                    @Override
                    protected void onSuccess(AttentionMineResp data){
                        if(page == 1){
                            view.completeRefresh();
                        }else {
                            view.completeLoadMore();
                        }
                        if (data != null && data.getUsers() != null && data.getPagination() != null) {
                            view.getAttentionMineSuccess(data.getPagination().getTotal(), data.getUsers());
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
