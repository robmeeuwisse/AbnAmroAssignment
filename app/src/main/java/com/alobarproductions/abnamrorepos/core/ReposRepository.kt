package com.alobarproductions.abnamrorepos.core

interface ReposRepository {
    suspend fun getRepos(offset: Int): List<Repo>
    suspend fun getById(repoId: Long): Repo
    suspend fun deleteAll()
}

data class Repo(
    val id: Long,
    val name: String,
    val fullName: String,
    val description: String?,
    val ownerAvatarUrl: String,
    val visibility: Visibility,
    val isPrivate: Boolean,
    val htmlUrl: String,
) {
    enum class Visibility { Public, Private, Unset }
}
