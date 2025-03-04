package com.alobarproductions.abnamrorepos.data

import com.alobarproductions.abnamrorepos.core.Repo
import com.alobarproductions.abnamrorepos.core.ReposRepository
import com.alobarproductions.abnamrorepos.data.database.RepoEntity
import com.alobarproductions.abnamrorepos.data.database.ReposDao
import com.alobarproductions.abnamrorepos.data.network.GitHubRepoResponse
import com.alobarproductions.abnamrorepos.data.network.GitHubService
import com.alobarproductions.abnamrorepos.data.network.GitHubVisibility

private const val AbnAmroGitHubUsername = "abnamrocoesd"

internal class InternalReposRepository(
    private val gitHubService: GitHubService,
    private val reposDao: ReposDao,
) : ReposRepository {

    override suspend fun getRepos(offset: Int): List<Repo> {
        require(offset >= 0)
        val limit = 10
        return buildList {
            val cachedRepos = reposDao
                .readRepos(offset = offset, limit = limit)
                .map(::toDomain)
            addAll(cachedRepos)
            if (size < limit) {
                val githubPageSize = 10
                val githubPageNumber = (offset / githubPageSize) + 1
                val fetchedRepos = gitHubService.listUserRepos(
                    username = AbnAmroGitHubUsername,
                    pageNumber = githubPageNumber,
                    pageSize = githubPageSize,
                )
                reposDao.insertRepos(
                    repos = fetchedRepos.map(::toDatabase).toTypedArray(),
                )
                val remainderRepos = reposDao.readRepos(
                    offset = offset + size,
                    limit = limit - size,
                )
                addAll(remainderRepos.map(::toDomain))
            }
        }
    }

    override suspend fun getById(repoId: Long): Repo {
        val result = reposDao.readRepoById(repoId)
        checkNotNull(result) { "Invalid repo ID: $repoId" }
        return toDomain(result)
    }

    override suspend fun deleteAll() {
        reposDao.deleteAll()
    }

    private fun toDomain(entity: RepoEntity): Repo {
        return Repo(
            id = entity.id,
            name = entity.name,
            fullName = entity.fullName,
            description = entity.description,
            ownerAvatarUrl = entity.ownerAvatarUrl,
            visibility = toDomain(entity.visibility),
            isPrivate = entity.isPrivate,
            htmlUrl = entity.htmlUrl,
        )
    }

    private fun toDomain(value: RepoEntity.Visibility): Repo.Visibility = when (value) {
        RepoEntity.Visibility.Private -> Repo.Visibility.Private
        RepoEntity.Visibility.Public -> Repo.Visibility.Public
        RepoEntity.Visibility.Unset -> Repo.Visibility.Unset
    }

    private fun toDatabase(value: GitHubRepoResponse) = RepoEntity(
        id = value.id,
        name = value.name,
        fullName = value.fullName,
        description = value.description,
        ownerAvatarUrl = value.owner.avatarUrl,
        visibility = toDatabase(value.visibility),
        isPrivate = value.private,
        htmlUrl = value.htmlUrl,
    )

    private fun toDatabase(value: GitHubVisibility?) = when (value) {
        GitHubVisibility.Public -> RepoEntity.Visibility.Public
        GitHubVisibility.Private -> RepoEntity.Visibility.Private
        null -> RepoEntity.Visibility.Unset
    }
}
