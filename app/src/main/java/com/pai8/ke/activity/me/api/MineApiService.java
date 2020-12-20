package com.pai8.ke.activity.me.api;

import com.pai8.ke.activity.me.entity.resp.AttentionMineResp;
import com.pai8.ke.activity.me.entity.resp.FansResp;
import com.pai8.ke.activity.me.entity.resp.HistoryResp;
import com.pai8.ke.activity.me.entity.resp.UserInfoResp;
import com.pai8.ke.activity.message.entity.resp.MessageResp;
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
    @POST("my/fans")
    Observable<BaseRespose<FansResp>> getFansList(@Body RequestBody param);

    /**
     * 我的关注
     */
    @POST("my/follow")
    Observable<BaseRespose<AttentionMineResp>> getAttentionMineList(@Body RequestBody param);

    /**
     * 获赞
     */
    @POST("my/zan")
    Observable<BaseRespose<HistoryResp>> getLikesList(@Body RequestBody param);


    /**
     * 我的足迹
     */
    @POST("my/history")
    Observable<BaseRespose<HistoryResp>> getHistoryList(@Body RequestBody param);

    /**
     * 修改用户信息
     * @param param
     * @return
     */
    @POST("user/setInfo")
    Observable<BaseRespose> setInfo(@Body RequestBody param);


    /**
     * 根据uid获取用户信息
     * @param param
     * @return
     */
    @POST("user/getInfoByUid")
    Observable<BaseRespose<UserInfoResp>> getInfoByUid(@Body RequestBody param);


}
