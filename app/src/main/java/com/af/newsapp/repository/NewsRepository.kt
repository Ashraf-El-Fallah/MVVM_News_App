package com.af.newsapp.repository

import com.af.newsapp.data.api.RetrofitInstance
import com.af.newsapp.data.db.ArticleDatabase
import com.af.newsapp.data.models.Article
import com.af.newsapp.data.models.NewsResponse
import retrofit2.Response
import retrofit2.http.Query

class NewsRepository(
    val db: ArticleDatabase
) {

    // i add this Response<NewsResponse>
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int): Response<NewsResponse> {
        return RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)
    }

    suspend fun searchForNews(searchQuery: String, pageNumber: Int): Response<NewsResponse> {
        return RetrofitInstance.api.searchForNews(searchQuery, pageNumber)
    }

    suspend fun upsert(article: Article?) = db.getArticleDao().upsert(article)
    fun getSavedNews() = db.getArticleDao().getAllArticles()
    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}