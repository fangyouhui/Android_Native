package com.pai8.ke.base;

/**
 * MVP模式-BaseView
 * Created by gh on 2018/7/27.
 */
public interface BaseView {

    void toast(String s);

    void showProgress(String s);

    void dismissProgress();

    /**
     * 显示成功页面
     */
    void showSucessPage();

    /**
     * 显示loading页
     */
    void showLoadingPage();

    /**
     * 显示空数据页面
     */
    void showEmptyPage();

    /**
     * 显示数据错误页面
     */
    void showErrorPage();

    /**
     * 显示网络异常页面
     */
    void showNetWorkErrorPage();


}
