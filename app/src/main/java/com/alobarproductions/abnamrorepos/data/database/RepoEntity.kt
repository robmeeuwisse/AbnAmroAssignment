package com.alobarproductions.abnamrorepos.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Entity(tableName = "repos")
data class RepoEntity(
    @PrimaryKey
    @ColumnInfo("repo_id")
    val id: Long,

    @ColumnInfo("name")
    val name: String,

    @ColumnInfo("full_name")
    val fullName: String,

    @ColumnInfo("description")
    val description: String?,

    @ColumnInfo("owner_avatar_url")
    val ownerAvatarUrl: String,

    @TypeConverters(VisibilityConverter::class)
    @ColumnInfo("visibility")
    val visibility: Visibility,

    @ColumnInfo("private")
    val isPrivate: Boolean,

    @ColumnInfo("html_url")
    val htmlUrl: String,
) {
    enum class Visibility(val serialName: String) {
        Private("private"),
        Public("public"),
        Unset("unset");
    }

    private object VisibilityConverter {

        @TypeConverter
        fun fromString(serialName: String): Visibility {
            return Visibility.entries.first { it.serialName == serialName }
        }

        @TypeConverter
        fun toString(status: Visibility): String {
            return status.serialName
        }
    }
}
