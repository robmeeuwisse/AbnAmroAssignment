package com.alobarproductions.abnamrorepos.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubService {

    /**
     * List a batch of repositories ordered by full_name
     */
    @Headers(
        "Accept: application/vnd.github+json",
        "X-GitHub-Api-Version: 2022-11-28"
    )
    @GET("users/{username}/repos?sort=full_name")
    suspend fun listUserRepos(
        @Path("username") username: String,
        @Query("page") pageNumber: Int,
        @Query("per_page") pageSize: Int = 10,
    ): List<GitHubRepoResponse>
}

/**
 * GitHub Repository response
 *
 * See [GitHub API documentation](https://docs.github.com/en/rest/repos/repos?apiVersion=2022-11-28#list-organization-repositories)
 */
@Serializable
data class GitHubRepoResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("full_name")
    val fullName: String,
    @SerialName("owner")
    val owner: GitHubRepoOwner,
    @SerialName("private")
    val private: Boolean,
    @SerialName("html_url")
    val htmlUrl: String,
    @SerialName("description")
    val description: String?,
    @SerialName("visibility")
    val visibility: GitHubVisibility?,
)

@Serializable
data class GitHubRepoOwner(
    @SerialName("avatar_url")
    val avatarUrl: String,
)

@Serializable
enum class GitHubVisibility {
    @SerialName("public")
    Public,

    @SerialName("private")
    Private
}