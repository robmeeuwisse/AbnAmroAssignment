package com.alobarproductions.abnamrorepos.main

import com.alobarproductions.abnamrorepos.BuildConfig

object UserAgent {

    val value get() = "$applicationId/$appVersionName+$appVersionCode $httpAgent"

    private val applicationId get() = BuildConfig.APPLICATION_ID
    private val appVersionName get() = BuildConfig.VERSION_NAME
    private val appVersionCode get() = BuildConfig.VERSION_CODE
    private val httpAgent get() = System.getProperty("http.agent")
}
