package com.pai8.ke.api;


import com.pai8.ke.app.MyApp;
import com.pai8.ke.base.retrofit.BaseApiImpl;

/**
 * 基础网络访问
 * Created by gh on 2020/6/22.
 */
public class Api extends BaseApiImpl {

    private static Api api = new Api(MyApp.getHttpBaseUrl());

    public Api(String baseUrl) {

        super(baseUrl);

    }

    public static ApiService getInstance() {

        return api.getRetrofit().create(ApiService.class);

    }
}
