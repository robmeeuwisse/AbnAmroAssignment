package com.alobarproductions.abnamrorepos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import com.alobarproductions.abnamrorepos.ui.repos.ReposNavigation
import com.alobarproductions.abnamrorepos.ui.util.LocalAppContainer

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CompositionLocalProvider(
                LocalAppContainer provides application.appContainer
            ) {
                ReposNavigation()
            }
        }
    }
}
