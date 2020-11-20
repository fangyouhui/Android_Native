package com.pai8.ke.api;

import com.pai8.ke.base.BaseRespose;
import com.pai8.ke.entity.req.CodeReq;
import com.pai8.ke.entity.req.LoginReq;
import com.pai8.ke.entity.req.VideoPublishReq;
import com.pai8.ke.entity.resp.MyInfoResp;
import com.pai8.ke.entity.resp.ShopListResp;
import com.pai8.ke.entity.resp.BusinessType;
import com.pai8.ke.entity.resp.Province;
import com.pai8.ke.entity.resp.CommentResp;
import com.pai8.ke.entity.resp.ShareResp;
import com.pai8.ke.entity.resp.UserInfo;
import com.pai8.ke.entity.resp.VideoResp;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
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
     * 获取个人中心
     *
     * @return
     */
    @POST("user/ucenter")
    Observable<BaseRespose<MyInfoResp>> getMyInfo();

    //****************************视频模块********************************

    /**
     * 视频列表（仿抖音）
     */
    @FormUrlEncoded
    @POST("index/contentList")
    Observable<BaseRespose<List<VideoResp>>> contentList(@Field("video_id") String video_id,
                                                         @Field("page") int page);

    /**
     * 关注用户
     *
     * @param to_user_id
     * @return
     */
    @FormUrlEncoded
    @POST("index/follow")
    Observable<BaseRespose> follow(@Field("to_user_id") String to_user_id);

    /**
     * 取消关注用户
     *
     * @param to_user_id
     * @return
     */
    @FormUrlEncoded
    @POST("index/unfollow")
    Observable<BaseRespose> unfollow(@Field("to_user_id") String to_user_id);

    /**
     * 喜欢
     *
     * @param video_id 视频id
     * @return
     */
    @FormUrlEncoded
    @POST("index/like")
    Observable<BaseRespose> like(@Field("video_id") String video_id);

    /**
     * 取消喜欢
     *
     * @param video_id 视频id
     * @return
     */
    @FormUrlEncoded
    @POST("index/like")
    Observable<BaseRespose> unlike(@Field("video_id") String video_id);

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
     * @param video_id
     * @param top_id     评论顶级id，一级评论top_id为0，二级评论top_id就是所属一级评论的id
     * @param pid        评论的父id，就是你回复那条评论的id。直接评论视频的话，pid为0
     * @param content    评论内容
     * @param to_user_id 被评论的用户id，被回复的用户id
     * @return
     */
    @FormUrlEncoded
    @POST("index/comment")
    Observable<BaseRespose> comment(@Field("video_id") String video_id,
                                    @Field("top_id") String top_id,
                                    @Field("pid") String pid,
                                    @Field("content") String content,
                                    @Field("to_user_id") String to_user_id);

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
     * 推荐的视频列表
     *
     * @param keywords
     * @param page
     * @return
     */
    @FormUrlEncoded
    @POST("index/videoList")
    Observable<BaseRespose<List<VideoResp>>> videoList(@Field("keywords") String keywords,
                                                       @Field("page") int page);

    /**
     * 附近的视频列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("index/nearbyVideoList")
    Observable<BaseRespose<List<VideoResp>>> nearbyVideoList(@Field("keywords") String keywords,
                                                             @Field("page") int page);

    /**
     * 关注的视频列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("index/followVideoList")
    Observable<BaseRespose<List<VideoResp>>> followVideoList(@Field("page") int page);


    /**
     * 通知极光推送通话
     *
     * @param id     接收通知的用户
     * @param c_type 呼叫类型 0为音频 1为视频
     * @return
     */
    @FormUrlEncoded
    @POST("Jpush/pushsingle")
    Observable<BaseRespose> notifyPush(@Field("id") String id, @Field("c_type") String c_type);


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
    @POST("shop/shopList")
    Observable<BaseRespose<ShopListResp>> shopList(@Field("page") int page,
                                                   @Field("keywords") String keywords);

}
