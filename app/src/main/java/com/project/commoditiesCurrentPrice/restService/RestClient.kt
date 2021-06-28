package com.project.commoditiesCurrentPrice.restService

import com.project.commoditiesCurrentPrice.BuildConfig
import com.project.commoditiesCurrentPrice.utils.Constants
import com.project.commoditiesCurrentPrice.utils.Constants.Companion.REQUEST_TIME_OUT
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RestClient {

    val restApi : RestApi
    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL).client(getHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
        restApi = retrofit.create(RestApi::class.java)
    }

    private fun getHttpClient() : OkHttpClient {
        val client = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()

        if(BuildConfig.DEBUG) {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY;
        } else {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.NONE;
        }
        //add interceptor
        client.addInterceptor(loggingInterceptor);

        client.readTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS);
        client.connectTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS);

        return client.build();
    }

}
