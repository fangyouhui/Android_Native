package com.pai8.ke.activity.me.api;

import com.pai8.ke.activity.message.entity.resp.MessageResp;
import com.pai8.ke.activity.message.entity.resp.MsgCountResp;
import com.pai8.ke.activity.takeaway.entity.FoodGoodInfo;
import com.pai8.ke.activity.takeaway.entity.OrderInfo;
import com.pai8.ke.activity.takeaway.entity.ShopFoodGoodInfo;
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

/**
 * 
 * @author Created by zzf
 * @time  11:06
 * Description：
 */
public interface MineApiService {
    /**
     * 我的关注列表接口
     */
    @POST("user/followOthersList")
    Observable<BaseRespose<List<MessageResp>>> getAttentionList(@Body RequestBody param);

    /**
     * 我的粉丝列表
     * @return
     */
    @POST("user/followMeList")
    Observable<BaseRespose<List<MessageResp>>> getFansList(@Body RequestBody param);



    @POST("shop/categoryEdit")
    Observable<BaseRespose<ShopInfo>> categoryEdit(@Body RequestBody param);


    /**
     * 商家商品类别
     * @param parm
     * @return
     */
    @POST("shop/categoryList")
    Observable<BaseRespose<List<ShopInfo>>> getCategoryList(@Body RequestBody param);


}
