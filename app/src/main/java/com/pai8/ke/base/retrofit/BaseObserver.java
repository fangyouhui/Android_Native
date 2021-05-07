package com.pai8.ke.base.retrofit;

import android.net.ParseException;
import android.util.Log;

import com.google.gson.JsonParseException;
import com.pai8.ke.R;
import com.pai8.ke.base.BaseRespose;
import com.pai8.ke.exception.NetConnException;
import com.pai8.ke.global.GlobalConstants;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.utils.ResUtils;
import com.pai8.ke.utils.ToastUtils;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 观察者-拦截
 *
 * @param <T> Created by gh on 2018/7/27.
 */
public abstract class BaseObserver<T> implements Observer<BaseRespose<T>> {

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(BaseRespose<T> value) {
        Log.d("####", "onNext");
        if (value.isSuccess()) {
            //请求成功
            onSuccess(value.getData());
        } else if (value.isTokenError()) {
            ToastUtils.showShort(value.getMsg());
            AccountManager.getInstance().logout();
        } else {
            onError(value.getMsg(), value.getCode());
        }

    }

    @Override
    public void onError(Throwable e) {

        e.printStackTrace();

        if (e instanceof SocketTimeoutException) {
            //请求超时
            Log.d("####", ResUtils.getText(R.string.str_conn_timeout));
            onError(ResUtils.getText(R.string.str_conn_timeout), GlobalConstants.HTTP_CONN_TIMEOUT);
        } else if (e instanceof NetConnException) {
            //网络未连接/网络不可用
            Log.d("####", e.getMessage());
            onError(e.getMessage(), GlobalConstants.HTTP_NET_ERROR);
        } else if (e instanceof UnknownHostException || e instanceof ConnectException) {
            //连接异常
            Log.d("####", ResUtils.getText(R.string.str_conn_error));
            onError(ResUtils.getText(R.string.str_conn_error), GlobalConstants.HTTP_CONN_ERROR);
        } else if (e instanceof JsonParseException || e instanceof JSONException || e instanceof
                ParseException) {
            //数据解析异常
            Log.d("####", ResUtils.getText(R.string.str_data_parse_error));
            onError(ResUtils.getText(R.string.str_data_parse_error), GlobalConstants.HTTP_DATA_PARSE_ERROR);
        } else {
            //其他均视为连接异常
            Log.d("####", ResUtils.getText(R.string.str_conn_error));
            onError(ResUtils.getText(R.string.str_conn_error), GlobalConstants.HTTP_CONN_ERROR);
        }

    }

    @Override
    public void onComplete() {

    }

    protected abstract void onSuccess(T t);

    protected void onError(String errorTip, int errorCode) {
        ToastUtils.showShort(errorTip);
    }
}


