package com.pai8.ke.api;

import com.pai8.ke.activity.me.entity.resp.ShopCouponListResult;
import com.pai8.ke.base.BaseRespose;
import com.pai8.ke.entity.UserInfo;
import com.pai8.ke.entity.Video;
import com.pai8.ke.entity.req.CodeReq;
import com.pai8.ke.entity.req.LoginReq;
import com.pai8.ke.entity.req.VideoPublishReq;
import com.pai8.ke.entity.resp.AttentionResp;
import com.pai8.ke.entity.resp.BusinessType;
import com.pai8.ke.entity.resp.CommentResp;
import com.pai8.ke.entity.resp.CouponListResp;
import com.pai8.ke.entity.resp.MyInfoResp;
import com.pai8.ke.entity.resp.Province;
import com.pai8.ke.entity.resp.ShareMiniResp;
import com.pai8.ke.entity.resp.ShareResp;
import com.pai8.ke.entity.resp.ShopList;
import com.pai8.ke.entity.resp.VersionResp;
import com.pai8.ke.entity.resp.VideoListResp;
import com.pai8.ke.entity.resp.VideoNearResp;
import com.pai8.ke.entity.resp.VideoResp;
import com.pai8.ke.entity.resp.WxOrderPrepayResp;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/*
 * Api接口服务类
 * Created by gh on 2020/6/22.
 */
public interface ApiService {

    /**
     * 获取短信验证码
     */
    @POST("public/getCode")
    Observable<BaseRespose> verifyCode(@Body CodeReq param);

    /**
     * 登录
     */
    @POST("public/login")
    Observable<BaseRespose<UserInfo>> login(@Body LoginReq param);

    /**
     * 手机号登录
     *
     * @param param
     * @return
     */
    @POST("public/mobileLogin")
    Observable<BaseRespose<UserInfo>> mobileLogin(@Body LoginReq param);

    /**
     * 微信登录
     *
     * @param code 微信平台code
     * @return
     */
    @FormUrlEncoded
    @POST("public/getUid")
    Observable<BaseRespose<UserInfo>> getUid(@Field("code") String code);

    /**
     * 用户注销账号
     */
    @POST("public/MemberLogout")
    Observable<BaseRespose> memberLogout();

    /**
     * 获取个人中心
     *
     * @return
     */
    @POST("user/ucenter")
    Observable<BaseRespose<MyInfoResp>> getMyInfo();

    /**
     * 根据id获取用户信息
     *
     * @param uid
     * @return
     */
    @FormUrlEncoded
    @POST("user/getInfoByUid")
    Observable<BaseRespose<UserInfo>> getUserInfoById(@Field("uid") String uid);

    @POST("system/checkUpgrade")
    Observable<BaseRespose<VersionResp>> checkUpgrade();

    //****************************视频模块********************************

    /**
     * 举报
     *
     * @param video_id 视频id
     * @param content  举报内容
     * @return
     */
    @FormUrlEncoded
    @POST("index/report")
    Observable<BaseRespose> report(@Field("video_id") String video_id,
                                   @Field("content") String content);

    /**
     * 评论
     *
     * @return
     */
    @FormUrlEncoded
    @POST("video/addReview")
    Observable<BaseRespose<VideoResp>> comment(@Field("video_id") String video_id,
                                               @Field("pid") String pid,
                                               @Field("content") String content);

    /**
     * 评论列表
     *
     * @param video_id
     * @return
     */
    @FormUrlEncoded
    @POST("index/comments")
    Observable<BaseRespose<List<CommentResp>>> comments(@Field("video_id") String video_id);

    /**
     * 视频分享
     *
     * @param video_id
     * @return
     */
    @FormUrlEncoded
    @POST("index/shareUrl")
    Observable<BaseRespose<ShareResp>> shareUrl(@Field("video_id") String video_id);

    /**
     * 删除视频
     *
     * @param video_id
     * @return
     */
    @FormUrlEncoded
    @POST("video/delete")
    Observable<BaseRespose> deleteVideo(@Field("id") String video_id);

