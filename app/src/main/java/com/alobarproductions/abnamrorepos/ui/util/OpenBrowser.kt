package com.alobarproductions.abnamrorepos.ui.util

import android.app.Activity
import android.content.Intent
import android.net.Uri

fun Activity.openBrowser(htmlUrl: String) {
    val htmlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(htmlUrl))
    startActivity(Intent.createChooser(htmlIntent, null))
}
