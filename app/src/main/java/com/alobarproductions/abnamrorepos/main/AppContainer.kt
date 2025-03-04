package com.alobarproductions.abnamrorepos.main

import android.app.Application
import com.alobarproductions.abnamrorepos.BuildConfig
import com.alobarproductions.abnamrorepos.core.ReposRepository
import com.alobarproductions.abnamrorepos.data.InternalReposRepository
import com.alobarproductions.abnamrorepos.data.database.ReposDatabase
import com.alobarproductions.abnamrorepos.data.network.GitHubServiceFactory

class AppContainer(
    private val application: Application,
) {

    val reposRepository: ReposRepository by lazy {
        InternalReposRepository(
            gitHubService = gitHubService,
            reposDao = reposDatabase.reposDao(),
        )
    }

    private val gitHubService by lazy {
        GitHubServiceFactory(
            baseUrl = BuildConfig.GITHUB_BASE_URL,
            token = BuildConfig.GITHUB_TOKEN,
            userAgent = UserAgent.value,
            logHttpTraffic = BuildConfig.DEBUG,
        ).create()
    }

    private val reposDatabase by lazy {
        ReposDatabase.create(application)
    }
}
