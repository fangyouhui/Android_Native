package com.pai8.ke.entity.resp;

public class MyInfoResp {


    /**
     * user_id : 122
     * avatar : https://jianshen.fyh5p8.com/FnOJBxi6OWdVItjRHbFBN3tMbxs6
     * user_nickname : 哥来也
     * shop_id : null
     * verify_status : null
     * my_follows : 0
     * my_fans : 0
     * my_likes : 0
     * my_history : 10
     * is_business : 1
     */

    private int user_id;
    private String avatar;
    private String user_nickname;
    private String shop_id;
    private Integer verify_status;
    private int my_follows;
    private int my_fans;
    private int my_likes;
    private int my_history;
    private int is_business;
    private String manage;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public Integer getVerify_status() {
        return verify_status == null ? 0 : verify_status;
    }

    public void setVerify_status(Integer verify_status) {
        this.verify_status = verify_status;
    }

    public int getMy_follows() {
        return my_follows;
    }

    public void setMy_follows(int my_follows) {
        this.my_follows = my_follows;
    }

    public int getMy_fans() {
        return my_fans;
    }

    public void setMy_fans(int my_fans) {
        this.my_fans = my_fans;
    }

    public int getMy_likes() {
        return my_likes;
    }

    public void setMy_likes(int my_likes) {
        this.my_likes = my_likes;
    }

    public int getMy_history() {
        return my_history;
    }

    public void setMy_history(int my_history) {
        this.my_history = my_history;
    }

    public int getIs_business() {
        return is_business;
    }

    public void setIs_business(int is_business) {
        this.is_business = is_business;
    }

    public String getManage() {
        return manage;
    }

    public void setManage(String manage) {
        this.manage = manage;
    }
}
