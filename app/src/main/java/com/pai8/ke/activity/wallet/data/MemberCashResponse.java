package com.pai8.ke.activity.wallet.data;

import java.io.Serializable;

/**
 * Created by atian
 * on 1/9/21
 * describe:
 */

public class MemberCashResponse implements Serializable {
    private String code;
    private String msg;
    private String result;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
