package com.pai8.ke.groupBuy.http

import com.pai8.ke.app.MyApp
import com.pai8.ke.base.retrofit.BaseApiImpl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 *   @auther : lihongshi
 *   time   : 2021/03/27
 */
class RetrofitClient {

    companion object {
        fun getInstance() = SingletonHolder.INSTANCE
        private lateinit var retrofit: Retrofit
    }

    private object SingletonHolder {
        val INSTANCE = RetrofitClient()
    }

    init {
        retrofit = Retrofit.Builder()
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create()) // 添加Gson转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(MyApp.getHttpBaseUrl())
                .build()
    }

    private fun getOkHttpClient(): OkHttpClient {
//        return OkHttpClient.Builder()
//                .connectTimeout(20L, TimeUnit.SECONDS)
//                .addNetworkInterceptor(InterceptorUtil.getHttpLoggingInterceptor())
//                .addInterceptor(InterceptorUtil.getHttpHeaderInterceptor())
//                .addInterceptor(InterceptorUtil.getEncryptInterceptor())
//                .writeTimeout(20L, TimeUnit.SECONDS)
//                .connectionPool(ConnectionPool(8, 15, TimeUnit.SECONDS))
//                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), SSLSocketClient.getX509TrustManager())
//                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
//                .build()

        return BaseApiImpl.okHttpClient()
    }

    private fun <T> create(service: Class<T>?): T =
            retrofit.create(service!!) ?: throw RuntimeException("Api service is null!")


    fun getMainService(): GroupBuyApiService {
        return mApiService
    }

    private val mApiService by lazy { getInstance().create(GroupBuyApiService::class.java) }

}