package com.pai8.ke.groupBuy.http

import com.google.gson.JsonObject
import com.pai8.ke.activity.me.entity.resp.ShopCouponListResult
import com.pai8.ke.activity.message.entity.GoodsCollectionResult
import com.pai8.ke.activity.message.entity.resp.MsgCountResp
import com.pai8.ke.activity.takeaway.entity.OrderDetailResult
import com.pai8.ke.activity.takeaway.entity.OrderInfo
import com.pai8.ke.activity.takeaway.entity.OrderListResult
import com.pai8.ke.activity.takeaway.entity.req.AddFoodReq
import com.pai8.ke.activity.takeaway.entity.req.StoreInfoParam
import com.pai8.ke.activity.takeaway.entity.resq.ShopInfo
import com.pai8.ke.activity.takeaway.entity.resq.StoreInfoResult
import com.pai8.ke.entity.*
import com.pai8.ke.entity.req.VideoPublishReq
import com.pai8.ke.entity.resp.*
import org.json.JSONObject
import retrofit2.http.*

interface GroupBuyApiService {


    @POST("system/checkUpgrade")
    suspend fun checkUpgrade(): BaseHttpResult<VersionResp>

    /**
     * 获取个人中心
     *
     * @return
     */
    @POST("user/ucenter")
    suspend fun ucenter(): BaseHttpResult<MyInfoResp>


    @POST("public/getCode")
    suspend fun getCode(@Query("mobile") mobile: String): BaseHttpResult<JSONObject>


    @POST("public/register")
    suspend fun register(@Query("mobile") mobile: String, @Query("code") code: String,
                         @Query("pwd") pwd: String, @Query("repwd") repwd: String): BaseHttpResult<JSONObject>


    /**
     * 根据id获取用户信息
     *
     * @param uid
     * @return
     */
    @POST("user/getInfoByUid")
    suspend fun getUserInfoById(@Query("uid") uid: String): BaseHttpResult<UserInfo>

    /**
     * 分类列表，用于在拍视频、商家申请入驻选择分类的时候选择填写的
     */
    @POST("index/businessType")
    suspend fun businessType(): BaseHttpResult<List<BusinessTypeResult>>

