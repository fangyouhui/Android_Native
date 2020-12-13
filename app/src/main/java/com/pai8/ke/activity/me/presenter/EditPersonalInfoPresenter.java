package com.pai8.ke.activity.me.presenter;

import com.pai8.ke.activity.me.api.MineApi;
import com.pai8.ke.activity.me.contract.EditPersonalInfoContract;
import com.pai8.ke.activity.me.contract.FansContract;
import com.pai8.ke.activity.me.entity.resp.UserInfoResp;
import com.pai8.ke.activity.message.entity.resp.MessageResp;
import com.pai8.ke.base.BasePresenterImpl;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.manager.UploadFileManager;
import com.pai8.ke.utils.StringUtils;
import com.pai8.ke.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;

/**
 * @author Created by zzf
 * @time 2020/12/12 21:47
 * Description：
 */
public class EditPersonalInfoPresenter extends BasePresenterImpl<EditPersonalInfoContract.View> {

    public EditPersonalInfoPresenter(EditPersonalInfoContract.View view) {
        super(view);
    }

    public void getInfoByUid() {
        HashMap<String, Object> map = new HashMap<>(1);
        map.put("uid", AccountManager.getInstance().getUid());
        MineApi.getInstance().getInfoByUid(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<UserInfoResp>() {
                    @Override
                    protected void onSuccess(UserInfoResp data) {
                        view.getUserInfo(data);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }

    public void submitChangeInfo(String path,String url, String nickname, String wechat) {
        view.showProgress("正在提交保存");
        if(StringUtils.isEmpty(path)){
            saveData(url, nickname, wechat);
        }else {
            UploadFileManager.getInstance().upload(path, new UploadFileManager.Callback() {
                @Override
                public void onSuccess(String url, String key) {
                    saveData(url, nickname, wechat);
                }

                @Override
                public void onError(String msg) {
                    view.dismissProgress();
                    ToastUtils.showShort(msg);
                }
            });
        }
    }

    private void saveData(String url, String nickname, String wechat) {
        HashMap<String, Object> map = new HashMap<>(3);
        map.put("avatar", url);
        map.put("user_nickname", nickname);
        map.put("wechat", wechat);
        MineApi.getInstance().setInfo(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver() {
                    @Override
                    protected void onSuccess(Object data) {
                        view.dismissProgress();
                        view.saveSuccess();
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                        view.dismissProgress();
                    }
                });
    }
}
