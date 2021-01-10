package com.pai8.ke.activity.wallet.data;

import java.util.List;

public class InOutRecordResp {
    private int count;
    private List<InOutRecordBean> list;
    private String balance;
    private String last_money;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<InOutRecordBean> getList() {
        return list;
    }

    public void setList(List<InOutRecordBean> list) {
        this.list = list;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getLast_money() {
        return last_money;
    }

    public void setLast_money(String last_money) {
        this.last_money = last_money;
    }
}
