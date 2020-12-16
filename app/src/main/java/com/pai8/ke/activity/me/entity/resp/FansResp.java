package com.pai8.ke.activity.me.entity.resp;

import com.pai8.ke.activity.me.entity.PaginationBean;
import com.pai8.ke.entity.User;

import java.util.List;

/**
 * @author Created by zzf
 * @time 2020/12/15 21:48
 * Description：
 */
public class FansResp {

    /**
     * users : [{"id":8,"nickname":"筱因阁-尼克","avatar":"https://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83epW1ZH4kgvmicNYLlMTEI5oKfUIcUXxbTEvvAkS0JMOypEu2k1oXrF0XKypWY2cIk5uORTQnMibQtBQ/132","phone":"","wechat":""}]
     * pagination :  {"has_more":0,"total":5}
     */

    private List<User> users;


    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    private PaginationBean pagination;

    public PaginationBean getPagination() {
        return pagination;
    }

    public void setPagination(PaginationBean pagination) {
        this.pagination = pagination;
    }
}
