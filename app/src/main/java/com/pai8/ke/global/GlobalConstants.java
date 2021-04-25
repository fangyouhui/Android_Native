package com.pai8.ke.global;

public class GlobalConstants {

    //---------------------HTTP环境配置-----------------------
    public static String HTTP_HOST = "https://www.5pai8.com/"; // http://test.5pai8.com/
    //生产环境
    public static String HTTP_URL_RELEASE = HTTP_HOST + "api.php/demo/";
    // public static String HTTP_URL_RELEASE = "http://test.5pai8.com/api.php/demo/";
    //测试环境
    public static String HTTP_URL_TEST = "http://test.5pai8.com/api.php/demo/";
    //开发环境
    public static String HTTP_URL_DEV = "";

    public static String HTTP_PRIVACY_PROTOCOL = GlobalConstants.HTTP_HOST + "agreement/serverProtocol/index.html";//隐私政策

    public static String HTTP_SERVER_PROTOCOL = GlobalConstants.HTTP_HOST + "agreement/serverProtocol/index.html";//协议


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
    //一页显示的数据
    public static final int SIZE = 8;
    public static final int PAGE_SIZE = 10;

    public static final String APP_ID = "wx00290ee6e44c1cfd"; //appid
    public static final String APP_SECRET = "3db5d02b7d93bcffbb15f3f0fb1ead4f";

}