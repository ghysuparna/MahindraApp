package com.example.mahindraassignmentapp.data.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object ApiClient {

    fun makeOkHttpClient(): OkHttpClient {

        return OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .apply {
                networkInterceptors().add(Interceptor { chain->
                    val original = chain.request()
                    val request = original.newBuilder()
                        .method(original.method, original.body)
                        .build()
                    return@Interceptor chain.proceed(request)
                })
                addInterceptor(makeLoggingInterceptor())
            }.build()
    }

    private fun makeLoggingInterceptor(): HttpLoggingInterceptor {

        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    }
}