    /**
     * 分类列表，用于在拍视频、商家申请入驻选择分类的时候选择填写的
     */
    @POST("index/setvideotype")
    suspend fun setvideotype(): BaseHttpResult<List<BusinessTypeResult>>

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
                                 @Query("size") size: String): BaseHttpResult<List<Video>>

    @POST("index/GetBannerList")
    suspend fun getBannerList(): BaseHttpResult<List<BannerResult>>


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
    suspend fun getCoupon(@Query("buyer_id") buyer_id: String, @Query("coupon_ids") coupon_ids: String): BaseHttpResult<JSONObject>


    /**
     * 领取
     *
     * @return
     */
    @POST("Order/verifyOrder")
    suspend fun verifyOrder(@Query("order_no") order_no: String, @Query("shop_id") shop_id: String): BaseHttpResult<String>

    /**
     * 商家关注
     */
    @POST("shop/shopCollect")
    suspend fun shopCollect(@Query("shop_id") shop_id: String): BaseHttpResult<String>


    /**
     * 商家取消关注
     */
    @POST("shop/shopUncollect")
    suspend fun shopUncollect(@Query("shop_id") shop_id: String): BaseHttpResult<String>

    /**
     * 获取用户是否关注商家信息
     */
    @POST("shop/IsUserFollow")
    suspend fun isUserFollow(@Query("shop_id") shop_id: String, @Query("user_id") user_id: String): BaseHttpResult<JSONObject>

    /**
     * 商品收藏
     */
    @POST("Group/AddGoodsCollection")
    suspend fun AddGoodsCollection(@Query("goods_id") goods_id: String): BaseHttpResult<JsonObject>


    /**
     * 商品取消收藏
     */
    @POST("Group/SetGoodsUncollect")
    suspend fun SetGoodsUncollect(@Query("goods_id") goods_id: String): BaseHttpResult<JsonObject>

    /**
     * 获取该用户是否收藏该商品
     * @param param
     * @return
     */
    @POST("Group/GetGoodsCollection")
    suspend fun getGoodsCollection(@Query("goods_id") goods_id: String): BaseHttpResult<GoodsCollectionResult>


    /**
     * 消息数量
     * @param param
     * @return
     */
    @POST("Msg/msgIndex")
    suspend fun msgIndex(@Query("user_id") user_id: String): BaseHttpResult<MsgCountResp>


    @POST("system/share")
    suspend fun shareMini(@Query("type") type: Int, @Query("id") id: String): BaseHttpResult<ShareMiniResp>


    @POST("shop/shopEditInfo")
    suspend fun shopEditInfo(@Query("shop_id") shop_id: String, @Query("user_id") user_id: String): BaseHttpResult<StoreInfoResult>


    //***************************视频模块End*******************************
    @POST("public/area")
    suspend fun getArea(): BaseHttpResult<List<Province>>


    @POST("shop/editShop")
    suspend fun editShop(@Body body: StoreInfoParam): BaseHttpResult<JSONObject>


    /**
     * 外卖商品类别列表
     *
     * @param parm
     * @return
     */
    @POST("shop/categoryList")
    suspend fun categoryList(@Query("shop_id") shop_id: String): BaseHttpResult<List<ShopInfo>>


    /**
     * 商家列表
     */

    @POST("shop/shopSelect")
    suspend fun shopSelect(@Query("page") page: Int,
                           @Query("keywords") keywords: String): BaseHttpResult<List<ShopList>>


    /**
     * 附近的视频列表（新）
     *
     * @return
     */
    @POST("video/nearby")
    suspend fun nearby(@Query("page") page: Int, @Query("size") size: Int): BaseHttpResult<VideoListResp>

    /**
     * 推荐的视频列表（新）
     *
     * @return
     */
    @POST("video/flow")
    suspend fun flow(@Query("page") page: Int, @Query("size") size: Int): BaseHttpResult<VideoListResp>

    /**
     * 关注的视频列表（新）
     *
     * @return
     */
    @POST("video/follow")
    suspend fun follow(@Query("page") page: Int, @Query("size") size: Int): BaseHttpResult<VideoListResp>


    /**
     * 我发布的视频列表（新）
     *
     * @return
     */
    @POST("video/myVideo")
    suspend fun myVideo(@Query("page") page: Int, @Query("size") size: Int): BaseHttpResult<VideoListResp>


    /**
     * 我喜欢的视频列表（新）
     *
     * @return
     */
    @POST("video/myLike")
    suspend fun myLike(@Query("page") page: Int, @Query("size") size: Int): BaseHttpResult<VideoListResp>


    /**
     * 关联我的或我关联的视频列表
     *
     * @return
     */
    @POST("video/mylink")
    suspend fun myLink(@Query("page") page: Int, @Query("size") size: Int): BaseHttpResult<VideoListResp>


    /**
     * 编辑外卖商品
     */
    @POST("shop/editGoods")
    suspend fun editGoods(@Body param: AddFoodReq): BaseHttpResult<List<String>>

    /**
     * 下架外卖商品
     */
    @POST("shop/foodDelete")
    suspend fun foodDelete(@Query("food_id") food_id: String): BaseHttpResult<List<String>>

    /**
     * order_status:获取所有订单状态需要传空字符串，*该值为str型，不同的状态请用半角/符号隔开*订单状态 0为待支付 1为已支付 2为商家已接单 7为订单制作完成 3为配送中 4为订单已完成 5为订单已申请退款 6订单被拒绝退款 8为订单已退款 9为订单已取消 -1为支付超时 -2订单拒绝接单
     * order_type: 1为邮寄订单 2为外卖订单 3为团购核销订单 不填写默认为外卖订单
     */
    @POST("Order/shopOrderList")
    suspend fun shopOrderList(@Query("shop_id") shop_id: String, @Query("order_status") order_status: String,
                              @Query("order_type") order_type: Int, @Query("page") page: Int): BaseHttpResult<List<OrderInfo>>

    @POST("Order/shopDealOrder")
    suspend fun shopDealOrder(@Query("shop_id") shop_id: String, @Query("order_no") order_no: String, @Query("type") type: Int): BaseHttpResult<List<String>>


    @POST("Order/applyRefund")
    suspend fun applyRefund(@Query("buyer_id") buyer_id: String, @Query("order_no") order_no: String, @Query("reason") reason: String): BaseHttpResult<List<String>>


    /**
     * 上传视频
     *
     * @param req
     * @return
     */
    @POST("index/upVideo")
    suspend fun upVideo(@Body req: VideoPublishReq): BaseHttpResult<Object>

}