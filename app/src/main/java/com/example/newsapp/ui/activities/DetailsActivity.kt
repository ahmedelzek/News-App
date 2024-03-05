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
        binding.apply {
            articleTime.text = article?.publishedAt
            articleTitle.text = article?.description
            sourceTv.text = article?.source?.name
            content.text = article?.content
            sourceBtn.setOnClickListener {
                startActivity(Intent(Intent(Intent.ACTION_VIEW, Uri.parse("${article?.url}"))))
            }
        }
        Glide.with(this).load(article?.urlToImage).into(binding.articleImage)
    }

}