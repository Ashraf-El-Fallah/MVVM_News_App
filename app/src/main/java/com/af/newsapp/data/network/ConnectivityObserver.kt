package com.af.newsapp.data.network

import kotlinx.coroutines.flow.Flow


interface ConnectivityObserver {
    fun observe(): Flow<NetworkStatus>
    enum class NetworkStatus {
        Available, Unavailable, Losing, Lost
    }
}