package com.pai8.ke.activity.takeaway.presenter;

import com.pai8.ke.activity.takeaway.api.TakeawayApi;
import com.pai8.ke.activity.takeaway.contract.EvaluateContract;
import com.pai8.ke.activity.takeaway.entity.resq.CommentInfo;
import com.pai8.ke.base.BasePresenterImpl;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;

import java.util.HashMap;
import java.util.List;

public class EvaluatePresenter extends BasePresenterImpl<EvaluateContract.View> {


    public EvaluatePresenter(EvaluateContract.View view) {
        super(view);
    }



    public void shopComments(String shop_id,int page){
        HashMap<String, Object> map = new HashMap<>();
        map.put("page",page);
        map.put("shop_id",shop_id);
        TakeawayApi.getInstance().shopComments(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<List<CommentInfo>>() {
                    @Override
                    protected void onSuccess(List<CommentInfo> data){
                        view.getCommentSuccess(data);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }

}
