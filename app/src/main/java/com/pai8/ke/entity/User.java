package com.pai8.ke.entity;

import java.io.Serializable;

public class User implements Serializable {

    /**
     * id : 17
     * nickname :
     * avatar :
     * phone : 15061009506
     * wechat :
     */

    private String id;
    private String nickname;
    private String avatar;
    private String phone;
    private String wechat;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }
}
