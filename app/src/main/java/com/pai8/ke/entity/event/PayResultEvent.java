package com.pai8.ke.entity.event;


import androidx.annotation.IntDef;

/**
 * 支付Event
 */
public class PayResultEvent {

    //支付成功
    public static final int PAY_SUCESS = 1;
    //支付取消
    public static final int PAY_CANCEL = 2;
    //支付失败
    public static final int PAY_FAIL = 3;

    private int result;

    @IntDef({PAY_SUCESS, PAY_CANCEL, PAY_FAIL})
    public @interface Result {
    }

    public PayResultEvent(@Result int result) {
        this.result = result;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

}
