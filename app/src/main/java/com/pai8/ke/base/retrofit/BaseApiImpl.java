package com.pai8.ke.base.retrofit;

import com.google.gson.GsonBuilder;
import com.pai8.ke.app.MyApp;
import com.pai8.ke.global.GlobalConstants;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.utils.AppUtils;
import com.pai8.ke.utils.HttpsUtils;
import com.pai8.ke.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * retrofit实例
 * Created by gh on 2018/7/27.
 */
public class BaseApiImpl implements BaseApi {

    private volatile static Retrofit retrofit;

    protected Retrofit.Builder retrofitBuilder = new Retrofit.Builder();

    protected OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();

    public BaseApiImpl(String baseUrl) {

        retrofitBuilder.addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setLenient()
                        .create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient())
                .baseUrl(baseUrl);

    }

    /**
     * 构建Retrofit
     *
     * @return
     */
    @Override
    public Retrofit getRetrofit() {

        if (retrofit == null) {
            synchronized (BaseApiImpl.class) {
                if (retrofit == null) {
                    retrofit = retrofitBuilder.build();
                }
            }
        }
        return retrofit;

    }

    @Override
    public OkHttpClient.Builder setInterceptor(Interceptor interceptor) {

        return httpBuilder.addInterceptor(interceptor);

    }

    @Override
    public Retrofit.Builder setConverterFactory(Converter.Factory factory) {

        return retrofitBuilder.addConverterFactory(factory);

    }

    /**
     * 设置okHttpClient
     *
     * @return
     */
    public OkHttpClient okHttpClient() {
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        return new OkHttpClient.Builder()
                .addInterceptor(getLoggerInterceptor())
                .addInterceptor(getHeaderInterceptor())
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .hostnameVerifier((hostname, session) -> true)
                .connectTimeout(GlobalConstants.CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(GlobalConstants.READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(GlobalConstants.WRITE_TIMEOUT, TimeUnit.SECONDS)
                .build();

    }

    /**
     * 头部拦截器
     *
     * @return
     */
    public Interceptor getHeaderInterceptor() {

        return chain -> {
            Request.Builder builder = chain.request().newBuilder();
            builder.addHeader("Content-Type", "application/json; charset=UTF-8");
            builder.addHeader("Accept", "application/json");
            // 语言版本
            builder.addHeader("lang", AppUtils.getSysLanguage());
            // 唯一标识
            builder.addHeader("deviceSn", AppUtils.getUUID());
            // 设备类型
            builder.addHeader("deviceType", "Android");
            // 设备型号
            builder.addHeader("model", AppUtils.getSysModel() + "_" + AppUtils.getSysVersion());
            // 网络类型
            builder.addHeader("net", "");
            // 分辨率
            builder.addHeader("screen", AppUtils.getScreen());
            // app版本
            builder.addHeader("version", AppUtils.getVerName());
            // token
            builder.addHeader("authkey", AccountManager.getInstance().getToken());
            builder.addHeader("longitude", MyApp.getLngLat().get(0));
            builder.addHeader("latitude", MyApp.getLngLat().get(1));
            return chain.proceed(builder.build());

        };

    }

    /**
     * 日志拦截器
     *
     * @return
     */
    public HttpLoggingInterceptor getLoggerInterceptor() {

        return new HttpLoggingInterceptor(message ->
                LogUtils.e("HTTP", "--->" + message)).setLevel(HttpLoggingInterceptor.Level.BODY);

    }

}
