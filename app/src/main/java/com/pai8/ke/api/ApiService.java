package com.pai8.ke.api;

import com.pai8.ke.base.BaseRespose;
import com.pai8.ke.entity.req.CodeReq;
import com.pai8.ke.entity.req.LoginReq;
import com.pai8.ke.entity.resp.Getcode;
import com.pai8.ke.entity.resp.UserInfo;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/*
 * Api接口服务类
 * Created by gh on 2020/6/22.
 */
public interface ApiService {

    // TODO: 2020/11/2 以下获取验证码和登录为例，GET POST基本用法

    /**
     * 获取短信验证码
     */
    @POST("public/getCode")
    Observable<BaseRespose<Getcode>> verifyCode(@Body CodeReq param);

    /**
     * 登录
     */
    @POST("account/login")
    Observable<BaseRespose<UserInfo>> login(@Body LoginReq param);

}
