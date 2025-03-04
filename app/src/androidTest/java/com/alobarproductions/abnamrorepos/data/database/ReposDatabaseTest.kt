package com.alobarproductions.abnamrorepos.data.database

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ReposDatabaseTest {

    private lateinit var database: ReposDatabase
    private lateinit var reposDao: ReposDao

    @Before
    fun setup() {
        database = ReposDatabase.createInMemory(ApplicationProvider.getApplicationContext())
        reposDao = database.reposDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun round_trip() = runTest {
        val repo1 = RepoEntity(
            id = 1,
            name = "Repo-One",
            fullName = "Group/Repo-One",
            description = "Repository One",
            ownerAvatarUrl = "https://example.com/users/1/avatar.png",
            visibility = RepoEntity.Visibility.Private,
            isPrivate = true,
            htmlUrl = "https://example.com/repos/1/",
        )

        val repo2 = RepoEntity(
            id = 2,
            name = "Repo-Two",
            fullName = "Group/Repo-Two",
            description = "Repository Two",
            ownerAvatarUrl = "https://example.com/users/2/avatar.png",
            visibility = RepoEntity.Visibility.Public,
            isPrivate = false,
            htmlUrl = "https://example.com/repos/2/",
        )

        reposDao.insertRepos(repo1, repo2)

        val actual1 = reposDao.readRepoById(repo1.id)
        assertTrue(actual1 == repo1)
        assertFalse(actual1 === repo1)

        val actual2 = reposDao.readRepoById(repo2.id)
        assertTrue(actual2 == repo2)
        assertFalse(actual2 === repo2)

        val actual3 = reposDao.readRepoById(repoId = 3)
        assertNull(actual3)
    }

    @Test
    fun read_repos_sorts_by_lowercase_full_name() = runTest {
        val repos = listOf("Date", "cherry", "Banana", "apple")
            .mapIndexed { index, fullName ->
                RepoEntity(
                    id = index.toLong(),
                    name = "Repo-$index",
                    fullName = fullName,
                    description = null,
                    ownerAvatarUrl = "https://example.com/avatar.png",
                    visibility = RepoEntity.Visibility.Public,
                    isPrivate = false,
                    htmlUrl = "https://example.com/repos/$index/",
                )
            }.toTypedArray()
        reposDao.insertRepos(*repos)

        val actualRepos = reposDao.readRepos(0, Int.MAX_VALUE)

        val actualFullNames = actualRepos.map { it.fullName }
        val expectedFullNames = listOf("apple", "Banana", "cherry", "Date")
        assertEquals(expectedFullNames, actualFullNames)
    }

    @Test
    fun upsert() = runTest {
        val initialRepo1 = RepoEntity(
            id = 1,
            name = "Repo-Initial",
            fullName = "Group/Repo-Initial",
            description = "Repository Initial",
            ownerAvatarUrl = "https://example.com/users/initial/avatar.png",
            visibility = RepoEntity.Visibility.Private,
            isPrivate = true,
            htmlUrl = "https://example.com/repos/initial/",
        )

        val updatedRepo1 = RepoEntity(
            id = 1,
            name = "Repo-Updated",
            fullName = "Group/Repo-Updated",
            description = null,
            ownerAvatarUrl = "https://example.com/users/updated/avatar.png",
            visibility = RepoEntity.Visibility.Public,
            isPrivate = false,
            htmlUrl = "https://example.com/repos/updated/",
        )

        reposDao.insertRepos(initialRepo1)
        reposDao.insertRepos(updatedRepo1)

        val actual = reposDao.readRepos(offset = 0, limit = Int.MAX_VALUE)

        val expected = listOf(updatedRepo1)
        assertEquals(expected, actual)
    }

    @Test
    fun query_paged() = runTest {
        val repos = Array(10) { repoId ->
            RepoEntity(
                id = repoId.toLong(),
                name = "Repo-$repoId",
                fullName = "Group/Repo-$repoId",
                description = if (repoId % 2 == 0) "Repository $repoId" else null,
                ownerAvatarUrl = "https://example.com/users/$repoId/avatar.png",
                visibility = if (repoId % 2 == 0) RepoEntity.Visibility.Public else RepoEntity.Visibility.Private,
                isPrivate = repoId % 2 == 0,
                htmlUrl = "https://example.com/repos/$repoId/",
            )
        }

        reposDao.insertRepos(*repos)

        assertEquals(
            emptyList<RepoEntity>(),
            reposDao.readRepos(offset = 0, limit = 0)
        )
        assertEquals(
            listOf(repos[0]),
            reposDao.readRepos(offset = 0, limit = 1)
        )
        assertEquals(
            listOf(repos[0], repos[1]),
            reposDao.readRepos(offset = 0, limit = 2)
        )
        assertEquals(
            listOf(repos[0], repos[1], repos[2]),
            reposDao.readRepos(offset = 0, limit = 3)
        )
        assertEquals(
            listOf(repos[7], repos[8], repos[9]),
            reposDao.readRepos(offset = 7, limit = 3)
        )
        assertEquals(
            listOf(repos[8], repos[9]),
            reposDao.readRepos(offset = 8, limit = 3)
        )
        assertEquals(
            listOf(repos[9]),
            reposDao.readRepos(offset = 9, limit = 3)
        )
        assertEquals(
            emptyList<RepoEntity>(),
            reposDao.readRepos(offset = 10, limit = 3)
        )
    }
}
