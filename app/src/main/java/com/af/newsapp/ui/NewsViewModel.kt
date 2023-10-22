package com.af.newsapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.af.newsapp.models.NewsResponse
import com.af.newsapp.repository.NewsRepository
import com.af.newsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    val newsRepository: NewsRepository
) : ViewModel() {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1


    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val searchNewPage = 1

    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())
        val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
        val result = handleBreakingNewsResponse(response)
        breakingNews.postValue(result)
    }

    fun searchForNews(searchQuery: String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val response=newsRepository.searchForNews(searchQuery,searchNewPage)
        val result=handleSearchNewsResponse(response)
        searchNews.postValue(result)
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            //The code block inside the let function is executed when the response body is not null.
            //The body() method typically contains the deserialized data from the HTTP response.
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            //The code block inside the let function is executed when the response body is not null.
            //The body() method typically contains the deserialized data from the HTTP response.
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


}