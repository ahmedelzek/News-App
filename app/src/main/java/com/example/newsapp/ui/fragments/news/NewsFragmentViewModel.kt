package com.example.newsapp.ui.fragments.news

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsapp.Constants
import com.example.newsapp.api.ApiManger
import com.example.newsapp.api.model.Article
import com.example.newsapp.api.model.ArticlesResponse
import com.example.newsapp.api.model.Source
import com.example.newsapp.api.model.SourcesResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsFragmentViewModel : ViewModel() {
    val sourcesListLiveData: MutableLiveData<List<Source?>?> = MutableLiveData()
    val isLoadingLiveData: MutableLiveData<Boolean> = MutableLiveData(false)
    val errorMessageLiveData: MutableLiveData<String> = MutableLiveData("")
    val articlesListLiveData: MutableLiveData<List<Article?>?> = MutableLiveData()

    fun loadSources(categoryId: String) {
        isLoadingLiveData.value = true
        errorMessageLiveData.value = ""
        ApiManger.getWebServices().getSources(Constants.API_KEY, categoryId)
            .enqueue(object : Callback<SourcesResponse> {
                override fun onResponse(
                    call: Call<SourcesResponse>,
                    response: Response<SourcesResponse>
                ) {
                    isLoadingLiveData.value = false
                    if (response.isSuccessful) {
                        response.body()?.sources.let {
                            sourcesListLiveData.value = it!!
                        }

                    } else {
                        val gsonResponse =
                            Gson().fromJson(
                                response.errorBody()?.string(),
                                SourcesResponse::class.java
                            )
                        errorMessageLiveData.value = gsonResponse.message ?: "Try again later"
                    }
                }

                override fun onFailure(call: Call<SourcesResponse>, t: Throwable) {
                    isLoadingLiveData.value = false
                    errorMessageLiveData.value = t.localizedMessage ?: "Try again later"
                }
            })
    }

    fun loadArticles(sourceId: String) {
        ApiManger.getWebServices().getArticles(
            Constants.API_KEY,
            sourceId
        ).enqueue(object : Callback<ArticlesResponse> {
            override fun onResponse(
                call: Call<ArticlesResponse>,
                response: Response<ArticlesResponse>
            ) {
                if (response.isSuccessful && response.body()?.articles?.isNotEmpty() == true) {
                    articlesListLiveData.value = response.body()?.articles!!
                } else {
                    val gsonResponse =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ArticlesResponse::class.java
                        )

                    errorMessageLiveData.value = gsonResponse.message ?: "Try again later"
                }
            }

            override fun onFailure(call: Call<ArticlesResponse>, t: Throwable) {
                isLoadingLiveData.value = false
                errorMessageLiveData.value = t.localizedMessage ?: "Try again later"
            }
        })
    }
}
