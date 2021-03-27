package com.pai8.ke.groupBuy.http;

import com.google.gson.annotations.SerializedName;
import com.lhs.library.base.IBaseResponse;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class BaseHttpResult<T> implements Serializable, IBaseResponse<T> {

    @SerializedName("code")
    private int code;

    @SerializedName("msg")
    private String msg;

    @SerializedName("result")
    private T data;


    @Override
    public int code() {
        return code;
    }

    @NotNull
    @Override
    public String msg() {
        return msg;
    }

    @Override
    public T data() {
        return data;
    }

    @Override
    public boolean isSuccess() {
        return code == 1;
    }
}
