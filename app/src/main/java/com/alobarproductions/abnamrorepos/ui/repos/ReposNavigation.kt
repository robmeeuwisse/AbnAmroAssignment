package com.alobarproductions.abnamrorepos.ui.repos

import android.os.Parcelable
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.alobarproductions.abnamrorepos.ui.repos.detail.RepoDetailScreen
import com.alobarproductions.abnamrorepos.ui.repos.list.RepoListScreen
import com.alobarproductions.abnamrorepos.ui.util.openBrowser
import kotlinx.parcelize.Parcelize

private interface ReposScreen : Parcelable {

    @Parcelize
    data object List : ReposScreen

    @Parcelize
    data class Detail(val repoId: Long) : ReposScreen
}

@Composable
fun ReposNavigation() {
    val currentActivity = LocalActivity.current
    var currentScreen: ReposScreen by rememberSaveable { mutableStateOf(ReposScreen.List) }

    Crossfade(
        targetState = currentScreen,
    ) { screen ->
        when (screen) {
            is ReposScreen.List -> RepoListScreen(
                onRepoClick = { currentScreen = ReposScreen.Detail(it) }
            )

            is ReposScreen.Detail -> RepoDetailScreen(
                repoId = screen.repoId,
                onBackClick = { currentScreen = ReposScreen.List },
                onOpenRepoClick = { currentActivity?.openBrowser(it) },
            )
        }
    }
}
