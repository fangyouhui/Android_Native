package com.pai8.ke.base;

/**
 * BaseEvent
 * Created by gh on 2018/7/27.
 */
public class BaseEvent<T> {

    private int code;

    private T data;

    public BaseEvent(int code) {
        this.code = code;
    }

    public BaseEvent(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
