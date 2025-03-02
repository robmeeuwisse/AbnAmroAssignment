package com.alobarproductions.abnamrorepos.core

interface ReposRepository {
    suspend fun getByPage(page: Int): List<Repo>
    suspend fun getById(repoId: Long): Repo
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
    enum class Visibility { Public, Private, Unknown }
}
