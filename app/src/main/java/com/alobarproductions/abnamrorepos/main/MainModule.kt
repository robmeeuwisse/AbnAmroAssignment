package com.alobarproductions.abnamrorepos.main

import com.alobarproductions.abnamrorepos.BuildConfig
import com.alobarproductions.abnamrorepos.core.ReposRepository
import com.alobarproductions.abnamrorepos.data.InternalReposRepository
import com.alobarproductions.abnamrorepos.data.network.GitHubServiceFactory

object MainModule {

    private val gitHubService = GitHubServiceFactory(
        baseUrl = BuildConfig.GITHUB_BASE_URL,
        token = BuildConfig.GITHUB_TOKEN,
        userAgent = UserAgent.value,
        logHttpTraffic = BuildConfig.DEBUG,
    ).create()

    val reposRepository: ReposRepository = InternalReposRepository(gitHubService)
}