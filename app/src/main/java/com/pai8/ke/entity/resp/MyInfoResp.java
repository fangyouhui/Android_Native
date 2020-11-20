package com.pai8.ke.entity.resp;

public class MyInfoResp {
    private int my_follows;
    private int my_fans;
    private int my_likes;
    private int my_history;
    private int verify_status;

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

    public int getVerify_status() {
        return verify_status;
    }

    public void setVerify_status(int verify_status) {
        this.verify_status = verify_status;
    }
}
