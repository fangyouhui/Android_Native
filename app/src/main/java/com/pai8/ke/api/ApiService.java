package com.pai8.ke.api;

import com.pai8.ke.base.BaseRespose;
import com.pai8.ke.entity.req.CodeReq;
import com.pai8.ke.entity.req.LoginReq;
import com.pai8.ke.entity.resp.BusinessType;
import com.pai8.ke.entity.resp.Province;
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

    //****************************视频模块********************************

    /**
     * 视频列表
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
     * 关注用户
     *
     * @param from_user_id
     * @return
     */
    @FormUrlEncoded
    @POST("index/follow")
    Observable<BaseRespose> follow(@Field("from_user_id") String from_user_id);

    /**
     * 取消关注用户
     *
     * @param from_user_id
     * @return
     */
    @FormUrlEncoded
    @POST("index/unfollow")
    Observable<BaseRespose> unfollow(@Field("from_user_id") String from_user_id);

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
     * 视频分享
     *
     * @param video_id
     * @return
     */
    @FormUrlEncoded
    @POST("index/shareUrl")
    Observable<BaseRespose<ShareResp>> shareUrl(@Field("video_id") String video_id);

    /**
     * 附近的视频
     *
     * @param longitude
     * @param latitude
     * @param keywords
     * @return
     */
    @FormUrlEncoded
    @POST("index/nearbyVideoList")
    Observable<BaseRespose> nearbyVideoList(@Field("longitude") String longitude,
                                            @Field("latitude") String latitude,
                                            @Field("longitude") String keywords);

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



}
