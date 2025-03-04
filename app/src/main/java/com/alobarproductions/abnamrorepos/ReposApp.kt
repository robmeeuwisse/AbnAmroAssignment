package com.alobarproductions.abnamrorepos

import android.app.Application
import com.alobarproductions.abnamrorepos.main.AppContainer

class ReposApp : Application() {

    val appContainer = AppContainer(this)
}

val Application.appContainer get() = (this as ReposApp).appContainer
