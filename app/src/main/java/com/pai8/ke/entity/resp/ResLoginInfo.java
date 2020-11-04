package com.pai8.ke.entity.resp;

import java.io.Serializable;

public class ResLoginInfo<T> implements Serializable {
    public int code;
    public String msg;
    public T result;

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

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

}
