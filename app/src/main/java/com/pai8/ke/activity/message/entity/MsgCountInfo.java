package com.pai8.ke.activity.message.entity;

/**
 * @author Created by zzf
 * @time 2020/12/7 21:44
 * Description：
 */
public class MsgCountInfo {

    private String count;

    private int pos;

    public MsgCountInfo(String count, int pos) {
        this.count = count;
        this.pos = pos;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
