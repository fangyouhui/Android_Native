package com.pai8.ke.groupBuy.http

import com.pai8.ke.entity.*
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface GroupBuyApiService {

    @POST("index/businessType")
    suspend fun businessType(): BaseHttpResult<List<BusinessTypeResult>>

    @POST("Group/GetGroupShopList")
    suspend fun getGroupShopList(@Body param: GetGroupShopListParam): BaseHttpResult<GetGroupShopListResult>


    @POST("Group/GetFoodSortList")
    suspend fun getFoodSortList(): BaseHttpResult<List<GroupBuyTypeResult>>

    @POST("Group/GetGroupGoodsList")
    suspend fun getGroupGoodsList(@Body param: Map<String, String>): BaseHttpResult<List<ShopTypeResult>>


    @POST("Group/GetGroupShopInfo")
    suspend fun getGroupShopInfo(@Query("shop_id") shop_id: String): BaseHttpResult<GroupShopInfoResult>


}