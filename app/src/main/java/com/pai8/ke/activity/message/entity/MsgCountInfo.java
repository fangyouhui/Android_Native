package com.pai8.ke.activity.message.entity;

/**
 * @author Created by zzf
 * @time 2020/12/7 21:44
 * Description：
 */
public class MsgCountInfo {

    private int count;

    private int pos;

    public MsgCountInfo(int count, int pos) {
        this.count = count;
        this.pos = pos;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
