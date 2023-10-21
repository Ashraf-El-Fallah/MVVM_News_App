package com.af.newsapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.af.newsapp.db.ArticleDatabase
import com.af.newsapp.repository.NewsRepository

class NewsViewModelFactory(
    val newsRepository: NewsRepository
):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(newsRepository) as T
    }
}