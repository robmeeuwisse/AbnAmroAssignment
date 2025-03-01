package com.alobarproductions.abnamrorepos.data.network

import okhttp3.Interceptor
import okhttp3.Response

class UserAgentHeaderInterceptor(
    private val userAgent: String,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .header("User-Agent", userAgent)
            .build()
        return chain.proceed(newRequest)
    }
}
