package com.pai8.ke.entity.resp;

import java.io.Serializable;

/**
 * 视频Entity
 */
public class VideoResp implements Serializable {

    /**
     * id : 6
     * video_desc : 给个广告
     * video_path : https://jianshen.fyh5p8.com/short_video_20201012145738.mp4
     * cover_path : https://jianshen.fyh5p8.com/short_video_20201012145738.mp4?vframe/jpg/offset/0
     * like_counts : 0
     * longitude : 120.734261
     * latitude : 31.260296
     * shop_id : 1
     * comment_counts : 27
     * look_counts : 0
     * create_time : 2020-10-12
     * user_id : 6
     * user_nickname : 15050137225
     * avatar :
     * like_status : 0
     * follow_status : 1
     */

    private String id;
    private String video_desc;
    private String video_path;
    private String cover_path;
    private int like_counts;
    private String longitude;
    private String latitude;
    private String shop_id;
    private int comment_counts;
    private int look_counts;
    private String create_time;
    private String user_id;
    private String user_nickname;
    private String avatar;
    private int like_status;
    private int follow_status;
    private String shop_name;
    private String distance;
    private String business_district;
    private String mobile;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideo_desc() {
        return video_desc;
    }

    public void setVideo_desc(String video_desc) {
        this.video_desc = video_desc;
    }

    public String getVideo_path() {
        return video_path;
    }

    public void setVideo_path(String video_path) {
        this.video_path = video_path;
    }

    public String getCover_path() {
        return cover_path;
    }

    public void setCover_path(String cover_path) {
        this.cover_path = cover_path;
    }

    public int getLike_counts() {
        return like_counts;
    }

    public void setLike_counts(int like_counts) {
        this.like_counts = like_counts;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public int getComment_counts() {
        return comment_counts;
    }

    public void setComment_counts(int comment_counts) {
        this.comment_counts = comment_counts;
    }

    public int getLook_counts() {
        return look_counts;
    }

    public void setLook_counts(int look_counts) {
        this.look_counts = look_counts;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getLike_status() {
        return like_status;
    }

    public void setLike_status(int like_status) {
        this.like_status = like_status;
    }

    public int getFollow_status() {
        return follow_status;
    }

    public void setFollow_status(int follow_status) {
        this.follow_status = follow_status;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getBusiness_district() {
        return business_district;
    }

    public void setBusiness_district(String business_district) {
        this.business_district = business_district;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
