package com.pai8.ke.activity.takeaway.api;

import com.pai8.ke.activity.takeaway.entity.req.CategoryInfo;
import com.pai8.ke.activity.takeaway.entity.req.MerchantSettledReq;
import com.pai8.ke.base.BaseRespose;

import java.util.List;

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


    @POST("shop/upCategory")
    Observable<BaseRespose<List<CategoryInfo>>> getUpCategory();

}
