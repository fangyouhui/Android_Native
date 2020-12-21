package com.pai8.ke.entity.event;


import androidx.annotation.IntDef;

/**
 * 登录状态
 * Created by gh on 2020/12/18.
 */
public class LoginStatusEvent {

    public static final int LOGIN = 1;
    public static final int LOGOUT = 2;

    @IntDef({LOGIN, LOGOUT})
    public @interface Status {
    }

    private int Status;

    public LoginStatusEvent(@Status int status) {
        Status = status;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

}
