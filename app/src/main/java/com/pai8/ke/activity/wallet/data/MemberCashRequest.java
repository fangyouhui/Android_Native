package com.pai8.ke.activity.wallet.data;

import java.io.Serializable;

/**
 * Created by atian
 * on 1/9/21
 * describe:
 */

public class MemberCashRequest implements Serializable {
    private String balance;//提现金额
    private String cash_type;//提现方式 1：支付宝 2：微信 3：银行卡
    private String cash_nickname;//真实姓名
    private String cash_account;//提现账户
    private String cash_bankname;//银行名称
    private String cash_bankadd;//开户行地址

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCash_type() {
        return cash_type;
    }

    public void setCash_type(String cash_type) {
        this.cash_type = cash_type;
    }

    public String getCash_nickname() {
        return cash_nickname;
    }

    public void setCash_nickname(String cash_nickname) {
        this.cash_nickname = cash_nickname;
    }

    public String getCash_account() {
        return cash_account;
    }

    public void setCash_account(String cash_account) {
        this.cash_account = cash_account;
    }

    public String getCash_bankname() {
        return cash_bankname;
    }

    public void setCash_bankname(String cash_bankname) {
        this.cash_bankname = cash_bankname;
    }

    public String getCash_bankadd() {
        return cash_bankadd;
    }

    public void setCash_bankadd(String cash_bankadd) {
        this.cash_bankadd = cash_bankadd;
    }
}
