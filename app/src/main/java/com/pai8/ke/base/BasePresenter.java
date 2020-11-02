package com.pai8.ke.base;

import io.reactivex.disposables.Disposable;

/**
 * MVP模式-BasePresent
 * Created by gh on 2018/7/27.
 */
public interface BasePresenter {

    void start();

    void detach();

    void addDisposable(Disposable subscription);

    void unDisposable();

}
