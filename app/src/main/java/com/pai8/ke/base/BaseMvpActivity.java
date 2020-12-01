package com.pai8.ke.base;


import com.google.gson.Gson;
import com.pai8.ke.utils.ToastUtils;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * MVP模式-BaseMvpActivity
 * Created by gh on 2018/7/27.
 */
public abstract class BaseMvpActivity<P extends BasePresenter> extends BaseActivity implements BaseView {

    protected P mPresenter;

    @Override
    public void initBasePresenter() {
        mPresenter = initPresenter();
    }

    public abstract P initPresenter();

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detach();
            mPresenter = null;
        }
        super.onDestroy();

    }

    @Override
    public void showProgress(String s) {
        showLoadingDialog(s);
    }

    @Override
    public void dismissProgress() {
        dismissLoadingDialog();
    }

    @Override
    public void showLoadingPage() {

    }

    @Override
    public void showEmptyPage() {

    }

    @Override
    public void showErrorPage() {

    }

    @Override
    public void showNetWorkErrorPage() {

    }

    public RequestBody createRequestBody(Map map) {
        Gson gson = new Gson();
        String json = gson.toJson(map);
        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));
        return requestBody;
    }


    @Override
    public void showSucessPage() {

    }

    @Override
    public void toast(String s) {
        ToastUtils.showShort(s);
    }
}
