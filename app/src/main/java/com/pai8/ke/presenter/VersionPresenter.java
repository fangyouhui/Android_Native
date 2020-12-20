package com.pai8.ke.presenter;


import com.pai8.ke.api.Api;
import com.pai8.ke.base.BasePresenterImpl;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.entity.resp.VersionResp;
import com.pai8.ke.interfaces.contract.VersionContract;

/**
 * 版本信息Presenter
 * Created by gh on 2020/12/20.
 */
public class VersionPresenter extends BasePresenterImpl<VersionContract.View> implements VersionContract
        .Presenter {

    public VersionPresenter(VersionContract.View view) {
        super(view);
    }

    @Override
    public void getVersion() {
        Api.getInstance().checkUpgrade()
                .doOnSubscribe(disposable -> addDisposable(disposable))
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<VersionResp>() {
                    @Override
                    protected void onSuccess(VersionResp data) {
                        view.showUpdateDialog(data);
                    }

                    @Override
                    protected void onError(String errorTip, int errorCode) {
                        super.onError(errorTip, errorCode);
                    }
                });

    }
}