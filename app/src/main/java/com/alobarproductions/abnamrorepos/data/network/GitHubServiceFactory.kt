package com.alobarproductions.abnamrorepos.data.network

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class GitHubServiceFactory(
    private val baseUrl: String,
    private val userAgent: String,
    private val logHttpTraffic: Boolean = false,
) {

    fun create(): GitHubService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(GitHubService::class.java)

    private val client by lazy {
        OkHttpClient.Builder()
            .addInterceptor(UserAgentHeaderInterceptor(userAgent))
            .apply {
                if (logHttpTraffic) {
                    addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                }
            }
            .build()
    }

    private val json by lazy {
        Json {
            ignoreUnknownKeys = true
        }
    }
}
