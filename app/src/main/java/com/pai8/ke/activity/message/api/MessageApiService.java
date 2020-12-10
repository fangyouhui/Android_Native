package com.pai8.ke.activity.message.api;

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
public interface MessageApiService {
    /**
     * 消息分类下列表接口
     */
    @POST("Msg/msgList")
    Observable<BaseRespose<List<MessageResp>>> getMsgList(@Body RequestBody param);

    /**
     * 消息数量
     * @param param
     * @return
     */
    @POST("Msg/msgIndex")
    Observable<BaseRespose<MsgCountResp>> getMsgCount(@Body RequestBody param);


    /**
     * 取消关注
     * @param param
     * @return
     */
    @POST("index/unfollow")
    Observable<BaseRespose> cancelAttention(@Body RequestBody param);


    /**
     * 关注用户
     * @param param
     * @return
     */
    @POST("index/follow")
    Observable<BaseRespose> getAttention(@Body RequestBody param);


}
