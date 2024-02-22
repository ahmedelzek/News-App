package com.example.newsapp.api

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiManger {
    private var retrofit: Retrofit? = null

    const val apiKey = "2c76bfed999f44c38aacfe4f49f34c7c"
    fun getWebServices(): WebServices {
        if (retrofit == null) {
            val loggingInterceptor = HttpLoggingInterceptor {
                Log.e("ApiManger", "Body: $it")
            }
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl("https://newsapi.org")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        }
        return retrofit!!.create(WebServices::class.java)
    }
}