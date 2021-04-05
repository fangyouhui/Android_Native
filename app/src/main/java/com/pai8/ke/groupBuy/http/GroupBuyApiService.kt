package com.pai8.ke.groupBuy.http

import com.pai8.ke.activity.me.entity.resp.ShopCouponListResult
import com.pai8.ke.activity.takeaway.entity.OrderDetailResult
import com.pai8.ke.activity.takeaway.entity.OrderListResult
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

    /**
     * 店铺团购列表
     */
    @POST("Group/GetShopGroupList")
    suspend fun getShopGroupList(@Query("shop_id") shop_id: String, @Query("page") page: String,
                                 @Query("size") size: String): BaseHttpResult<List<ShopGroupListResult>>

    /**
     * 店铺视频列表
     */
    @POST("Group/GetShopVideoList")
    suspend fun getShopVideoList(@Query("shop_id") shop_id: String, @Query("page") page: String,
                                 @Query("size") size: String): BaseHttpResult<List<ShopVideoResult>>

    @POST("Order/addOrder")
    suspend fun addOrder(@Body param: AddOrderParam): BaseHttpResult<String>


    @POST("Group/GetGroupGoodsInfo")
    suspend fun getGroupGoodsInfo(@Query("id") productId: String): BaseHttpResult<GroupGoodsInfoResult>


    @POST("Order/orderPrepay")
    suspend fun orderPrepayWithWx(@Query("order_no") order_no: String, @Query("buyer_id") buyer_id: String,
                                  @Query("pay_type") pay_type: Int): BaseHttpResult<OrderPrepayResult>

    @POST("Order/orderPrepay")
    suspend fun orderPrepayWithAli(@Query("order_no") order_no: String, @Query("buyer_id") buyer_id: String,
                                   @Query("pay_type") pay_type: Int): BaseHttpResult<String>


    @POST("Order/orderList")
    suspend fun orderList(@Query("buyer_id") buyer_id: Int, @Query("order_type") order_type: Int): BaseHttpResult<List<OrderListResult>>


    @POST("Order/orderDetail")
    suspend fun orderDetail(@Query("order_no") order_no: String): BaseHttpResult<OrderDetailResult>

    @POST("Order/cancelOrder")
    suspend fun cancelOrder(@Query("order_no") order_no: String, @Query("buyer_id") buyer_id: String): BaseHttpResult<String>


    @POST("shop/upComment")
    suspend fun upComment(@Body param: UpCommentParam): BaseHttpResult<List<String>>


    @POST("Coupon/shopCouponList")
    suspend fun shopCouponList(@Query("shop_id") shop_id: String, @Query("buyer_id") buyer_id: String): BaseHttpResult<ShopCouponListResult>


    /**
     * 领取
     *
     * @return
     */
    @POST("Coupon/getCoupon")
    suspend fun getCoupon(@Query("buyer_id") buyer_id: String, @Query("coupon_ids") coupon_ids: String): BaseHttpResult<List<String>>


    /**
     * 领取
     *
     * @return
     */
    @POST("Order/verifyOrder")
    suspend fun verifyOrder(@Query("order_no") order_no: String, @Query("shop_id") shop_id: String): BaseHttpResult<String>


}