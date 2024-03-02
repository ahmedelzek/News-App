package com.example.newsapp.api

import com.example.newsapp.api.model.ArticlesResponse
import com.example.newsapp.api.model.SourcesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WebServices {
    @GET("/v2/top-headlines/sources")
    fun getSources(
        @Query("apiKey") apiKey: String,
        @Query("category") categoryId: String
    ): Call<SourcesResponse>

    @GET("/v2/everything")
    fun getArticles(
        @Query("apiKey") apiKey: String,
        @Query("sources") sourceId: String
    ): Call<ArticlesResponse>

    @GET("/v2/top-headlines/sources")
    fun getArticlesForSearch(
        @Query("apiKey") apiKey: String,
        @Query("q") query: String
    ): Call<ArticlesResponse>
}