    /**
     * 查看视频
     */
    @FormUrlEncoded
    @POST("video/look")
    Observable<BaseRespose<VideoResp>> look(@Field("id") String video_id);

    /**
     * 关注/取消关注
     */
    @FormUrlEncoded
    @POST("video/attention")
    Observable<BaseRespose<AttentionResp>> attention(@Field("uid") String uid);

    /**
     * 点赞/取消点赞
     */
    @FormUrlEncoded
    @POST("video/like")
    Observable<BaseRespose<VideoResp>> like(@Field("video_id") String video_id);

    /**
     * 附近的视频列表（新）
     *
     * @return
     */
    @FormUrlEncoded
    @POST("video/nearby")
    Observable<BaseRespose<VideoListResp>> nearby(@Field("page") int page,
                                                  @Field("size") int size);

    /**
     * 推荐的视频列表（新）
     *
     * @return
     */
    @FormUrlEncoded
    @POST("video/flow")
    Observable<BaseRespose<VideoListResp>> flow(@Field("page") int page,
                                                @Field("size") int size);

    /**
     * 关注的视频列表（新）
     *
     * @return
     */
    @FormUrlEncoded
    @POST("video/follow")
    Observable<BaseRespose<VideoListResp>> follow(@Field("page") int page,
                                                  @Field("size") int size);

    /**
     * 搜索的视频列表（新）
     *
     * @return
     */
    @FormUrlEncoded
    @POST("video/search")
    Observable<BaseRespose<VideoListResp>> search(@Field("keywords") String keywords,
                                                  @Field("page") int page,
                                                  @Field("size") int size);

    /**
     * 我发布的视频列表（新）
     *
     * @return
     */
    @FormUrlEncoded
    @POST("video/myVideo")
    Observable<BaseRespose<VideoListResp>> myVideo(@Field("page") int page,
                                                   @Field("size") int size);

    /**
     * 我喜欢的视频列表（新）
     *
     * @return
     */
    @FormUrlEncoded
    @POST("video/myLike")
    Observable<BaseRespose<VideoListResp>> myLike(@Field("page") int page,
                                                  @Field("size") int size);

    /**
     * 关联我的或我关联的视频列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("video/mylink")
    Observable<BaseRespose<VideoListResp>> myLink(@Field("page") int page,
                                                  @Field("size") int size);

    /**
     * 推荐视频列表
     */
    @FormUrlEncoded
    @POST("index/videoList")
    Observable<BaseRespose<List<Video>>> videoList(@FieldMap Map<String, Object> fields);

    /**
     * 附近的视频列表
     */
    @FormUrlEncoded
    @POST("index/nearbyVideoList")
    Observable<BaseRespose<VideoNearResp>> nearbyVideoList(@FieldMap Map<String, Object> fields);

    /**
     * 关注的视频列表
     */
    @FormUrlEncoded
    @POST("index/followVideoList")
    Observable<BaseRespose<List<Video>>> followVideoList(@FieldMap Map<String, Object> fields);

    /**
     * 我的视频列表（个人中心）
     */
    @FormUrlEncoded
    @POST("user/myVideoList")
    Observable<BaseRespose<List<Video>>> myVideoList(@FieldMap Map<String, Object> fields);

    /**
     * 我喜欢的列表
     */
    @FormUrlEncoded
    @POST("user/mylikeVideoList")
    Observable<BaseRespose<List<Video>>> myLikeVideoList(@FieldMap Map<String, Object> fields);

    /**
     * 通知极光推送通话
     *
     * @param id     接收通知的用户
     * @param m_type 1-音频 2-视频 3-拒绝
     * @return
     */
    @FormUrlEncoded
    @POST("Jpush/pushsingle")
    Observable<BaseRespose> notifyPush(@Field("id") String id, @Field("m_type") String m_type, @Field(
            "content") String content);

