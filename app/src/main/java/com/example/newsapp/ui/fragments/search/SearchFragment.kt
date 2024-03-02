package com.example.newsapp.ui.fragments.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.Constants
import com.example.newsapp.adapter.NewsAdapter
import com.example.newsapp.api.model.Article
import com.example.newsapp.databinding.FragmentSearchBinding
import com.example.newsapp.ui.activities.DetailsActivity


class SearchFragment(private val cancelClick: (fragment: Fragment) -> Unit) : Fragment(),
    NewsAdapter.OnArticleClickListener {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: NewsAdapter
    private lateinit var viewModel: SearchFragmentViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[SearchFragmentViewModel::class.java]
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeToLiveData()
        newsSearch()
        onCancelSearchIconClick()
    }

    private fun newsSearch() {
        binding.searchBar.searchET.addTextChangedListener {
            viewModel.articleSearchInAPI(binding.searchBar.searchET.text.toString())
        }
    }

    private fun observeToLiveData() {
        viewModel.articleListLiveData.observe(viewLifecycleOwner) {
            updateRecyclerView(it)
        }
    }

    private fun updateRecyclerView(articles: List<Article?>) {
        adapter = NewsAdapter(articles)
        binding.searchRecyclerView.adapter = adapter
    }

    private fun onCancelSearchIconClick() {
        binding.searchBar.cancelSearchBtn.setOnClickListener {
            cancelClick.invoke(this)
        }
    }

    override fun onArticleClick(article: Article?) {
        val intent = Intent(requireActivity(), DetailsActivity::class.java)
        intent.putExtra(Constants.ARTICLE_KEY, article)
        startActivity(intent)
    }
}