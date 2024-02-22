package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.newsapp.Constants
import com.example.newsapp.adapter.NewsAdapter
import com.example.newsapp.api.ApiManger
import com.example.newsapp.api.model.ArticlesResponse
import com.example.newsapp.api.model.Source
import com.example.newsapp.api.model.SourcesResponse
import com.example.newsapp.databinding.FragmentNewsBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsFragment : Fragment(), OnTabSelectedListener {


    private lateinit var binding: FragmentNewsBinding
    private var newsAdapter = NewsAdapter(listOf())
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadSources()
        handleClicks()
        binding.recyclerViewNews.adapter = newsAdapter
    }

    private fun handleClicks() {
        binding.errorView.retryButton.setOnClickListener {
            loadSources()
        }
        binding.tapLayout.addOnTabSelectedListener(this)
    }

    private fun loadSources() {
        showProgressbarVisibility(true)
        showErrorVisibility(false)
        ApiManger.getWebServices().getSources(ApiManger.apiKey)
            .enqueue(object : Callback<SourcesResponse> {
                override fun onResponse(
                    call: Call<SourcesResponse>,
                    response: Response<SourcesResponse>
                ) {
                    showProgressbarVisibility(false)
                    if (response.isSuccessful) {
                        response.body()?.sources.let {
                            showTaps(it!!)
                        }
                    } else {
                        val responseGson = Gson().fromJson(
                            response.errorBody()?.string(),
                            SourcesResponse::class.java
                        )
                        showErrorVisibility(true, responseGson.message ?: Constants.ERROR_MESSAGE)
                    }

                }

                override fun onFailure(call: Call<SourcesResponse>, t: Throwable) {
                    showProgressbarVisibility(false)
                    t.localizedMessage?.let { showErrorVisibility(true, it) }
                }
            })
    }

    private fun showTaps(sources: List<Source?>) {
        sources.forEach { source ->
            val tab = binding.tapLayout.newTab()
            tab.text = source?.name
            binding.tapLayout.addTab(tab)
            tab.tag = source
        }
        binding.tapLayout.getTabAt(0)?.select()
    }

    private fun showErrorVisibility(isVisible: Boolean, message: String = "") {
        binding.errorView.root.isVisible = isVisible
        if (isVisible)
            binding.errorView.errorText.text = message
    }

    private fun showProgressbarVisibility(isVisible: Boolean) {
        binding.progressBar.isVisible = isVisible
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        val source = tab?.tag as Source?
        source?.id?.let {
            loadArticles(it)
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {}

    override fun onTabReselected(tab: TabLayout.Tab?) {
        val source = tab?.tag as Source?
        source?.id?.let {
            loadArticles(it)
        }
    }

    private fun loadArticles(sourceId: String) {
        ApiManger.getWebServices().getArticles(ApiManger.apiKey, sourceId)
            .enqueue(object : Callback<ArticlesResponse> {
                override fun onResponse(
                    call: Call<ArticlesResponse>,
                    response: Response<ArticlesResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.articles.let {
                            newsAdapter.updateArticles(it!!)
                        }
                    } else {
                        val responseGson = Gson().fromJson(
                            response.errorBody()?.string(),
                            ArticlesResponse::class.java
                        )
                        showErrorVisibility(true, responseGson.message ?: Constants.ERROR_MESSAGE)
                    }

                }

                override fun onFailure(call: Call<ArticlesResponse>, t: Throwable) {
                    showProgressbarVisibility(false)
                    t.localizedMessage?.let { showErrorVisibility(true, it) }
                }

            })
    }
}