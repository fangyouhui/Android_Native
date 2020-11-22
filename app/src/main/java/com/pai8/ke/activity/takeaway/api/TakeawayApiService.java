package com.pai8.ke.activity.takeaway.api;

import com.pai8.ke.activity.takeaway.entity.OrderInfo;
import com.pai8.ke.activity.takeaway.entity.req.AddFoodReq;
import com.pai8.ke.activity.takeaway.entity.req.MerchantSettledReq;
import com.pai8.ke.activity.takeaway.entity.req.ShopIdReq;
import com.pai8.ke.activity.takeaway.entity.req.ShopListReq;
import com.pai8.ke.activity.takeaway.entity.req.UpCategoryReq;
import com.pai8.ke.activity.takeaway.entity.resq.AddressInfo;
import com.pai8.ke.activity.takeaway.entity.resq.CommentInfo;
import com.pai8.ke.activity.takeaway.entity.resq.ShopContent;
import com.pai8.ke.activity.takeaway.entity.resq.ShopInfo;
import com.pai8.ke.activity.takeaway.entity.resq.TakeawayResq;
import com.pai8.ke.activity.takeaway.entity.resq.WaimaiResq;
import com.pai8.ke.base.BaseRespose;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
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


    /**
     * 添加分类
     * @return
     */
    @POST("shop/upCategory")
    Observable<BaseRespose<ShopInfo>> addUpCategory(@Body UpCategoryReq parm);


    @POST("shop/categoryEdit")
    Observable<BaseRespose<ShopInfo>> categoryEdit(@Body UpCategoryReq parm);


    /**
     * 商家商品类别
     * @param parm
     * @return
     */
    @POST("shop/categoryList")
    Observable<BaseRespose<List<ShopInfo>>> getCategoryList(@Body UpCategoryReq parm);


    /**
     * 删除商品类别
     * @param parm
     * @return
     */
    @POST("shop/categoryDelete")
    Observable<BaseRespose<String>> deleteCategory(@Body UpCategoryReq parm);

    /**
     * 添加商品
     * @param parm
     * @return
     */
    @POST("shop/addFood")
    Observable<BaseRespose<String>> addFood(@Body AddFoodReq parm);


    @POST("shop/editGoods")
    Observable<BaseRespose<String>> editGoods(@Body AddFoodReq parm);

    @POST("shop/foodDelete")
    Observable<BaseRespose<String>> foodDelete(@Body RequestBody body);
    /**
     * 收获地址
     * @return
     */
    @POST("shop/addressList")
    Observable<BaseRespose<List<AddressInfo>>> addressList();


    @POST("shop/upAddress")
    Observable<BaseRespose<String>> upAddress(@Body RequestBody body);


    @POST("shop/addressDelete")
    Observable<BaseRespose<String>> deleteAddress(@Body RequestBody body);

    /**
     * 收藏
     * @param parm
     * @return
     */
    @POST("shop/shopCollect")
    Observable<BaseRespose<String>> collection(@Body ShopIdReq parm);


    @POST("shop/shopUncollect")
    Observable<BaseRespose<String>> unCollection(@Body ShopIdReq parm);


    @POST("shop/shopContent")
    Observable<BaseRespose<ShopContent>> shopContent(@Body ShopIdReq parm);


    /**
     * 商家列表
     * @param parm
     * @return
     */
    @POST("shop/shopList")
    Observable<BaseRespose<TakeawayResq>> getShopList(@Body ShopListReq parm);


    /**
     * 下单
     */
    @POST("Order/addOrder")
    Observable<BaseRespose<String>> addOrder(@Body RequestBody body);


    /**
     * 获取订单列表
     * @param body
     * @return
     */
    @POST("Order/orderList")
    Observable<BaseRespose<List<OrderInfo>>> orderList(@Body RequestBody body);


    @POST("Order/orderDetail")
    Observable<BaseRespose<OrderInfo>> orderDetail(@Body RequestBody body);


    @POST("shop/shopComments")
    Observable<BaseRespose<List<CommentInfo>>> shopComments(@Body RequestBody body);


    @POST("shop/goodslist")
    Observable<BaseRespose<List<ShopInfo>>> goodslist(@Body RequestBody body);


    @POST("Order/waimaiPrice")
    Observable<BaseRespose<WaimaiResq>> waimaiPrice(@Body RequestBody body);


}
