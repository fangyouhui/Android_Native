package com.pai8.ke.activity.message.api;


import com.pai8.ke.app.MyApp;
import com.pai8.ke.base.retrofit.BaseApiImpl;

/**
 *
 * @author Created by zzf
 * @time  11:01
 * Description：
 */
public class MessageApi extends BaseApiImpl {

    private static MessageApi api = new MessageApi(MyApp.getHttpBaseUrl());

    public MessageApi(String baseUrl) {

        super(baseUrl);

    }

    public static MessageApiService getInstance() {

        return api.getRetrofit().create(MessageApiService.class);

    }
}
