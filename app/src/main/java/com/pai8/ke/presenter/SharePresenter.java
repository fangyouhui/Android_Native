package com.pai8.ke.presenter;

import com.pai8.ke.api.Api;
import com.pai8.ke.base.BasePresenterImpl;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.entity.resp.ShareMiniResp;
import com.pai8.ke.interfaces.contract.ShareContract;

public class SharePresenter extends BasePresenterImpl<ShareContract.View> implements ShareContract.Presenter {

    public SharePresenter(ShareContract.View view) {
        super(view);
    }

    @Override
    public void shareVideo(String videoId) {
        shareMini(1, videoId);
    }

    @Override
    public void shareShop(String shopId) {
        shareMini(2, shopId);
    }

    @Override
    public void shareGoods(String goodsId) {
        shareMini(3, goodsId);
    }

    private void shareMini(int type, String id) {
        view.showProgress(null);
        Api.getInstance().shareMini(type, id)
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<ShareMiniResp>() {
                    @Override
                    protected void onSuccess(ShareMiniResp shareMiniResp) {
                        view.dismissProgress();
                        if (shareMiniResp == null) {
                            view.toast("分享信息为空");
                            return;
                        }
                        view.shareMini(shareMiniResp);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                        view.dismissProgress();
                    }
                });
    }
}
