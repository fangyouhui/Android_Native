package com.pai8.ke.activity.takeaway.api;

import com.pai8.ke.activity.takeaway.entity.req.MerchantSettledReq;
import com.pai8.ke.base.BaseRespose;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/*
 * Api接口服务类
 */
public interface TakeawayApiService {
    /**
     * 商家申请
     */
    @POST("shop/upShop")
    Observable<BaseRespose> merchantSettled(@Body MerchantSettledReq param);

}
