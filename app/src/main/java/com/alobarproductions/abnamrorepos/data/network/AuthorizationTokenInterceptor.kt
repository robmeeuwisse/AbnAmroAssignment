package com.alobarproductions.abnamrorepos.data.network

import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationTokenInterceptor(
    private val token: String,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .header("Authorization", "Bearer $token")
            .build()
        return chain.proceed(newRequest)
    }
}
