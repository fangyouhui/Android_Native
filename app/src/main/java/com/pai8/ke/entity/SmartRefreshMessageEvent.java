package com.pai8.ke.entity;

public class SmartRefreshMessageEvent {

    public final static int FINISH_REFRESH = 0;
    public final static int FINISH_LOAD_MORE = 1;
    public final static int FINISH_LOAD_MORE_WITH_NO_MORE = 2;

    public int smartRefreshType;

}
