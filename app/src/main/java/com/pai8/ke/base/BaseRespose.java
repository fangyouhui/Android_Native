package com.pai8.ke.base;

import java.io.Serializable;

/**
 * 服务器返回数据基类
 * Created by gh on 2018/7/27.
 */
public class BaseRespose<T> implements Serializable {

    private static final long serialVersionUID = 2484868567614623456L;
    public int code;
    public String msg;
    public T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 请求成功
     *
     * @return
     */
    public boolean isSuccess() {
        return 200 == code;
    }

    /**
     * token失效
     *
     * @return
     */
    public boolean isTokenError() {
        return 4003 == code;
    }
}
