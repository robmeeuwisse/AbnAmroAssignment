package com.alobarproductions.abnamrorepos.data

import com.alobarproductions.abnamrorepos.data.database.RepoEntity
import com.alobarproductions.abnamrorepos.data.database.ReposDao
import com.alobarproductions.abnamrorepos.data.network.GitHubRepoOwner
import com.alobarproductions.abnamrorepos.data.network.GitHubRepoResponse
import com.alobarproductions.abnamrorepos.data.network.GitHubService
import com.alobarproductions.abnamrorepos.data.network.GitHubVisibility
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class InternalReposRepositoryTest {

    private val gitHubService: GitHubService = mockk()
    private val reposDao: ReposDao = mockk()

    private val repository = InternalReposRepository(gitHubService, reposDao)

    @Test
    fun `read from cache`() = runTest {
        val entity1 = createRepoEntity(1)
        val entity2 = createRepoEntity(2)

        coEvery {
            reposDao.readRepos(any(), any())
        } returns listOf(entity1, entity2)

        val actual = repository.getRepos(offset = 0, limit = 2)

        assertEquals(2, actual.size)
        coVerify { reposDao.readRepos(offset = 0, limit = 2) }
        coVerify(exactly = 0) { gitHubService.listUserRepos(any(), any(), any()) }
        coVerify(exactly = 0) { reposDao.insertRepos(any()) }
    }

    @Test
    fun `read from cache, append from network`() = runTest {
        val entity3 = createRepoEntity(3)
        val entity4 = createRepoEntity(4)
        val entity5 = createRepoEntity(5)
        val entity6 = createRepoEntity(6)
        val entity7 = createRepoEntity(7)
        val entity8 = createRepoEntity(8)

        val github5 = createGitHubRepo(5)
        val github6 = createGitHubRepo(6)
        val github7 = createGitHubRepo(7)
        val github8 = createGitHubRepo(8)

        coEvery {
            reposDao.readRepos(offset = 2, limit = 4)
        } returns listOf(entity3, entity4)

        coEvery {
            gitHubService.listUserRepos(username = any(), pageNumber = 2, pageSize = 4)
        } returns listOf(github5, github6, github7, github8)

        coEvery {
            reposDao.insertRepos(entity5, entity6, entity7, entity8)
        } just runs

        coEvery {
            reposDao.readRepos(offset = 4, limit = 2)
        } returns listOf(entity5, entity6)

        val actual = repository.getRepos(offset = 2, limit = 4)

        assertEquals(4, actual.size)
        coVerifySequence {
            reposDao.readRepos(offset = 2, limit = 4)
            gitHubService.listUserRepos(username = any(), pageNumber = 2, pageSize = 4)
            reposDao.insertRepos(entity5, entity6, entity7, entity8)
            reposDao.readRepos(offset = 4, limit = 2)
        }
    }

    private fun createGitHubRepo(id: Int) = GitHubRepoResponse(
        id = id.toLong(),
        name = "Repo-$id",
        fullName = "Group/Repo-$id",
        description = "Repository $id",
        owner = GitHubRepoOwner(
            avatarUrl = "https://example.com/users/2/avatar.png"
        ),
        visibility = GitHubVisibility.Public,
        private = false,
        htmlUrl = "https://example.com/repos/$id/",
    )

    private fun createRepoEntity(id: Int) = RepoEntity(
        id = id.toLong(),
        name = "Repo-$id",
        fullName = "Group/Repo-$id",
        description = "Repository $id",
        ownerAvatarUrl = "https://example.com/users/2/avatar.png",
        visibility = RepoEntity.Visibility.Public,
        isPrivate = false,
        htmlUrl = "https://example.com/repos/$id/",
    )
}
