package com.pai8.ke.global;

import com.tencent.mm.opensdk.openapi.IWXAPI;

public class GlobalConstants {

    //---------------------HTTP环境配置-----------------------
    //生产环境
    public static String HTTP_URL_RELEASE = "http://test.5pai8.com/api.php/demo/";
    //测试环境
    public static String HTTP_URL_TEST = "http://test.5pai8.com/api.php/demo/";
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

    //下拉刷新
    public static int REFRESH = 0x01;
    //上拉加载
    public static int LOADMORE = 0x02;

    //微信授权登录
    public static IWXAPI wx_api; //全局的微信api对象
    public static final String APP_ID = "wx00290ee6e44c1cfd"; //appid
    public static final String APP_SECRET = "3db5d02b7d93bcffbb15f3f0fb1ead4f";

}