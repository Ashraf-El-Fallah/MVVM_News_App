package com.af.newsapp.repository

import com.af.newsapp.api.RetrofitInstance
import com.af.newsapp.db.ArticleDatabase
import com.af.newsapp.models.NewsResponse
import retrofit2.Response

class NewsRepository(
    val db: ArticleDatabase
) {

    // i add this Response<NewsResponse>
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int): Response<NewsResponse> {
        return RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)
    }
}