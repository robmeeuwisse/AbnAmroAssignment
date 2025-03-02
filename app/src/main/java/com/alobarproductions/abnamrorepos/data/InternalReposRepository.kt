package com.alobarproductions.abnamrorepos.data

import com.alobarproductions.abnamrorepos.core.Repo
import com.alobarproductions.abnamrorepos.core.ReposRepository
import com.alobarproductions.abnamrorepos.data.network.GitHubService
import com.alobarproductions.abnamrorepos.data.network.GitHubVisibility

private const val AbnAmroUserName = "abnamrocoesd"

internal class InternalReposRepository(
    private val gitHubService: GitHubService,
) : ReposRepository {

    override suspend fun getByPage(page: Int): List<Repo> {
        val result = gitHubService.listUserRepos(AbnAmroUserName, page)
        return result.map {
            Repo(
                id = it.id,
                name = it.name,
                fullName = it.name,
                description = it.description,
                ownerAvatarUrl = it.owner.avatarUrl,
                visibility = when (it.visibility) {
                    GitHubVisibility.Public -> Repo.Visibility.Public
                    GitHubVisibility.Private -> Repo.Visibility.Private
                    null -> Repo.Visibility.Unknown
                },
                isPrivate = it.private,
                htmlUrl = it.htmlUrl,
            )
        }
    }

    override suspend fun getById(repoId: Long): Repo {
        val result = getByPage(1).find { it.id == repoId }
        return requireNotNull(result) { "Invalid GitHub repo ID: $repoId" }
    }
}
