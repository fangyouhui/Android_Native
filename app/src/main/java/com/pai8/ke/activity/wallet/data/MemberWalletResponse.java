package com.pai8.ke.activity.wallet.data;

import java.io.Serializable;

/**
 * Created by atian
 * on 1/9/21
 * describe:
 */

public class MemberWalletResponse implements Serializable {
    private String balance;//当前用户余额
    private String money;//获取用户昨日收益
    private String money_sum;//获取用户累计收益

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getMoney_sum() {
        return money_sum;
    }

    public void setMoney_sum(String money_sum) {
        this.money_sum = money_sum;
    }
}
