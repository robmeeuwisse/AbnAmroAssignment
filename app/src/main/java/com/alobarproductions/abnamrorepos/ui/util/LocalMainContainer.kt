package com.alobarproductions.abnamrorepos.ui.util

import androidx.compose.runtime.staticCompositionLocalOf
import com.alobarproductions.abnamrorepos.main.AppContainer

val LocalAppContainer = staticCompositionLocalOf<AppContainer> {
    error("CompositionLocal AppContainer not present")
}
