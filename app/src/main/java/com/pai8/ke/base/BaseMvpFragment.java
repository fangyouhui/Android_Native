package com.pai8.ke.base;

import android.content.Context;

import com.pai8.ke.utils.ToastUtils;


/**
 * MVP模式-BaseMvpFragment
 * Created by gh on 2018/7/27.
 */
public abstract class BaseMvpFragment<P extends BasePresenter> extends BaseFragment implements BaseView {

    protected P mPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPresenter = initPresenter();
    }

    public abstract P initPresenter();

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detach();
        }
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

    @Override
    public void showSucessPage() {

    }

    @Override
    public void toast(String s) {
        ToastUtils.showShort(getActivity(), s);
    }
}
