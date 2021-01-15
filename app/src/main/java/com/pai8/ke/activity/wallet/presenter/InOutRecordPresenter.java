package com.pai8.ke.activity.wallet.presenter;

import android.util.Log;

import com.pai8.ke.activity.me.entity.resp.FansResp;
import com.pai8.ke.activity.takeaway.api.TakeawayApi;
import com.pai8.ke.activity.wallet.contract.InOutRecordContract;
import com.pai8.ke.activity.wallet.data.InOutRecordRequest;
import com.pai8.ke.activity.wallet.data.InOutRecordResp;
import com.pai8.ke.base.BasePresenter;
import com.pai8.ke.base.BasePresenterImpl;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.utils.ToastUtils;

public class InOutRecordPresenter extends BasePresenterImpl<InOutRecordContract.View> {
    public InOutRecordPresenter(InOutRecordContract.View view) {
        super(view);
    }

    public void getInOutRecord(String month, int page) {
        InOutRecordRequest request = new InOutRecordRequest();
        request.setMonth(month);
        request.setPage(page);
        request.setPage_count(20);
        TakeawayApi.getInstance().inOutRecord(request)
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<InOutRecordResp>() {
                    @Override
                    protected void onSuccess(InOutRecordResp data){
                        if(page == 1){
                            view.completeRefresh();
                        }else {
                            view.completeLoadMore();
                        }
                        if (data != null && data.getList() != null) {
                            view.getInOutRecordSuccess(data);
                        } else {
                            Log.d("####","数据异常");
                            //ToastUtils.showShort("数据异常");
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
