package com.example.newsapp.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.newsapp.Constants
import com.example.newsapp.api.model.Article
import com.example.newsapp.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        getArticle()
    }

    private fun getArticle() {
        val article = intent.getSerializableExtra(Constants.ARTICLE_KEY) as? Article
        binding.articleTime.text = article?.publishedAt
        binding.articleTitle.text = article?.description
        Glide.with(this).load(article?.urlToImage).into(binding.articleImage)
        binding.sourceTv.text = article?.source?.name
        binding.content.text = article?.content
        binding.sourceBtn.setOnClickListener {
            startActivity(Intent(Intent(Intent.ACTION_VIEW, Uri.parse("${article?.url}"))))
        }
    }

}