package com.alobarproductions.abnamrorepos.core

import java.net.URL

interface ReposRepository {
    suspend fun getAll(): List<Repo>
}

data class Repo(
    val name: String,
    val ownerAvatarUrl: String,
    val visibility: Visibility,
    val isPrivate: Boolean,
) {
    enum class Visibility { Public, Private, Unknown }
}
