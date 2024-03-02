package com.example.newsapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.newsapp.Constants
import com.example.newsapp.adapter.NewsAdapter
import com.example.newsapp.api.ApiManger
import com.example.newsapp.api.model.Article
import com.example.newsapp.api.model.ArticlesResponse
import com.example.newsapp.api.model.Source
import com.example.newsapp.api.model.SourcesResponse
import com.example.newsapp.databinding.FragmentNewsBinding
import com.example.newsapp.ui.activities.DetailsActivity
import com.example.newsapp.ui.model.Category
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsFragment(
    private val categoryId: Category,
    private val onResume: () -> Unit
) : Fragment(), OnTabSelectedListener,
    NewsAdapter.OnArticleClickListener {


    private lateinit var binding: FragmentNewsBinding
    private var newsAdapter = NewsAdapter(listOf())
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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

    override fun onResume() {
        super.onResume()
        onResume.invoke()
    }

    private fun handleClicks() {
        binding.errorView.retryButton.setOnClickListener {
            loadSources()
        }
        binding.tabLayout.addOnTabSelectedListener(this)
        newsAdapter.setOnArticleClickListener(this)
    }

    private fun loadSources() {
        showProgressbarVisibility(true)
        showErrorVisibility(false)
        ApiManger.getWebServices().getSources(ApiManger.apiKey, categoryId.id)
            .enqueue(object : Callback<SourcesResponse> {
                override fun onResponse(
                    call: Call<SourcesResponse>, response: Response<SourcesResponse>
                ) {
                    showProgressbarVisibility(false)
                    if (response.isSuccessful) {
                        response.body()?.sources.let {
                            showTaps(it!!)
                        }
                    } else {
                        val responseGson = Gson().fromJson(
                            response.errorBody()?.string(), SourcesResponse::class.java
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
            val tab = binding.tabLayout.newTab()
            tab.text = source?.name
            binding.tabLayout.addTab(tab)
            tab.tag = source
        }
        binding.tabLayout.getTabAt(0)?.select()
        tabMargin()
    }

    private fun tabMargin() {
        val tabs = binding.tabLayout.getChildAt(0) as ViewGroup
        tabs.forEach {
            val layoutParams = it.layoutParams as LinearLayout.LayoutParams
            layoutParams.marginStart = 20
            it.layoutParams = layoutParams
            binding.tabLayout.requestLayout()
        }
    }

    private fun showErrorVisibility(isVisible: Boolean, message: String = "") {
        binding.errorView.root.isVisible = isVisible
        if (isVisible) binding.errorView.errorText.text = message
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
                    call: Call<ArticlesResponse>, response: Response<ArticlesResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.articles.let {
                            newsAdapter.updateArticles(it!!)
                        }
                    } else {
                        val responseGson = Gson().fromJson(
                            response.errorBody()?.string(), ArticlesResponse::class.java
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

    override fun onArticleClick(article: Article?) {
        val intent = Intent(requireActivity(), DetailsActivity::class.java)
        intent.putExtra(Constants.ARTICLE_KEY, article)
        startActivity(intent)
    }
}