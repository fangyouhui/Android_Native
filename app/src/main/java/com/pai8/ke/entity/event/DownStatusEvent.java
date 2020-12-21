package com.pai8.ke.entity.event;

/**
 * 下载进度Event
 * Created by gh on 2020/12/20.
 */
public class DownStatusEvent {

    public static final int DOWN_START = 0xff01;
    public static final int DOWN_LODING = 0xff02;
    public static final int DOWN_COMPLETE = 0xff03;

    private int status;
    private int progress;

    public DownStatusEvent() {

    }

    public DownStatusEvent(int status, int progress) {
        this.status = status;
        this.progress = progress;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
