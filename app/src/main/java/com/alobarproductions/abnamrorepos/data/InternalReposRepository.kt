package com.alobarproductions.abnamrorepos.data

import com.alobarproductions.abnamrorepos.core.Repo
import com.alobarproductions.abnamrorepos.core.ReposRepository
import com.alobarproductions.abnamrorepos.data.network.GitHubService
import com.alobarproductions.abnamrorepos.data.network.GitHubVisibility
import java.net.URL

private const val AbnAmroUserName = "abnamrocoesd"

internal class InternalReposRepository(
    private val gitHubService: GitHubService,
) : ReposRepository {
    override suspend fun getAll(): List<Repo> {
        val result = gitHubService.listUserRepos(AbnAmroUserName)
        return result.map {
            Repo(
                name = it.name,
                ownerAvatarUrl = it.owner.avatarUrl,
                visibility = when (it.visibility) {
                    GitHubVisibility.Public -> Repo.Visibility.Public
                    GitHubVisibility.Private -> Repo.Visibility.Private
                    null -> Repo.Visibility.Unknown
                },
                isPrivate = it.private,
            )
        }
    }
}