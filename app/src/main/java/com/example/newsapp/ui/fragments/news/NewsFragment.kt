package com.example.newsapp.ui.fragments.news

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.Constants
import com.example.newsapp.api.model.Article
import com.example.newsapp.api.model.Source
import com.example.newsapp.databinding.FragmentNewsBinding
import com.example.newsapp.ui.activities.DetailsActivity
import com.example.newsapp.ui.adapter.NewsAdapter
import com.example.newsapp.ui.model.Category
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

class NewsFragment(
    private val categoryId: Category,
    private val onResume: () -> Unit
) : Fragment(), OnTabSelectedListener,
    NewsAdapter.OnArticleClickListener {

    private lateinit var binding: FragmentNewsBinding
    private lateinit var viewModel: NewsFragmentViewModel
    private var newsAdapter = NewsAdapter(listOf())
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[NewsFragmentViewModel::class.java]
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadSources(categoryId.id)
        handleClicks()
        binding.recyclerViewNews.adapter = newsAdapter
        observeToLiveData()
    }

    override fun onResume() {
        super.onResume()
        onResume.invoke()
    }

    private fun handleClicks() {
        binding.errorView.retryButton.setOnClickListener {
            viewModel.loadSources(categoryId.id)
        }
        binding.tabLayout.addOnTabSelectedListener(this)
        newsAdapter.setOnArticleClickListener(this)
    }

    private fun observeToLiveData() {
        viewModel.sourcesListLiveData.observe(viewLifecycleOwner) {
            showTaps(it!!)
        }
        viewModel.isLoadingLiveData.observe(viewLifecycleOwner) {
            showProgressbarVisibility(it!!)
        }
        viewModel.errorMessageLiveData.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                showErrorVisibility(false)
            } else {
                showErrorVisibility(true, it)
            }
        }
        viewModel.articlesListLiveData.observe(viewLifecycleOwner) {
            newsAdapter.updateArticles(it!!)
        }
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
            viewModel.loadArticles(it)
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {}

    override fun onTabReselected(tab: TabLayout.Tab?) {
        val source = tab?.tag as Source?
        source?.id?.let {
            viewModel.loadArticles(it)
        }
    }

    override fun onArticleClick(article: Article?) {
        val intent = Intent(requireActivity(), DetailsActivity::class.java)
        intent.putExtra(Constants.ARTICLE_KEY, article)
        startActivity(intent)
    }
}