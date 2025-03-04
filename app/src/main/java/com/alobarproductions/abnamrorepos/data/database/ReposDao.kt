package com.alobarproductions.abnamrorepos.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ReposDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepos(vararg repos: RepoEntity)

    /**
     * Read a batch of repositories ordered by full_name.
     * Note that this is the same ordering that the GitHub repositories endpoint uses.
     */
    @Query("SELECT * FROM repos ORDER BY LOWER(full_name) ASC LIMIT :limit OFFSET :offset")
    suspend fun readRepos(offset: Int, limit: Int): List<RepoEntity>

    @Query("SELECT * FROM repos WHERE repo_id = :repoId")
    suspend fun readRepoById(repoId: Long): RepoEntity?

    @Query("DELETE FROM repos")
    suspend fun deleteAll()
}
