package com.pai8.ke.activity.takeaway.api;

import com.pai8.ke.activity.takeaway.entity.req.AddFoodReq;
import com.pai8.ke.activity.takeaway.entity.req.MerchantSettledReq;
import com.pai8.ke.activity.takeaway.entity.req.ShopIdReq;
import com.pai8.ke.activity.takeaway.entity.req.ShopListReq;
import com.pai8.ke.activity.takeaway.entity.req.UpCategoryReq;
import com.pai8.ke.activity.takeaway.entity.resq.ShopInfo;
import com.pai8.ke.activity.takeaway.entity.resq.TakeawayResq;
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


    /**
     * 添加分类
     * @return
     */
    @POST("shop/upCategory")
    Observable<BaseRespose<ShopInfo>> addUpCategory(@Body UpCategoryReq parm);

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


    @POST("shop/shopCollect")
    Observable<BaseRespose<String>> collection(@Body ShopIdReq parm);


    @POST("shop/shopUncollect")
    Observable<BaseRespose<String>> unCollection(@Body ShopIdReq parm);

    /**
     * 商家列表
     * @param parm
     * @return
     */
    @POST("shop/shopList")
    Observable<BaseRespose<TakeawayResq>> getShopList(@Body ShopListReq parm);


}
