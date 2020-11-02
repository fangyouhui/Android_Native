package com.pai8.ke.global;

public class GlobalConstants {

    //---------------------HTTP环境配置-----------------------
    //生产环境
    public static String HTTP_URL_RELEASE = "";
    //测试环境
    public static String HTTP_URL_TEST = "";
    //开发环境
    public static String HTTP_URL_DEV = "";

    //--------------------网络请求相关------------------------
    //连接超时时间
    public static int CONNECT_TIMEOUT = 15;
    //写超时时间
    public static int WRITE_TIMEOUT = 20;
    //读超时时间
    public static int READ_TIMEOUT = 20;

    //网络未连接
    public static int HTTP_NET_ERROR = 0x01;
    //连接异常
    public static int HTTP_CONN_ERROR = 0x02;
    //连接异常
    public static int HTTP_CONN_TIMEOUT = 0x03;
    //数据解析异常
    public static int HTTP_DATA_PARSE_ERROR = 0x04;
    //Token失效
    public static int HTTP_TOKEN_ERROR = 0x05;

}