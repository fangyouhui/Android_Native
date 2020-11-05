package com.pai8.ke.api;

import com.pai8.ke.base.BaseRespose;
import com.pai8.ke.entity.req.CodeReq;
import com.pai8.ke.entity.req.LoginReq;
import com.pai8.ke.entity.resp.Getcode;
import com.pai8.ke.entity.resp.UserInfo;
import com.pai8.ke.entity.resp.VideoEntity;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/*
 * Api接口服务类
 * Created by gh on 2020/6/22.
 */
public interface ApiService {

    /**
     * 获取短信验证码
     */
    @POST("public/getCode")
    Observable<BaseRespose> verifyCode(@Body CodeReq param);

    /**
     * 登录
     */
    @POST("public/login")
    Observable<BaseRespose<UserInfo>> login(@Body LoginReq param);

    /**
     * 手机号登录
     *
     * @param param
     * @return
     */
    @POST("public/mobileLogin")
    Observable<BaseRespose<UserInfo>> mobileLogin(@Body LoginReq param);

    //****************************视频模块********************************

    /**
     * 视频列表
     *
     * @param keywords
     * @param page
     * @return
     */
    @FormUrlEncoded
    @POST("index/videoList")
    Observable<BaseRespose<List<VideoEntity>>> videoList(@Field("keywords") String keywords,
                                                         @Field("page") int page);
}
