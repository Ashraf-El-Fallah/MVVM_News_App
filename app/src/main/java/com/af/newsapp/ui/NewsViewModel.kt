package com.af.newsapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_ETHERNET
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.ContactsContract.CommonDataKinds.Email.TYPE_MOBILE
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.af.newsapp.NewsApplication
import com.af.newsapp.models.Article
import com.af.newsapp.models.NewsResponse
import com.af.newsapp.network.ConnectivityObserver
import com.af.newsapp.network.NetworkConnectivityObserver
import com.af.newsapp.repository.NewsRepository
import com.af.newsapp.util.Resource
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

class NewsViewModel(
    val newsRepository: NewsRepository,
    application: Application
) : AndroidViewModel(application) {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsResponse: NewsResponse? = null


    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null
    var newSearchQuery: String? = null
    var oldSearchQuery: String? = null

    val context = getApplication<Application>().applicationContext
    lateinit var connectivityObserver: ConnectivityObserver

    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        safeBreakingNewsCall(countryCode)
    }

    fun searchForNews(searchQuery: String) = viewModelScope.launch {
        safeSearchNewsCall(searchQuery)
    }

    fun saveArticle(article: Article?) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }


    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            //The code block inside the let function is executed when the response body is not null.
            //The body() method typically contains the deserialized data from the HTTP response.
            response.body()?.let { resultResponse ->
                //handle pagination

                breakingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                } else {
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            //The code block inside the let function is executed when the response body is not null.
            //The body() method typically contains the deserialized data from the HTTP response.
            response.body()?.let { resultResponse ->
                //handle pagination
                if (searchNewsResponse == null || newSearchQuery != oldSearchQuery) {
                    searchNewsPage = 1
                    oldSearchQuery = newSearchQuery
                    searchNewsResponse = resultResponse
                } else {
                    searchNewsPage++
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun safeSearchNewsCall(searchQuery: String) {
        newSearchQuery = searchQuery
        connectivityObserver = NetworkConnectivityObserver(context)
        connectivityObserver.observe().onEach {
            try {
                if (it.toString() == "Lost" || it.toString() == "Losing" || it.toString() == "Unavailable") {
                    searchNews.postValue(Resource.Error("No internet connection"))
                } else {
                    val response = newsRepository.searchForNews(searchQuery, searchNewsPage)
                    searchNews.postValue(handleSearchNewsResponse(response))
                }
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> searchNews.postValue(Resource.Error("Network failure"))
                    else -> searchNews.postValue(Resource.Error("Conversion Error"))
                }
            }
        }.launchIn(MainScope())
    }

    private suspend fun safeBreakingNewsCall(countryCode: String) {
        connectivityObserver = NetworkConnectivityObserver(context)
        connectivityObserver.observe().onEach {
            try {
                if (it.toString() == "Lost" || it.toString() == "Losing" || it.toString() == "Unavailable") {
                    breakingNews.postValue(Resource.Error("No internet connection"))
                } else {
                    val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
                    breakingNews.postValue(handleBreakingNewsResponse(response))
                }
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> breakingNews.postValue(Resource.Error("Network Failure"))
                    else -> breakingNews.postValue(Resource.Error("Conversion Error"))
                }
            }
        }.launchIn(MainScope())
    }

//    private fun hasInternetConnection(): Boolean {
//        val connectivityManager = getApplication<NewsApplication>().getSystemService(
//            Context.CONNECTIVITY_SERVICE
//        ) as ConnectivityManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            val activeNetwork = connectivityManager.activeNetwork ?: return false
//            val capabilities =
//                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
//            return when {
//                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_SUPL) -> true
//                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_MMS) -> true
//                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_FOTA) -> true
//                else -> false
//            }
//        } else {
//            connectivityManager.activeNetworkInfo?.run {
//                return when (type) {
//                    TYPE_WIFI -> true
//                    TYPE_MOBILE -> true
//                    TYPE_ETHERNET -> true
//                    else -> false
//                }
//            }
//        }
//        return false
//    }
}