package com.pai8.ke.activity.me.entity.resp;

/**
 * @author Created by zzf
 * @time 2020/12/12 23:03
 * Description：
 */
public class UserInfoResp {
    /**
     * avatar : https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKbWwpicJhyAcWTb9h1pD6S3BbE4Uqp5MR2vb5w3jQ127uXAzrSwibVb7ckiaUuWYIbFMfFglxOPV8cw/132
     * user_nickname : 哥来也
     * wechat :
     */

    private String avatar;
    private String user_nickname;
    private String wechat;

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

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }
}
