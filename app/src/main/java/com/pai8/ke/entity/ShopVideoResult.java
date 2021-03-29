package com.pai8.ke.entity;

import java.io.Serializable;

public class ShopVideoResult implements Serializable {

    private int id;
    private String video_title;
    private String video_desc;
    private String video_path;
    private String cover_path;
    private int like_counts;
    private int goods_id;
    private String user_nickname;
    private String avatar;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setVideo_title(String video_title) {
        this.video_title = video_title;
    }

    public String getVideo_title() {
        return video_title;
    }

    public void setVideo_desc(String video_desc) {
        this.video_desc = video_desc;
    }

    public String getVideo_desc() {
        return video_desc;
    }

    public void setVideo_path(String video_path) {
        this.video_path = video_path;
    }

    public String getVideo_path() {
        return video_path;
    }

    public void setCover_path(String cover_path) {
        this.cover_path = cover_path;
    }

    public String getCover_path() {
        return cover_path;
    }

    public void setLike_counts(int like_counts) {
        this.like_counts = like_counts;
    }

    public int getLike_counts() {
        return like_counts;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar() {
        return avatar;
    }


}
