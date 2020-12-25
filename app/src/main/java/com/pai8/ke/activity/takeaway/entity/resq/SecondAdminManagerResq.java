package com.pai8.ke.activity.takeaway.entity.resq;

import java.util.List;

/**
 * @author Created by zzf
 * @time 2020/12/23 22:13
 * Description：
 */
public class SecondAdminManagerResq {
    /**
     * id : 159
     * user_nickname : 好人一生平安，
     * avatar : https://thirdwx.qlogo.cn/mmopen/vi_32/tZ7121CNYZKPibtHlqCsVHNXFJ5P95ialqbgVFNFZBWwzCCysWvCL7COGKRY7bwm8o26b3ZEqVIoKhqIJCJx57CQ/132
     * phone :
     * china_id : 450500
     * add_time : 1607933791
     * power : 2,3,4
     * power_array : [{"id":2,"title":"订单处理","url":"","icon":null,"add_time":"1608012682"},{"id":3,"title":"团购核对","url":"","icon":null,"add_time":"1608012682"},{"id":4,"title":"收入提现","url":"","icon":null,"add_time":"1608012682"}]
     */

    private int id;
    private String user_nickname;
    private String avatar;
    private String phone;
    private int china_id;
    private String add_time;
    private String power;
    private List<PowerArrayBean> power_array;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getChina_id() {
        return china_id;
    }

    public void setChina_id(int china_id) {
        this.china_id = china_id;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public List<PowerArrayBean> getPower_array() {
        return power_array;
    }

    public void setPower_array(List<PowerArrayBean> power_array) {
        this.power_array = power_array;
    }

    public static class PowerArrayBean {
        /**
         * id : 2
         * title : 订单处理
         * url :
         * icon : null
         * add_time : 1608012682
         */

        private int id;
        private String title;
        private String url;
        private String icon;
        private String add_time;
        private boolean isChoose;

        public boolean isChoose() {
            return isChoose;
        }

        public void setChoose(boolean choose) {
            isChoose = choose;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }
    }
}
