package com.alobarproductions.abnamrorepos.ui.repos

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.alobarproductions.abnamrorepos.ui.repos.detail.RepoDetailScreen
import com.alobarproductions.abnamrorepos.ui.repos.list.RepoListScreen
import com.alobarproductions.abnamrorepos.ui.util.openBrowser

private interface ReposScreen {
    data object List : ReposScreen
    data class Detail(val repoId: Long) : ReposScreen
}

@Composable
fun ReposNavigation() {
    val currentActivity = LocalActivity.current
    var currentScreen: ReposScreen by remember { mutableStateOf(ReposScreen.List) }

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
