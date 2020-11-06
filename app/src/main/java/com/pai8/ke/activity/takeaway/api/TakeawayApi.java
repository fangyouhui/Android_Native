package com.pai8.ke.activity.takeaway.api;


import com.pai8.ke.app.MyApp;
import com.pai8.ke.base.retrofit.BaseApiImpl;

/**
 * 基础网络访问
 */
public class TakeawayApi extends BaseApiImpl {

    private static TakeawayApi api = new TakeawayApi(MyApp.getHttpBaseUrl());

    public TakeawayApi(String baseUrl) {

        super(baseUrl);

    }

    public static TakeawayApiService getInstance() {

        return api.getRetrofit().create(TakeawayApiService.class);

    }
}
