package com.alobarproductions.abnamrorepos.data.network

import com.alobarproductions.abnamrorepos.util.requireTextResource
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Test

class GitHubServiceTest {

    @Test
    fun `list user repos sends headers`() = runMockWebServerTest {
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(requireTextResource("/github/users/octocat/repos/list-repos.json"))
        )

        service.listUserRepos("octocat", pageNumber = 1)

        val request = server.takeRequest()

        assertEquals("test-user-agent", request.headers["User-Agent"])
        assertEquals("2022-11-28", request.headers["X-GitHub-Api-Version"])
        assertEquals("application/vnd.github+json", request.headers["Accept"])
    }

    @Test
    fun `parse user repos response`() = runMockWebServerTest {
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(requireTextResource("/github/users/octocat/repos/list-repos.json"))
        )

        val actual = service.listUserRepos("octocat", pageNumber = 1)

        val expected = listOf(
            GitHubRepoResponse(
                id = 1296269,
                name = "Hello-World",
                fullName = "octocat/Hello-World",
                owner = GitHubRepoOwner(
                    avatarUrl = "https://github.com/images/error/octocat_happy.gif",
                ),
                private = false,
                description = "This your first repo!",
                htmlUrl = "https://github.com/octocat/Hello-World",
                visibility = GitHubVisibility.Public,
            )
        )
        assertEquals(expected, actual)
    }

    private data class MockWebServerScope(val server: MockWebServer, val service: GitHubService)

    private fun runMockWebServerTest(block: suspend MockWebServerScope.() -> Unit) = runTest {
        MockWebServer().use { server ->
            val service = GitHubServiceFactory(
                baseUrl = server.url("/").toString(),
                token = "github-token",
                userAgent = "test-user-agent",
                logHttpTraffic = false,
            ).create()

            block(MockWebServerScope(server, service))
        }
    }
}
