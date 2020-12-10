package com.pai8.ke.entity.resp;

import com.pai8.ke.entity.User;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.utils.StringUtils;

import java.io.Serializable;

/**
 * 视频Entity
 */
public class VideoResp implements Serializable {

    private String id;
    private String video_desc;
    private String video_path;
    private String cover_path;
    private int like_counts;
    private String longitude;
    private String latitude;
    private int comment_counts;
    private int look_counts;
    private String create_time;
    private int like_status;
    private int follow_status;
    private int onsale;
    private String shop_id;
    private String shop_name;
    private String shop_img;
    private String distance;
    private String business_district;
    private User user;
    private String share_url;

    private int page;

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    private String proxyUrl;

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

    public String getProxyUrl() {
        return proxyUrl;
    }

    public void setProxyUrl(String proxyUrl) {
        this.proxyUrl = proxyUrl;
    }

    public String getShop_img() {
        return shop_img;
    }

    public void setShop_img(String shop_img) {
        this.shop_img = shop_img;
    }

    public int getOnsale() {
        return onsale;
    }

    public void setOnsale(int onsale) {
        this.onsale = onsale;
    }

    public boolean isSelf() {
        return StringUtils.equals(AccountManager.getInstance().getUid(), user.getId());
    }
}
