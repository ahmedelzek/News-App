package com.example.newsapp.ui.fragments.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsapp.Constants
import com.example.newsapp.api.ApiManger
import com.example.newsapp.api.model.Article
import com.example.newsapp.api.model.ArticlesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragmentViewModel : ViewModel() {
    var articleListLiveData: MutableLiveData<List<Article?>> = MutableLiveData(listOf())
    fun articleSearchInAPI(text: String) {
        ApiManger.getWebServices().getArticles(
            apiKey = Constants.API_KEY,
            searchKey = text
        ).enqueue(object : Callback<ArticlesResponse> {
            override fun onResponse(
                call: Call<ArticlesResponse>,
                response: Response<ArticlesResponse>
            ) {
                if (response.isSuccessful && response.body()?.articles?.isNotEmpty() == true) {
                    articleListLiveData.value = response.body()?.articles!!
                }
            }

            override fun onFailure(call: Call<ArticlesResponse>, t: Throwable) {}
        })
    }

}
