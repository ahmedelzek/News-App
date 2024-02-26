package com.example.newsapp.ui.model

import com.example.newsapp.R

data class Category(
    val id: String,
    val imageId: Int,
    val backgroundColorId: Int,
    val title: String
) {
    companion object {
        val categories = listOf(
            Category(
                id = "sports", imageId = R.drawable.ic_sports,
                backgroundColorId = R.color.red, title = "Sports"
            ),
            Category(
                id = "entertainment", imageId = R.drawable.ic_politics,
                backgroundColorId = R.color.blue, title = "Entertainment"
            ),
            Category(
                id = "health", imageId = R.drawable.ic_health,
                backgroundColorId = R.color.pink, title = "Health"
            ),
            Category(
                id = "business", imageId = R.drawable.ic_bussines,
                backgroundColorId = R.color.orange, title = "Business"
            ),
            Category(
                id = "technology", imageId = R.drawable.ic_environment,
                backgroundColorId = R.color.light_blue, title = "Technology"
            ),
            Category(
                id = "science", imageId = R.drawable.ic_science,
                backgroundColorId = R.color.yellow, title = "Science"
            ),
        )
    }
}