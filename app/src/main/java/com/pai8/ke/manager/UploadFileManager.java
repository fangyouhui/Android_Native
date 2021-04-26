package com.pai8.ke.manager;

import com.pai8.ke.api.Api;
import com.pai8.ke.app.MyApp;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.utils.LogUtils;

import java.io.File;

/**
 * 七牛云上传
 */
public class UploadFileManager {

    private UploadFileManager() {

    }

    public static UploadFileManager getInstance() {
        return UploadFileManagerHolder.mInstance;
    }

    public static class UploadFileManagerHolder {

        public static UploadFileManager mInstance = new UploadFileManager();

    }

    public void upload(String filePath, Callback callback) {
        Api.getInstance().getQNToken()
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String token) {
                        MyApp.getMyApp().getUploadManager().put(filePath, null,
                                token, (key, info, res) -> {
                                    try {
                                        if (info.isOK()) {
                                            String url = res.getString("domain") + res.getString("key");
                                            LogUtils.e("七牛上传成功:URL:" + url + " -key:" + res.getString("key"));
                                            callback.onSuccess(url, res.getString("key"));
                                        }
                                    } catch (Exception e) {
                                        callback.onError("七牛上传失败:" + e.getMessage());
                                    }
                                }, null);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                        callback.onError("获取七牛token失败");
                    }
                });
    }

    public interface Callback {
        void onSuccess(String url, String key);

        void onError(String msg);
    }
}
