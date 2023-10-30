package com.af.newsapp.data.models

import com.af.newsapp.data.models.Article

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)