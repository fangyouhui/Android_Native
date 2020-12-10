package com.pai8.ke.activity.me.api;


import com.pai8.ke.activity.message.api.MessageApiService;
import com.pai8.ke.app.MyApp;
import com.pai8.ke.base.retrofit.BaseApiImpl;

/**
 *
 * @author Created by zzf
 * @time  11:01
 * Description：
 */
public class MineApi extends BaseApiImpl {

    private static MineApi api = new MineApi(MyApp.getHttpBaseUrl());

    public MineApi(String baseUrl) {

        super(baseUrl);

    }

    public static MineApiService getInstance() {

        return api.getRetrofit().create(MineApiService.class);

    }
}
