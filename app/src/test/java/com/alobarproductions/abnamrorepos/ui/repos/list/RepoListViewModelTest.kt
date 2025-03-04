package com.alobarproductions.abnamrorepos.ui.repos.list

import com.alobarproductions.abnamrorepos.core.Repo
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException

class RepoListViewModelTest {

    private val repo1 = Repo(
        id = 1,
        name = "Repo-One",
        fullName = "Group/Repo-One",
        description = "Repository One",
        ownerAvatarUrl = "https://example.com/users/1/avatar.png",
        visibility = Repo.Visibility.Private,
        isPrivate = true,
        htmlUrl = "https://example.com/repos/1/",
    )

    private val repo2 = Repo(
        id = 2,
        name = "Repo-Two",
        fullName = "Group/Repo-Two",
        description = "Repository Two",
        ownerAvatarUrl = "https://example.com/users/2/avatar.png",
        visibility = Repo.Visibility.Public,
        isPrivate = false,
        htmlUrl = "https://example.com/repos/2/",
    )

    private val testDispatcher = StandardTestDispatcher()

    private val viewModel = RepoListViewModel(
        getRepos = MockGetRepos::invoke,
        refreshRepos = {},
        ioDispatcher = testDispatcher,
    )

    @Test
    fun `initial state`() = runTest {
        viewModel.onLoadMore()

        assertEquals(
            RepoListViewModel.UiState(
                isLoading = false,
                hasError = false,
                repos = emptyList(),
            ),
            viewModel.uiState.value,
        )
    }

    @Test
    fun `loading then content`() = runTest {
        MockGetRepos.getReposResult = Result.success(listOf(repo1, repo2))

        viewModel.onLoadMore()

        testDispatcher.scheduler.runCurrent()
        assertEquals(
            RepoListViewModel.UiState(
                hasLoadedAll = false,
                isLoading = true,
                hasError = false,
                repos = emptyList(),
            ),
            viewModel.uiState.value,
        )

        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(
            RepoListViewModel.UiState(
                hasLoadedAll = false,
                isLoading = false,
                hasError = false,
                repos = listOf(repo1, repo2),
            ),
            viewModel.uiState.value,
        )
    }

    @Test
    fun `loading then error`() = runTest {
        MockGetRepos.getReposResult = Result.failure(IOException("403 Forbidden"))

        viewModel.onLoadMore()

        testDispatcher.scheduler.runCurrent()
        assertEquals(
            RepoListViewModel.UiState(
                hasLoadedAll = false,
                isLoading = true,
                hasError = false,
                repos = emptyList(),
            ),
            viewModel.uiState.value,
        )

        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(
            RepoListViewModel.UiState(
                hasLoadedAll = false,
                isLoading = false,
                hasError = true,
                repos = emptyList(),
            ),
            viewModel.uiState.value,
        )
    }

    @Test
    fun `stops loading more`() {
        MockGetRepos.getReposResult = Result.success(listOf(repo1, repo2))
        viewModel.onLoadMore()

        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(
            RepoListViewModel.UiState(
                hasLoadedAll = false,
                isLoading = false,
                hasError = false,
                repos = listOf(repo1, repo2),
            ),
            viewModel.uiState.value,
        )

        MockGetRepos.getReposResult = Result.success(emptyList())
        viewModel.onLoadMore()

        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(
            RepoListViewModel.UiState(
                hasLoadedAll = true,
                isLoading = false,
                hasError = false,
                repos = listOf(repo1, repo2),
            ),
            viewModel.uiState.value,
        )
    }

    object MockGetRepos {

        var getReposResult: Result<List<Repo>> =
            Result.failure(NotImplementedError("getReposResult is not initialized"))

        suspend operator fun invoke(offset: Int): List<Repo> {
            delay(1) // allow test scheduler to become idle
            return getReposResult.getOrThrow()
        }
    }
}
