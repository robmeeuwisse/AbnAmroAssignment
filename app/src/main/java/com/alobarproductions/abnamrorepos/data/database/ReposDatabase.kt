package com.alobarproductions.abnamrorepos.data.database

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [RepoEntity::class],
    exportSchema = false,
)
abstract class ReposDatabase : RoomDatabase() {

    abstract fun reposDao(): ReposDao

    companion object {
        fun create(context: Context): ReposDatabase {
            return Room.databaseBuilder(
                context = context.applicationContext,
                klass = ReposDatabase::class.java,
                name = "repos.db",
            ).build()
        }

        @VisibleForTesting
        fun createInMemory(context: Context): ReposDatabase {
            return Room.inMemoryDatabaseBuilder(
                context = context.applicationContext,
                klass = ReposDatabase::class.java,
            ).build()
        }
    }
}
