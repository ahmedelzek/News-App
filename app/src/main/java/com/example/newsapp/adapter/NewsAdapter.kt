package com.example.newsapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.newsapp.api.model.Article
import com.example.newsapp.databinding.NewsItemBinding

class NewsAdapter(private var articlesList: List<Article?>) :
    Adapter<NewsAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun getItemCount(): Int = articlesList.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = articlesList[position]
        holder.bind(article)
    }

    fun updateArticles(newList: List<Article?>) {
        articlesList = newList
        notifyDataSetChanged()
    }

    class NewsViewHolder(private val binding: NewsItemBinding) : ViewHolder(binding.root) {
        fun bind(article: Article?) {
            binding.apply {
                newAuthorTxt.text = article?.source?.name
                newTitleTxt.text = article?.title
                newTimeTxt.text = article?.publishedAt
                Glide.with(root)
                    .load(article?.urlToImage)
                    .into(binding.newsImg)
            }
        }
    }

}