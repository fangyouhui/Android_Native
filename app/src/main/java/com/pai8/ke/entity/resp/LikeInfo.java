package com.pai8.ke.entity.resp;

import com.pai8.ke.entity.User;
import com.pai8.ke.entity.Video;

public class LikeInfo {
    private int id;
    private int video_id;
    private int video_user_id;
    private int user_id;
    private int like_status;
    private long create_time;
    private User user;
    private Video video;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVideo_id() {
        return video_id;
    }

    public void setVideo_id(int video_id) {
        this.video_id = video_id;
    }

    public int getVideo_user_id() {
        return video_user_id;
    }

    public void setVideo_user_id(int video_user_id) {
        this.video_user_id = video_user_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getLike_status() {
        return like_status;
    }

    public void setLike_status(int like_status) {
        this.like_status = like_status;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }
}