    /**
     * 获取七牛音视频token值
     *
     * @param roomNumber 房间号（5p+用户id）
     * @param userid     用户标识号（5p8+用户id）
     * @return
     */
    @FormUrlEncoded
    @POST("qiniu/qiniuToken")
    Observable<BaseRespose<String>> qnToken(@Field("roomNumber") String roomNumber,
                                            @Field("userid") String userid);

    /**
     * 上传视频
     *
     * @param req
     * @return
     */
    @POST("index/upVideo")
    Observable<BaseRespose> upVideo(@Body VideoPublishReq req);

    //***************************视频模块End*******************************

    @POST("public/area")
    Observable<BaseRespose<List<Province>>> getArea();

    @POST("public/qiniuToken")
    Observable<BaseRespose<String>> getQNToken();

    /**
     * 分类列表，用于在拍视频、商家申请入驻选择分类的时候选择填写的
     */
    @POST("index/businessType")
    Observable<BaseRespose<List<BusinessType>>> getBusinessType();

    /**
     * 商家列表
     */
    @FormUrlEncoded
    @POST("shop/shopSelect")
    Observable<BaseRespose<List<ShopList>>> shopSelect(@Field("page") int page,
                                                       @Field("keywords") String keywords);

    @FormUrlEncoded
    @POST("Order/orderPrepay")
    Observable<BaseRespose<WxOrderPrepayResp>> orderPrepay(@Field("order_no") String order_no
            , @Field("buyer_id") String buyer_id, @Field("pay_type") int pay_type);

    @FormUrlEncoded
    @POST("Order/orderPrepay")
    Observable<BaseRespose<String>> orderPrepayZFB(@Field("order_no") String order_no
            , @Field("buyer_id") String buyer_id, @Field("pay_type") int pay_type);

    /**
     * 用户所有优惠券
     *
     * @param time_type 优惠券时效类型 1为可用 2为超时 3为所有
     * @return
     */
    @FormUrlEncoded
    @POST("Coupon/allUserCouponList")
    Observable<BaseRespose<List<CouponListResp>>> allUserCouponList(@Field("time_type") int time_type);

    /**
     * 当前店铺可领取优惠券列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("Coupon/shopCouponList")
    Observable<BaseRespose<ShopCouponListResult>> shopCouponList(@Field("shop_id") String shop_id,
                                                                 @Field("buyer_id") String buyer_id);

    /**
     * 领取
     *
     * @return
     */
    @FormUrlEncoded
    @POST("Coupon/getCoupon")
    Observable<BaseRespose> getCoupon(@Field("buyer_id") String buyer_id,
                                      @Field("coupon_ids") String coupon_ids);


    /**
     * 添加优惠券
     *
     * @return
     */
    @FormUrlEncoded
    @POST("Coupon/addCoupon")
    Observable<BaseRespose> addCoupon(@Field("shop_id") String shop_id,
                                      @Field("dis_price") String dis_price,
                                      @Field("trig_price") String trig_price,
                                      @Field("days") String days,
                                      @Field("type") String type,
                                      @Field("num") String num);

    /**
     * 编辑优惠券
     *
     * @return
     */
    @FormUrlEncoded
    @POST("Coupon/editCoupon")
    Observable<BaseRespose> editCoupon(@Field("coupon_id") String coupon_id,
                                       @Field("shop_id") String shop_id,
                                       @Field("dis_price") String dis_price,
                                       @Field("trig_price") String trig_price,
                                       @Field("days") String days,
                                       @Field("type") String type,
                                       @Field("num") String num);

    /**
     * 删除优惠券
     *
     * @return
     */
    @FormUrlEncoded
    @POST("Coupon/delCoupon")
    Observable<BaseRespose> delCoupon(@Field("coupon_id") String coupon_id,
                                      @Field("shop_id") String shop_id);

    @FormUrlEncoded
    @POST("system/share")
    Observable<BaseRespose<ShareMiniResp>> shareMini(@Field("type") int type,
                                                     @Field("id") String id);

    @FormUrlEncoded
    @POST("Order/verifyOrder")
    Observable<BaseRespose<List<String>>> verifyOrder(@Field("order_no") String order_no,
                                                      @Field("shop_id") String shop_id);
}
